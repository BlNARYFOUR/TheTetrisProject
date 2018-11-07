package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import org.pmw.tinylog.Logger;

import java.io.IOException;

public class Routes {

    private static ObjectMapper objectMapper = new ObjectMapper();

    void rootHandler(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response
            .putHeader("content-type", "text/html")
            .write("<h1>Wrong page amigo...</h1><img src=static/images/facepalm.jpg><p>Goto <a href=static>here</a> instead</p>")
            .end();
    }

    void registerHandler(RoutingContext routingContext) {
        Session session = routingContext.session();
        Logger.info(session.id() + ":\n" + routingContext.getBodyAsJson());

        /*
        String json = "{ \"username\" : \"test\", \"password\" : \"Azerty123\" }";
        try {

            User user = objectMapper.readValue(json, User.class);
            System.out.println(user);
        } catch (IOException e) {
            Logger.error("No valid user registration!");
        }
        */
    }
}
