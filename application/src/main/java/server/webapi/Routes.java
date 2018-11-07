package server.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    void rootHandler(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response
            .putHeader("content-type", "text/html")
            .write("<h1>Wrong page amigo...</h1><img src=static/images/facepalm.jpg><p>Goto <a href=static>here</a> instead</p>")
            .end();
    }
}
