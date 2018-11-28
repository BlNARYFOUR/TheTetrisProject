package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.JDBCInteractor;
import data.TetrisRepository;
import data.loginRepository.LoginRepository;
import data.Repositories;
import domain.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import server.Tetris;
import server.webapi.util.SecureFilePath;
import util.Hash;

import javax.smartcardio.TerminalFactory;
import java.io.IOException;

public class WebAPI extends AbstractVerticle {
    private ObjectMapper objectMapper = new ObjectMapper();
    private LoginRepository repo = Repositories.getInstance().getLoginRepository();

    @Override
    public void start() {
        
        this.initDB();
        TetrisRepository.populateDB();
        
        
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Routes routes = new Routes();

        // We need a cookie handler first
        router.route().handler(CookieHandler.create());
        // Create a clustered session store using defaults
        SessionStore store = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        // Make sure all requests are routed through the session handler too
        router.route().handler(sessionHandler);

        router.route("/").handler(routes::rootHandler);

        router.route("/static*").handler(BodyHandler.create());
        router.post("/static").handler(routes::loginHandler);
        router.post("/static/pages/register.html").handler(routes::registerHandler);

        router.route("/static").handler(routes::rerouteWebrootHandler);
        router.route("/static/index.html").handler(routes::rerouteHandler);

        for (SecureFilePath secureFilePath : SecureFilePath.values()) {
            router.route("/static" + secureFilePath).handler(routingContext ->
                    routes.secureHandler(routingContext, secureFilePath));
        }


        router.route("/static/*").handler(StaticHandler.create());
        router.route("/tetris/events/*").handler(new TetrisSockJSHandler(vertx).create());
        router.route("/logout").handler(routes::logoutHandler);

        //router.route("/static/pages/main_menu.html").handler(routes::dailyStreakHandler);



        server.requestHandler(router::accept).listen(8081);

        initConsumers();
    }

    private void initDB() {
        new JDBCInteractor().startDBServer();
    }

    private void initConsumers() {
        /*
        TODO
         */
    }
}
