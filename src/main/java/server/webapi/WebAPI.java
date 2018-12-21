package server.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import data.avatarRepository.AvatarRepository;
import data.dailystreakrepository.DailyRepository;
import data.gamerepository.GameRepository;
import data.JdbcInteractor;
import data.TetrisRepository;
import data.loggedinrepository.LoggedInRepository;
import data.Repositories;
import data.loginrepository.LoginRepository;
import domain.Avatar;
import domain.User;
import domain.dailystreak.ControlDailyStreak;
import domain.dailystreak.ScratchCard;
import domain.game.Game;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.pmw.tinylog.Logger;
import server.Config;
import server.webapi.util.SecureFilePath;
import util.MatchableException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * All the communication setup and basic handlers.
 */
public class WebAPI extends AbstractVerticle {
    private static final String SOCKET_URL_DOT = "tetris.events.";
    private static final String REGISTER = "/register.html";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private LoginRepository repo = Repositories.getInstance().getLoginRepository();
    private final LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();
    private final GameRepository gameRepo = Repositories.getInstance().getGameRepository();
    private AvatarRepository avatarRepo = Repositories.getInstance().getAvatarRepository();
    private DailyRepository repoDaily = Repositories.getInstance().getDailyRepository();

    private String sessionID = null;

    @Override
    public void start() {
        
        this.initDB();
        
        
        final HttpServer server = vertx.createHttpServer();
        final Router router = Router.router(vertx);
        final Routes routes = new Routes();

        // We need a cookie handler first
        router.route().handler(CookieHandler.create());
        // Create a clustered session store using defaults
        final SessionStore store = LocalSessionStore.create(vertx);
        final SessionHandler sessionHandler = SessionHandler.create(store);
        // Make sure all requests are routed through the session handler too
        router.route().handler(sessionHandler);

        router.route("/").handler(routes::rootHandler);

        router.route(Config.STATIC_FILE_URL + '*').handler(BodyHandler.create());
        router.post(Config.STATIC_FILE_URL).handler(routes::loginHandler);
        router.post(Config.STATIC_FILE_URL + REGISTER).handler(routes::registerHandler);

        router.route(Config.STATIC_FILE_URL).handler(
            routingContext -> routes.rerouteSpecificHandler(routingContext, Routes.INDEX_REF)
        );
        router.route(Config.STATIC_FILE_URL + REGISTER).handler(
            routingContext -> routes.rerouteSpecificHandler(routingContext, Routes.REGISTER_REF)
        );
        router.route(Config.STATIC_FILE_URL + "/index.html").handler(routes::rerouteHandler);

        router.route("/tetris-16/api/getHeroes").handler(routes::heroHandler);

        for (SecureFilePath secureFilePath : SecureFilePath.values()) {
            router.route(Config.STATIC_FILE_URL + secureFilePath).handler(routingContext ->
                    routes.secureHandler(routingContext, secureFilePath));
        }

        router.route(Config.STATIC_FILE_URL + "/*").handler(StaticHandler.create());
        router.route("/tetris-16/socket/*").handler(new TetrisSockJSHandler(vertx).create("tetris\\.events\\..+"));
        router.route(Config.REST_ENDPOINT + "logout").handler(routes::logoutHandler);

        //router.route("/static/pages/main_menu.html").handler(routes::dailyStreakHandler);

        server.requestHandler(router::accept).listen(Config.WEB_PORT);

        // MATCH_MAKING
        vertx.setPeriodic(7500, this::makeMatchHandler);

        initConsumers();
        initGameConsumers();
    }

    private void initDB() {
        new JdbcInteractor().startDBServer();
        TetrisRepository.populateDB();
    }

    private void makeMatchHandler(final Long aLong) {
        Logger.debug(aLong);
        final Set<Match> matched = MatchHandler.getInstance().matchUsers();
        //Logger.info("Matched users: " + matched);

        matched.forEach(match -> {
            final Game game = new Game(match);
            gameRepo.addActiveGame(game);

            final Map<String, String> data = new HashMap<>();

            data.put("match", game.genGameAddress());
            data.put("amountOfPlayers", Integer.toString(match.getUsers().size()));

            final String json;

            try {
                json = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                throw new MatchableException("json data is not okay ¯\\_(ツ)_/¯", e);
            }

            match.getUsers().forEach(user -> {
                final String clientMatch = "client.match.";
                Logger.warn(user.getUsername() + " " + SOCKET_URL_DOT + clientMatch + loggedInRepo.getSessionID(user));
                vertx.eventBus().publish(SOCKET_URL_DOT + clientMatch + loggedInRepo.getSessionID(user), json);
            });

            //game.startGame();
        });

        // TODO: send response to users activate a game
    }

    private void initConsumers() {
        EventBus eb = vertx.eventBus();
        // sessionInfo
        eb.consumer("tetris.events.sessionInfo", this::cSessionInfo);

        // rewards
        eb.consumer("tetris.events.reward", this::cReward);

        // scratch card
        eb.consumer("tetris.events.priceScratchCard", this::cScratchCard);

        // mystery box
        eb.consumer("tetris.events.priceMysteryBox", this::cMysteryBox);

        eb.consumer("tetris.events.mysteryBoxRequest", this::cMysteryBoxWon);
        eb.consumer("tetris.events.receivedMBReward", this::cMysteryBoxReceived);

        // change avatar
        eb.consumer("tetris.events.changeAvatarRequest", this::cChangeAvatar);
        eb.consumer("tetris.events.newAvatar", this::cNewAvatar);

    }

    private void cSessionInfo(Message message) {
        try{
            Map<String, Object> jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Session request received: " + jsonMap);

            sessionID(jsonMap);
            ControlDailyStreak controlDailyStreak = new ControlDailyStreak(sessionID);
            controlDailyStreak.control();
            rewards();
        }catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");

    }

    private void cReward(Message message) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Reward request received: " + jsonMap);

            String reward = String.valueOf(jsonMap.get("reward"));
            System.out.println("reward" + reward);

            switch (reward){
                case "xp":
                    addRewardXPToUsersAccount(jsonMap);
                    userReceivedReward(jsonMap);
                    break;
                case "scratch card":
                    scratchCard();
                    break;
                case "cubes":
                    addRewardCubesToUsersAccount(jsonMap);
                    userReceivedReward(jsonMap);
                    break;
                case "mystery box":
                    mysterybox();
                    break;
            }
        }catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }


    private void cScratchCard(Message message) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Reward request received: " + jsonMap);
            String type = "ScratchCard";

            addRewardToUser(jsonMap, type);

        }catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }

    private void cMysteryBox(Message message) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Reward request received: " + jsonMap);
            String type = "MysteryBox";

            addRewardToUser(jsonMap, type);

        }catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }

    private void cMysteryBoxWon(Message message) {
        try {
            Map<String, Object> mysteryBoxPriceRequest = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Reward request received: " + mysteryBoxPriceRequest);

            mysteryBoxSendPrice();

        } catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }

    private void cMysteryBoxReceived(Message message) {
        try {
            Map<String, Object> mysteryBoxReceivedReward = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Reward request received: " + mysteryBoxReceivedReward);
            String type = "MysteryBox";

            addRewardToUser(mysteryBoxReceivedReward, type);
            userReceivedReward(mysteryBoxReceivedReward);

        } catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }

    private void cChangeAvatar(Message message) {
        try{
            Map<String, Object> jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Change avatar request received: " + jsonMap);

            sendAllUsersAvatars();
            System.out.println("change avatar page");
        }catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }

    private void cNewAvatar(Message message) {
        try{
            Map<String, Object> jsonMap = objectMapper.readValue(message.body().toString(), new TypeReference<Map<String, Object>>(){});
            Logger.warn("Received new avatar: " + jsonMap);

            setNewAvatarToUsersAccount(jsonMap);
            System.out.println("change avatar");
        }catch (IOException e) {
            System.err.println("Something went wrong with");
        }
        message.reply("thx");
    }

    private void setNewAvatarToUsersAccount(Map<String,Object> jsonMap) {
        String newAvatar = jsonMap.get("newAvatar").toString();
        System.out.println("newAvatar " + newAvatar);

        int avatarID = avatarRepo.getAvatarID(newAvatar).getID();
        System.out.println("newAvatar ID " + avatarID);

        int userID = loggedInRepo.getLoggedUser(sessionID).getId();
        System.out.println("user ID " + userID);

        avatarRepo.changeAvatar(avatarID, userID);
    }


    private void sendAllUsersAvatars() {
        JsonObject allUsersAvatars = new JsonObject();

        int userID = loggedInRepo.getLoggedUser(sessionID).getId();
        List<Avatar> avatars = avatarRepo.getAllAvatarsFromUser(userID);
        allUsersAvatars.put("avatars", new Gson().toJson(avatars));

        String username = loggedInRepo.getLoggedUser(sessionID).getUsername();
        allUsersAvatars.put("user", new Gson().toJson(repo.getUser(username)));

        int avatarID = repo.getUser(username).getAvatarID();
        System.out.println("avatar " + avatarID);
        allUsersAvatars.put("avatar", new Gson().toJson(avatarRepo.getAvatar(avatarID)));

        vertx.eventBus().send("tetris.events.allAvatars", Json.encode(allUsersAvatars));
    }


    private void mysteryBoxSendPrice() {
        JsonObject mysteryboxWon = new JsonObject();

        int count = repoDaily.getAllMBPrices().size();
        int randomNumber = generateRandomNumber(count);

        mysteryboxWon.put("won", new Gson().toJson(repoDaily.getMBPricesById(randomNumber)));
        System.out.println("price " + Json.encode(mysteryboxWon));

        vertx.eventBus().send("tetris.events.mysteryBoxWon", Json.encode(mysteryboxWon));
    }


    private void addRewardToUser(Map<String, Object> jsonMap, String type) {
        // TODO if you won something add to DB.
        int userID = loggedInRepo.getLoggedUser(sessionID).getId();

        if (jsonMap.containsKey("won")){
            int amount;

            switch (jsonMap.get("won").toString()){
                case "xp":
                    amount = (int) jsonMap.get("amount");
                    addAmountOfXPToUser(amount);

                    break;
                case "cubes":
                    amount = (int) jsonMap.get("amount");
                    addAmountOfCubesToUser(amount);

                    break;
                case "skin":
                    System.out.println("SKIN");
                    String skinName;
                    int skinID;

                    if (type.equals("ScratchCard")){
                        skinName = repoDaily.getSkinFromSC().getName();
                        System.out.println("skinName " + skinName);
                        skinID = repoDaily.getSkinID(skinName).getID();

                        System.out.println("skin ID " + skinID + " userID " + userID);

                        repoDaily.addSkinToUser(userID, skinID);
                        userReceivedReward(jsonMap);
                        break;

                    }else {
                        skinName = repoDaily.getSkinFromMB().getName();
                        System.out.println("skinName " + skinName);
                        skinID = repoDaily.getSkinID(skinName).getID();

                        System.out.println("skin ID " + skinID + " userID " + userID);

                        repoDaily.addSkinToUser(userID, skinID);
                        userReceivedReward(jsonMap);
                        break;
                    }

                case "avatar":
                    System.out.println("AVATAR");
                    String avatarName;
                    int avatarID;

                    if (type.equals("ScratchCard")){
                        skinName = repoDaily.getAvatarFromSC().getName();
                        System.out.println("skinName " + skinName);
                        skinID = repoDaily.getAvatarID(skinName).getID();

                        System.out.println("skin ID " + skinID + " userID " + userID);

                        repoDaily.addAvatarToUser(userID, skinID);
                        userReceivedReward(jsonMap);
                        break;

                    }else {
                        avatarName = repoDaily.getAvatarFromMB().getName();
                        System.out.println("avatarName " + avatarName);
                        avatarID = repoDaily.getAvatarID(avatarName).getID();

                        System.out.println("skin ID " + avatarID + " userID " + userID);

                        repoDaily.addAvatarToUser(userID, avatarID);
                        userReceivedReward(jsonMap);
                        break;
                    }

                case "nothing":
                    System.out.println("NOTHING");
                    break;
            }
        }
        userReceivedReward(jsonMap);
    }


    private void sessionID(Map<String, Object> obj) {
        sessionID = String.valueOf(obj.get("session"));
    }

    private void addAmountOfCubesToUser(int amount) {
        String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        int amountCubes = repoDaily.getCubes(user).getCubes();
        amount = amountCubes + amount;

        repoDaily.updateCubes(amount, user);
    }

    private void addAmountOfXPToUser(int amount) {
        String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        int amountXP = repoDaily.getXP(user).getXp();
        amount = amountXP + amount;

        repoDaily.updateXP(amount, user);
    }

    private void userReceivedReward(Map<String, Object> obj) {
        Boolean alreadyLoggedInToday = (Boolean) obj.get("alreadyLoggedInToday");

        repoDaily.updateAlreaddyLoggedIn(alreadyLoggedInToday, loggedInRepo.getLoggedUser(sessionID).getUsername());
    }

    private void addRewardXPToUsersAccount(Map<String, Object> obj) {
        String reward = String.valueOf(obj.get("reward"));
        int amount = (int) obj.get("amount");
        String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        int amountXP = repoDaily.getXP(user).getXp();
        amount = amountXP + amount;

        repoDaily.updateXP(amount, user);
        System.out.println(amount + " " + reward);
    }

    private void addRewardCubesToUsersAccount(Map<String, Object> obj) {
        String reward = String.valueOf(obj.get("reward"));
        int amount = (int) obj.get("amount");
        String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        int amountCubes = repoDaily.getCubes(user).getCubes();
        amount = amountCubes + amount;

        repoDaily.updateCubes(amount, user);
        System.out.println(amount + " " + reward);
    }


    private void rewards() {
        JsonObject obj = new JsonObject();

        obj.put("rewards", new Gson().toJson(repoDaily.getAllRewards()));
        String username = loggedInRepo.getLoggedUser(sessionID).getUsername();

        obj.put("user", new Gson().toJson(repo.getUser(username)));

        int avatarID = repo.getUser(username).getAvatarID();
        obj.put("avatar", new Gson().toJson(avatarRepo.getAvatar(avatarID)));

        vertx.eventBus().send("tetris.events.rewards", Json.encode(obj));
    }

    private void scratchCard(){
        JsonObject scratchCard = new JsonObject();

        int count = repoDaily.getAllSCPrices().size();
        int amountScratchBoxes = 3;

        int[] randomNumber = new int[amountScratchBoxes];
        List<ScratchCard> rewards = new ArrayList<>();

        for (int i = 0; i < amountScratchBoxes ; i++){
            randomNumber[i] = generateRandomNumber(count);
            rewards.add(repoDaily.getSCPricesById(randomNumber[i]));
        }

        scratchCard.put("prices", new Gson().toJson(repoDaily.getAllSCPrices()));
        scratchCard.put("skin", new Gson().toJson(repoDaily.getSkinFromSC().getName()));
        //scratchCard.put("avatar", new Gson().toJson(repoDaily.getAvatarFromSC().getName()));
        scratchCard.put("scPrices", new Gson().toJson(rewards));

        vertx.eventBus().send("tetris.events.scratchCard",  Json.encode(scratchCard));
    }

    private int generateRandomNumber(int max) {
        return (int) (Math.random() * max + 1);
    }

    private void mysterybox() {
        JsonObject mysterybox = new JsonObject();

        mysterybox.put("prices", new Gson().toJson(repoDaily.getAllMBPrices()));

        mysterybox.put("skin", new Gson().toJson(repoDaily.getSkinFromMB().getName()));
        mysterybox.put("avatar", new Gson().toJson(repoDaily.getAvatarFromMB().getName()));

        vertx.eventBus().send("tetris.events.showMysteryBox", Json.encode(mysterybox));
    }



    private void initGameConsumers() {
        final EventBus eb = vertx.eventBus();
        eb.consumer(SOCKET_URL_DOT + "server.match", this::matchHandler);
    }

    private void matchHandler(final Message message) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                    message.body().toString(),
                    new TypeReference<Map<String, Object>>() { }
                    );
            Logger.warn("Match request received: " + jsonMap);

            final User user = loggedInRepo.getLoggedUser((String) jsonMap.get("session"));
            user.selectHero((String) jsonMap.get("hero"));
            final GameMode gameMode = GameMode.getGameModeByValue((String) jsonMap.get("gameMode"));
            MatchHandler.getInstance().addMatchable(user, gameMode);
            Logger.info(MatchHandler.getInstance().getMatchable());
        } catch (IOException e) {
            Logger.warn(e.getMessage());
        }

        message.reply("OK");
    }
}
