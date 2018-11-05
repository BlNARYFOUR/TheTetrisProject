package vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class LoginSockJSHandler {
    private final SockJSHandler sockJSHandler;

    LoginSockJSHandler(final Vertx vertx){
        sockJSHandler = SockJSHandler.create(vertx);
    }

    private void addBridgeOptions(){
        final PermittedOptions inbound = new PermittedOptions()
                .setAddressRegex("login\\.events\\..+");
        final PermittedOptions outbound = new PermittedOptions()
                .setAddressRegex("login\\.events\\..+");
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
