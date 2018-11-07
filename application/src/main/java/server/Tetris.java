package server;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import server.webapi.WebAPI;

public class Tetris {
    public static void main(String... args) {
        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        vertx.deployVerticle(new WebAPI());
        System.out.println("Starting server");
    }
}
