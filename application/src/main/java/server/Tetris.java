package server;

import io.vertx.core.Vertx;
import org.pmw.tinylog.Logger;
import server.webapi.WebAPI;

/**
 * deploy a new WebAPI.
 */
public final class Tetris {
    private Tetris() {

    }

    public static void main(final String... args) {
        //Logger.warn("Starting server");
        Logger.info("Starting server");

        final Vertx vertx = Vertx.vertx();
        //final EventBus eb = vertx.eventBus();
        vertx.deployVerticle(new WebAPI());


        Logger.info("Succesfully started server: localhost:8016/static");
    }
}
