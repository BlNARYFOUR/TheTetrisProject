package server.webapi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import server.webapi.TetrisSockJSHandler;

public class WebAPI extends AbstractVerticle {
    @Override
    public void start(){
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Routes routes = new Routes();

        router.route("/").handler(routes::rootHandler);
        router.route("/static/*").handler(StaticHandler.create());
        router.route("/tetris/events/*").handler(new TetrisSockJSHandler(vertx).create());

        server.requestHandler(router::accept).listen(8081);
    }
}
