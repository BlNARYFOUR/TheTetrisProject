package server.webapi;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

class TetrisSockJSHandler {
    private final SockJSHandler sockJSHandler;

    TetrisSockJSHandler(final Vertx vertx){
        sockJSHandler = SockJSHandler.create(vertx);
    }

    private void addBridgeOptions(){
        final PermittedOptions inbound = new PermittedOptions()
                .setAddressRegex("tetris-16\\.socket\\..+");
        //noinspection UnnecessaryLocalVariable
        final PermittedOptions outbound = inbound;
        final BridgeOptions options = new BridgeOptions()
                .addInboundPermitted(inbound)
                .addOutboundPermitted(outbound);
        sockJSHandler.bridge(options);
    }

    SockJSHandler create(){
        addBridgeOptions();
        return sockJSHandler;
    }
}
