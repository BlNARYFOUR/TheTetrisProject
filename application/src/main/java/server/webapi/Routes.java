package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.LoginRepository;
import data.Repositories;
import domain.User;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.pmw.tinylog.Logger;
import server.webapi.util.FilePath;
import util.Hash;

import java.io.IOException;

public class Routes {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private LoginRepository repo = Repositories.getInstance().getLoginRepository();

    void rootHandler(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response
            .putHeader("content-type", "text/html")
            .write("<h1>Wrong page amigo...</h1><img src=static/images/facepalm.jpg><p>Goto <a href=static>here</a> instead</p>")
            .end();
    }

    void loginHandler(RoutingContext routingContext) {
        try {
            String body = routingContext.getBodyAsString();
            System.out.println(routingContext.getBodyAsString());
            String[] params = body.split("&");
            String username = params[0].split("=")[1];
            String password = params[1].split("=")[1];

            User user = new User(username, password);

            Session session = routingContext.session();
            session.put("username", user.getUsername());
            session.put("password", user.getPassword());

            user = repo.authenticateUser(user);
            if (user == null) {
                HttpServerResponse response = routingContext.response();
                response.sendFile("webroot/index.html");
            } else {
                HttpServerResponse response = routingContext.response();
                response.headers().add("location", "/static/pages/main_menu.html");
                response.setStatusCode(302).end();
            }
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.sendFile("webroot/index.html");
        }
    }

    void secureHandler(RoutingContext routingContext, FilePath filePath) {
        Session session = routingContext.session();
        try {
            User user = repo.authenticateUser(session.get("username"), session.get("password"));
            if (user == null) {
                HttpServerResponse response = routingContext.response();
                response.headers().add("location", "/static");
                response.setStatusCode(302).end();
            } else {
                routingContext.response().sendFile("webroot"+filePath);
        }
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.headers().add("location", "/static");
            response.setStatusCode(302).end();
        }
    }

    void rerouteHandler(RoutingContext routingContext) {
        Session session = routingContext.session();

        User user = repo.authenticateUser(session.get("username"), session.get("password"));

        HttpServerResponse response = routingContext.response();

        if(user == null) {
            response.headers().add("location", "/static");
        } else {
            response.headers().add("location", "/static"+FilePath.MAIN_MENU_WEB_FILE);
        }

        response.setStatusCode(302).end();
    }
}
