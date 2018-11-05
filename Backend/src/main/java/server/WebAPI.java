package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class WebAPI extends AbstractVerticle {
    @Override
    public void start(){
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            final HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("Hello user");
            response.end();
        });

        server.requestHandler(router::accept).listen(8081);

        router.route("/login/*").handler(StaticHandler.create());

        router.route("/login/events/*").handler(new TetrisSockJSHandler(vertx).create());
    }
}
