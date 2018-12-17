package server.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.GameRepository.GameRepository;
import data.JDBCInteractor;
import data.TetrisRepository;
import data.loggedInRepository.LoggedInRepository;
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
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.pmw.tinylog.Logger;
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
    private static final String STATIC_REF = "/static";

    private ObjectMapper objectMapper = new ObjectMapper();
    // private LoginRepository loginRepo = Repositories.getInstance().getLoginRepository();
    private LoggedInRepository loggedInRepo = Repositories.getInstance().getLoggedInRepository();
    private GameRepository gameRepo = Repositories.getInstance().getGameRepository();

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

        router.route("/static*").handler(BodyHandler.create());
        router.post(STATIC_REF).handler(routes::loginHandler);
        router.post("/static/pages/register.html").handler(routes::registerHandler);

        router.route(STATIC_REF).handler(routes::rerouteWebrootHandler);
        router.route("/static/index.html").handler(routes::rerouteHandler);

        for (SecureFilePath secureFilePath : SecureFilePath.values()) {
            router.route(STATIC_REF + secureFilePath).handler(routingContext ->
                    routes.secureHandler(routingContext, secureFilePath));
        }


        router.route("/static/*").handler(StaticHandler.create());
        router.route("/tetris-16/socket/*").handler(new TetrisSockJSHandler(vertx).create());
        router.route("/logout").handler(routes::logoutHandler);

        //router.route("/static/pages/main_menu.html").handler(routes::dailyStreakHandler);

        server.requestHandler(router::accept).listen(8016);

        // MATCH_MAKING
        vertx.setPeriodic(7500, this::makeMatchHandler);

        initConsumers();
        initGameConsumers();
    }

    private void initDB() {
        new JDBCInteractor().startDBServer();
        TetrisRepository.populateDB();
    }

    private void makeMatchHandler(Long aLong) {
        final Set<Match> matched = MatchHandler.getInstance().matchUsers();
        //Logger.info("Matched users: " + matched);

        matched.forEach(match -> {
            final Game game = new Game(match);
            gameRepo.addActiveGame(game);

            final Map<String, String> data = new HashMap<>();

            data.put("match", game.getGameAddress());
            data.put("amountOfPlayers", Integer.toString(match.getUsers().size()));

            final String json;

            try {
                json = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                throw new MatchableException("json data is not okay ¯\\_(ツ)_/¯");
            }

            match.getUsers().forEach(user -> {
                Logger.warn(user.getUsername() + " tetris-16.socket.client.match." + loggedInRepo.getSessionID(user));
                vertx.eventBus().publish("tetris-16.socket.client.match." + loggedInRepo.getSessionID(user), json);
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

        eb.consumer("tetris-16.socket.server.match", this::matchHandler);
    }

    private void matchHandler(Message message) {
        try {
            final Map<String, Object> jsonMap = objectMapper.readValue(
                    message.body().toString(),
                    new TypeReference<Map<String, Object>>() { }
                    );
            Logger.warn("Match request received: " + jsonMap);

            final User user = loggedInRepo.getLoggedUser((String) jsonMap.get("session"));
            user.selectHero((String) jsonMap.get("hero"));
            final GameMode gameMode = GameMode.valueOf((String) jsonMap.get("gameMode"));

            MatchHandler.getInstance().addMatchable(user, gameMode);
            Logger.info(MatchHandler.getInstance().getMatchable());
        } catch (IOException e) {
            e.getMessage();
        }

        message.reply("OK");
    }
}
