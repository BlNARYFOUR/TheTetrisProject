package server.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.gamerepository.GameRepository;
import data.JdbcInteractor;
import data.TetrisRepository;
import data.loggedinrepository.LoggedInRepository;
import data.Repositories;
import domain.User;
import domain.game.Game;
import domain.game.matchmaking.Match;
import domain.game.matchmaking.MatchHandler;
import domain.game.modes.GameMode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.pmw.tinylog.Logger;
import server.Config;
import server.webapi.util.SecureFilePath;
import util.MatchableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * All the communication setup and basic handlers.
 */
public class WebAPI extends AbstractVerticle {
    private static final String SOCKET_URL_DOT = "tetris.events.";

    private final ObjectMapper objectMapper = new ObjectMapper();
    // private LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private final LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();
    private final GameRepository gameRepo = Repositories.getInstance().getGameRepository();

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
        router.post(Config.STATIC_FILE_URL + "/pages/register.html").handler(routes::registerHandler);

        router.route(Config.STATIC_FILE_URL).handler(routes::rerouteWebrootHandler);
        //router.route(Config.STATIC_FILE_URL + "/index.html").handler(routes::loginHandler);

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
        /*
        TODO
         */
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
