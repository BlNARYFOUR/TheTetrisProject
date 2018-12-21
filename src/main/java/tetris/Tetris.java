package tetris;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.pmw.tinylog.Logger;
import tetris.webapi.WebServer;

/**
 * Main Verticle initialising the application.
 *
 * @author  JVD
 */

public class Tetris extends AbstractVerticle {

    // JVD: Run deploy verticle -> module opstarten
    private void deploy(Vertx vertx) {
        Logger.info("Initialised Vertx");

        vertx.deployVerticle(new WebServer(), new DeploymentOptions(), complete -> {
            Logger.info("Deployed WebServer");
        });

    }

    @Override
    public void start() {
        deploy(vertx);
    }


    public static void main(String... args) {
        final Vertx vertx = Vertx.vertx();
        new Tetris().deploy(vertx);

    }


}
