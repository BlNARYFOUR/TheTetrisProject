package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.pmw.tinylog.Logger;
import server.webapi.WebAPI;

/**
 * Main Verticle initialising the application.
 *
 * @author  JVD
 */

public class Tetris extends AbstractVerticle {

    // JVD: Run deploy verticle -> module opstarten
    private void deploy(final Vertx vertx) {
        Logger.info("Initialised Vertx");

        vertx.deployVerticle(new WebAPI(), new DeploymentOptions(), complete -> {
            Logger.info("Deployed WebServer");
        });

    }

    @Override
    public void start() {
        deploy(vertx);
    }


    public static void main(final String... args) {
        final Vertx vertx = Vertx.vertx();
        new Tetris().deploy(vertx);

    }


}
