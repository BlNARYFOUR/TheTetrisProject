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

    void secureHandler(RoutingContext routingContext) {
        System.out.println("gets here");
        Session session = routingContext.session();
        try {
            if (repo.authenticateUser(session.get("username"), session.get("password")) == null) {
                HttpServerResponse response = routingContext.response();
                response.headers().add("location", "/static");
                response.setStatusCode(302).end();
            } else {
                routingContext.response().sendFile("webroot/pages/main_menu.html");
        }
        } catch (Exception ex) {
            HttpServerResponse response = routingContext.response();
            response.headers().add("location", "/static");
            response.setStatusCode(302).end();
        }
    }
}
