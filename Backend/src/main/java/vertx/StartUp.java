package vertx;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class StartUp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        EventBus eb = vertx.eventBus();
        vertx.deployVerticle(new WebAPI());
        System.out.println("Starting server");
    }
}
