package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import org.pmw.tinylog.Logger;
import server.webapi.TetrisSockJSHandler;
import util.Hash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebAPI extends AbstractVerticle {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void start(){
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
        router.route("/static/*").handler(StaticHandler.create());
        router.route("/tetris/events/*").handler(new TetrisSockJSHandler(vertx).create());

        server.requestHandler(router::accept).listen(8081);

        initConsumers();
    }

    private void initConsumers() {
        vertx.eventBus().consumer("tetris.events.register.server", this::register);
    }

    private void register(Message<Object> message) {
        Logger.info("1 received message.body() = " + message.body());

        try {
            User user = objectMapper.readValue(message.body().toString(), User.class);
            user.setPassword(Hash.md5HashString(user.getPassword()));
            System.out.println(user);

            message.reply("SUCCESSFUL");
        } catch (IOException e) {
            Logger.error("No valid user registration!");
            message.reply("INVALID");
        }
    }
}
