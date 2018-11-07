package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.LoginRepository;
import data.Repositories;
import domain.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
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
    private LoginRepository repo = Repositories.getInstance().getLoginRepository();
    private SessionStore store;

    @Override
    public void start(){
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Routes routes = new Routes();

        // We need a cookie handler first
        router.route().handler(CookieHandler.create());
        // Create a clustered session store using defaults
         store = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        // Make sure all requests are routed through the session handler too
        router.route().handler(sessionHandler);

        router.route("/").handler(routes::rootHandler);

        /*
        router.route("/static").handler(BodyHandler.create());
        router.post("/static").handler(routingContext -> {
            System.out.println(routingContext.getBodyAsString());

            try {
                System.out.println(routingContext.getBodyAsJsonArray().toString());
            } catch (IOException e) {
                Logger.error("No valid user registration!");
            }
            

            Session session = routingContext.session();
            try {
                if (repo.authenticateUser(session.get("username"), session.get("password")) == null) {
                    HttpServerResponse response = routingContext.response();
                    response.sendFile("webroot/index.html");
                } else {
                    HttpServerResponse response = routingContext.response();
                    response.headers().add("location", "/static/pages/main_menu.html");
                    response.setStatusCode(302);
                }
            } catch (Exception ex) {
                HttpServerResponse response = routingContext.response();
                response.sendFile("webroot/index.html");
            }
        });
        router.route("/static/pages/main_menu.html").handler(routes::secureHandler);
        */

        router.route("/static/*").handler(StaticHandler.create());
        router.route("/tetris/events/*").handler(new TetrisSockJSHandler(vertx).create());

        server.requestHandler(router::accept).listen(8081);

        initConsumers();
    }

    private void initConsumers() {
        vertx.eventBus().consumer("tetris.events.register.server", this::register);
}

    private void register(Message<Object> message) {
        String address = message.replyAddress();
        System.out.println(address);
        Logger.info("1 received message = " + message);

        try {
            User user = objectMapper.readValue(message.body().toString(), User.class);
            user.setPassword(Hash.md5HashString(user.getPassword()));
            System.out.println(user);

            message.reply("");
        } catch (IOException e) {
            Logger.error("No valid user registration!");
            message.reply("INVALID");
        }
    }
}
