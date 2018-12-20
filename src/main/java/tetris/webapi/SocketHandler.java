package tetris.webapi;

import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.pmw.tinylog.Logger;

/**
 * Sockethandler for Tetris application.
 * No changes to be made here
 * @author  JVD
 */
class SocketHandler {

    private final SockJSHandler sockJSHandler;

    SocketHandler(final Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
    }

    SockJSHandler create(String permittedAddress) {
        addBridgeOptions(permittedAddress);

        Logger.info("Listening to socket");
        return sockJSHandler;
    }

    private void addBridgeOptions(String permittedAddress) {
        // JVD: enkel regels op dit adres toestaan van front end naar back end
        final PermittedOptions inbound = new PermittedOptions().setAddressRegex(permittedAddress);

        // JVD: gelijke adresregels voor van en naar front end
        final PermittedOptions outbound = inbound;

        final BridgeOptions options = new BridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);
        sockJSHandler.bridge(options);

    }
}
