package server;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.pmw.tinylog.Logger;
import server.webapi.WebAPI;

public class Tetris {
    public static void main(String... args) {
        Logger.warn("Starting server");

        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        vertx.deployVerticle(new WebAPI());

        Logger.info("Succesfully started server");
    }
}
