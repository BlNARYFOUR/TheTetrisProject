package server.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import data.avatarrepository.AvatarRepository;
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
import java.util.*;

/**
 * All the communication setup and basic handlers.
 */
@SuppressWarnings({"ClassFanOutComplexity", "ClassDataAbstractionCoupling", "PMD"})
public class WebAPI extends AbstractVerticle {
    private static final String SOCKET_URL_DOT = "tetris.events.";
    private static final String REGISTER = "/register.html";
    private static final String PRICES = "prices";
    private static final String SESSION_STR = "session";
    private static final String USER_ID_STR = " userID ";
    private static final String SKIN_ID_STR = "skin ID ";
    private static final String SKIN_NAME_STR = "skinName ";
    private static final String SKIN_STR = "skin";
    private static final String AMOUNT_STR = "amount";
    private static final String WON_STR = "won";
    private static final String AVATAR_STR = "avatar";
    private static final String USER_STR = "user";
    private static final String MYSTERYBOX_STR = "MysteryBox";
    private static final String SCRATCHCARD_STR = "ScratchCard";
    private static final String CUBES_STR = "cubes";
    private static final String XP_STR = "xp";
    private static final String REWARD_STR = "reward";
    private static final String REWARD_REQ_REC_STR = "Reward request received: ";
    private static final String THX_STR = "thx";
    private static final String SOMETHING_WENT_WRONG_STR = "Something went wrong with";
    private static final String SPACE_STR = " ";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LoginRepository repo = Repositories.getInstance().getLoginRepository();
    private final LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();
    private final GameRepository gameRepo = Repositories.getInstance().getGameRepository();
    private final AvatarRepository avatarRepo = Repositories.getInstance().getAvatarRepository();
    private final DailyRepository repoDaily = Repositories.getInstance().getDailyRepository();

    private String sessionID;

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
            router.route(Config.STATIC_FILE_URL + '/' + secureFilePath).handler(routingContext ->
                    routes.secureHandler(routingContext, secureFilePath));
        }

        router.route(Config.STATIC_FILE_URL + "/*").handler(StaticHandler.create());
        router.route("/tetris-16/socket/*").handler(new TetrisSockJSHandler(vertx).create("tetris\\.events\\..+"));
        router.route(Config.STATIC_FILE_URL + "/logout").handler(routes::logoutHandler);

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
                Logger.warn(user.getUsername() + SPACE_STR + SOCKET_URL_DOT + clientMatch
                        + loggedInRepo.getSessionID(user));
                vertx.eventBus().publish(SOCKET_URL_DOT + clientMatch + loggedInRepo.getSessionID(user), json);
            });

            //game.startGame();
        });

        // TODO: send response to users activate a game
    }

    private void initConsumers() {
        final EventBus eb = vertx.eventBus();
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

    private void cSessionInfo(final Message message) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn("Session request received: " + jsonMap);

            setSessionID(jsonMap);
            final ControlDailyStreak controlDailyStreak = new ControlDailyStreak(sessionID);
            controlDailyStreak.control();
            rewards();
        } catch (IOException e) {
            Logger.warn(SOMETHING_WENT_WRONG_STR);
        }
        message.reply(THX_STR);

    }

    private void cReward(final Message message) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn(REWARD_REQ_REC_STR + jsonMap.toString());

            final String reward = String.valueOf(jsonMap.get(REWARD_STR));
            Logger.info(REWARD_STR + reward);

            switch (reward) {
                case XP_STR:
                    addRewardXPToUsersAccount(jsonMap);
                    userReceivedReward(jsonMap);
                    break;
                case "scratch card":
                    scratchCard();
                    break;
                case CUBES_STR:
                    addRewardCubesToUsersAccount(jsonMap);
                    userReceivedReward(jsonMap);
                    break;
                case "mystery box":
                    mysterybox();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Logger.info(SOMETHING_WENT_WRONG_STR);
        }
        message.reply(THX_STR);
    }


    private void cScratchCard(final Message message) {
        onMysteryOrScratch(message, SCRATCHCARD_STR);
        message.reply(THX_STR);
    }

    private void onMysteryOrScratch(final Message message, final String name) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn(REWARD_REQ_REC_STR + jsonMap.toString());

            addRewardToUser(jsonMap, name);

        } catch (IOException e) {
            Logger.info(SOMETHING_WENT_WRONG_STR);
        }
    }

    private void cMysteryBox(final Message message) {
        onMysteryOrScratch(message, MYSTERYBOX_STR);
        message.reply(THX_STR);
    }

    private void cMysteryBoxWon(final Message message) {
        try {
            final Map<String, Object> mysteryBoxPriceRequest = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn(REWARD_REQ_REC_STR + mysteryBoxPriceRequest.toString());

            mysteryBoxSendPrice();

        } catch (IOException e) {
            Logger.info(SOMETHING_WENT_WRONG_STR);
        }
        message.reply(THX_STR);
    }

    private void cMysteryBoxReceived(final Message message) {
        try {
            final Map<String, Object> mysteryBoxReceivedReward = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn(REWARD_REQ_REC_STR + mysteryBoxReceivedReward.toString());

            addRewardToUser(mysteryBoxReceivedReward, MYSTERYBOX_STR);
            userReceivedReward(mysteryBoxReceivedReward);

        } catch (IOException e) {
            Logger.info(SOMETHING_WENT_WRONG_STR);
        }
        message.reply(THX_STR);
    }

    private void cChangeAvatar(final Message message) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn("Change avatar request received: " + jsonMap);

            sendAllUsersAvatars();
            Logger.info("change avatar page");
        } catch (IOException e) {
            Logger.info(SOMETHING_WENT_WRONG_STR);
        }
        message.reply(THX_STR);
    }

    private void cNewAvatar(final Message message) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                message.body().toString(),
                new TypeReference<Map<String, Object>>() { }
            );
            Logger.warn("Received new avatar: " + jsonMap);

            setNewAvatarToUsersAccount(jsonMap);
            Logger.info("change avatar");
        } catch (IOException e) {
            Logger.info(SOMETHING_WENT_WRONG_STR);
        }
        message.reply(THX_STR);
    }

    private void setNewAvatarToUsersAccount(final Map<String, Object> jsonMap) {
        final String newAvatar = jsonMap.get("newAvatar").toString();
        Logger.info("newAvatar " + newAvatar);

        final int avatarID = avatarRepo.getAvatarID(newAvatar).getId();
        Logger.info("newAvatar ID " + avatarID);

        final int userID = loggedInRepo.getLoggedUser(sessionID).getId();
        Logger.info("user ID " + userID);

        avatarRepo.changeAvatar(avatarID, userID);
    }


    private void sendAllUsersAvatars() {
        final JsonObject allUsersAvatars = new JsonObject();

        final int userID = loggedInRepo.getLoggedUser(sessionID).getId();
        final List<Avatar> avatars = avatarRepo.getAllAvatarsFromUser(userID);
        allUsersAvatars.put("avatars", new Gson().toJson(avatars));

        final String username = loggedInRepo.getLoggedUser(sessionID).getUsername();
        allUsersAvatars.put(USER_STR, new Gson().toJson(repo.getUser(username)));

        final int avatarID = repo.getUser(username).getAvatarID();
        Logger.info("avatar " + avatarID);
        allUsersAvatars.put(AVATAR_STR, new Gson().toJson(avatarRepo.getAvatar(avatarID)));

        vertx.eventBus().send("tetris.events.allAvatars", Json.encode(allUsersAvatars));
    }


    private void mysteryBoxSendPrice() {
        final JsonObject mysteryboxWon = new JsonObject();

        final int count = repoDaily.getAllMBPrices().size();
        final int randomNumber = generateRandomNumber(count);

        mysteryboxWon.put(WON_STR, new Gson().toJson(repoDaily.getMBPricesById(randomNumber)));
        Logger.info("price " + Json.encode(mysteryboxWon));

        vertx.eventBus().send("tetris.events.mysteryBoxWon", Json.encode(mysteryboxWon));
    }

    @SuppressWarnings("PMD")
    private void addRewardToUser(Map<String, Object> jsonMap, String type) {
        // TODO if you won something add to DB.
        final int userID = loggedInRepo.getLoggedUser(sessionID).getId();

        if (jsonMap.containsKey(WON_STR)) {
            final int amount;

            switch (jsonMap.get(WON_STR).toString()) {
                case XP_STR:
                    amount = (int) jsonMap.get(AMOUNT_STR);
                    addAmountOfXPToUser(amount);

                    break;
                case CUBES_STR:
                    amount = (int) jsonMap.get(AMOUNT_STR);
                    addAmountOfCubesToUser(amount);

                    break;
                case SKIN_STR:
                    Logger.info(SKIN_STR);
                    final String skinName;
                    final int skinID;

                    if (SCRATCHCARD_STR.equals(type)) {
                        skinName = repoDaily.getSkinFromSC().getName();
                        Logger.info(SKIN_NAME_STR + skinName);
                        skinID = repoDaily.getSkinID(skinName).getId();

                        Logger.info(SKIN_ID_STR + skinID + USER_ID_STR + userID);

                        repoDaily.addSkinToUser(userID, skinID);
                        userReceivedReward(jsonMap);
                        break;

                    } else {
                        skinName = repoDaily.getSkinFromMB().getName();
                        Logger.info(SKIN_NAME_STR + skinName);
                        skinID = repoDaily.getSkinID(skinName).getId();

                        Logger.info(SKIN_ID_STR + skinID + USER_ID_STR + userID);

                        repoDaily.addSkinToUser(userID, skinID);
                        userReceivedReward(jsonMap);
                        break;
                    }

                case AVATAR_STR:
                    doAvatarStuff(type, userID, jsonMap);
                    break;
                case "nothing":
                    Logger.info("NOTHING");
                    break;
                default:
                    Logger.info("DEFAULT");
                    break;
            }
        }
        userReceivedReward(jsonMap);
    }

    private void doAvatarStuff(final String type, final int userID, final Map<String, Object> jsonMap) {
        Logger.info(AVATAR_STR);
        final String avatarName;
        final int avatarID;

        if (SCRATCHCARD_STR.equals(type)) {
            final String skinName = repoDaily.getAvatarFromSC().getName();
            Logger.info(SKIN_NAME_STR + skinName);
            final int skinID = repoDaily.getAvatarID(skinName).getId();

            Logger.info(SKIN_ID_STR + skinID + USER_ID_STR + userID);

            repoDaily.addAvatarToUser(userID, skinID);
            userReceivedReward(jsonMap);

        } else {
            avatarName = repoDaily.getAvatarFromMB().getName();
            Logger.info("avatarName " + avatarName);
            avatarID = repoDaily.getAvatarID(avatarName).getId();

            Logger.info(SKIN_ID_STR + avatarID + USER_ID_STR + userID);

            repoDaily.addAvatarToUser(userID, avatarID);
            userReceivedReward(jsonMap);
        }
    }

    private void setSessionID(final Map<String, Object> obj) {
        sessionID = String.valueOf(obj.get(SESSION_STR));
    }

    private void addAmountOfCubesToUser(final int amount) {
        final String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        final int amountCubes = repoDaily.getCubes(user).getCubes();
        final int amountBuf = amountCubes + amount;

        repoDaily.updateCubes(amountBuf, user);
    }

    private void addAmountOfXPToUser(final int amount) {
        final String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        final int amountXP = repoDaily.getXP(user).getXp();
        final int amountBuf = amountXP + amount;

        repoDaily.updateXP(amountBuf, user);
    }

    private void userReceivedReward(final Map<String, Object> obj) {
        final Boolean alreadyLoggedInToday = (Boolean) obj.get("alreadyLoggedInToday");

        repoDaily.updateAlreadyLoggedIn(alreadyLoggedInToday, loggedInRepo.getLoggedUser(sessionID).getUsername());
    }

    private void addRewardXPToUsersAccount(final Map<String, Object> obj) {
        final String reward = String.valueOf(obj.get(REWARD_STR));
        int amount = (int) obj.get(AMOUNT_STR);
        final String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        final int amountXP = repoDaily.getXP(user).getXp();
        amount = amountXP + amount;

        repoDaily.updateXP(amount, user);
        Logger.info(amount + SPACE_STR + reward);
    }

    private void addRewardCubesToUsersAccount(final Map<String, Object> obj) {
        final String reward = String.valueOf(obj.get(REWARD_STR));
        int amount = (int) obj.get(AMOUNT_STR);
        final String user = loggedInRepo.getLoggedUser(sessionID).getUsername();

        final int amountCubes = repoDaily.getCubes(user).getCubes();
        amount = amountCubes + amount;

        repoDaily.updateCubes(amount, user);
        Logger.info(amount + SPACE_STR + reward);
    }


    private void rewards() {
        final JsonObject obj = new JsonObject();

        obj.put("rewards", new Gson().toJson(repoDaily.getAllRewards()));
        final String username = loggedInRepo.getLoggedUser(sessionID).getUsername();

        obj.put(USER_STR, new Gson().toJson(repo.getUser(username)));

        final int avatarID = repo.getUser(username).getAvatarID();
        obj.put(AVATAR_STR, new Gson().toJson(avatarRepo.getAvatar(avatarID)));

        vertx.eventBus().send("tetris.events.rewards", Json.encode(obj));
    }

    private void scratchCard() {
        final JsonObject scratchCard = new JsonObject();

        final int count = repoDaily.getAllSCPrices().size();
        final int amountScratchBoxes = 3;

        final int[] randomNumber = new int[amountScratchBoxes];
        final List<ScratchCard> rewards = new ArrayList<>();

        for (int i = 0; i < amountScratchBoxes; i++) {
            randomNumber[i] = generateRandomNumber(count);
            rewards.add(repoDaily.getSCPricesById(randomNumber[i]));
        }

        scratchCard.put(PRICES, new Gson().toJson(repoDaily.getAllSCPrices()));
        scratchCard.put(SKIN_STR, new Gson().toJson(repoDaily.getSkinFromSC().getName()));
        //scratchCard.put(AVATAR_STR, new Gson().toJson(repoDaily.getAvatarFromSC().getName()));
        scratchCard.put("scPrices", new Gson().toJson(rewards));

        vertx.eventBus().send("tetris.events.scratchCard", Json.encode(scratchCard));
    }

    private int generateRandomNumber(final int max) {
        return (int) (Math.random() * max + 1);
    }

    private void mysterybox() {
        final JsonObject mysterybox = new JsonObject();

        mysterybox.put(PRICES, new Gson().toJson(repoDaily.getAllMBPrices()));

        mysterybox.put(SKIN_STR, new Gson().toJson(repoDaily.getSkinFromMB().getName()));
        mysterybox.put(AVATAR_STR, new Gson().toJson(repoDaily.getAvatarFromMB().getName()));

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

            final User user = loggedInRepo.getLoggedUser((String) jsonMap.get(SESSION_STR));
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
