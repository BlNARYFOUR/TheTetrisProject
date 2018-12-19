package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.pmw.tinylog.Logger;
import server.webapi.WebAPI;

/**
 * deploy a new WebAPI.
 */
public final class Tetris extends AbstractVerticle {
    @Override
    public void start() {
        Logger.info("Starting server");

        config().getJsonObject("components")
                .forEach(entry -> {
                    final JsonObject json = (JsonObject) entry.getValue();
                    final String optionsStr = "options";
                    if (json.containsKey(optionsStr)) {
                        final JsonObject options = ((JsonObject) entry.getValue()).getJsonObject(optionsStr);
                        vertx.deployVerticle(entry.getKey(), new DeploymentOptions(options));
                    } else {
                        vertx.deployVerticle(entry.getKey());
                    }
                });

        Logger.info("Succesfully started server: localhost:" + Config.WEB_PORT + Config.STATIC_FILE_URL);
    }

    public static void main(final String... args) {
        //Logger.warn("Starting server");

        final Vertx vertx = Vertx.vertx();
        //final EventBus eb = vertx.eventBus();
        vertx.deployVerticle(new WebAPI());
    }
}
