package server.webapi;

import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.pmw.tinylog.Logger;

/**
 * SockJSHandler for the Tetris Project for
 * 'tetris-16.socket.<>'
 */
class TetrisSockJSHandler {
    private final SockJSHandler sockJSHandler;

    TetrisSockJSHandler(final Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
    }

    @SuppressWarnings("SameParameterValue")
    protected SockJSHandler create(final String permittedAddress) {
        addBridgeOptions(permittedAddress);

        Logger.info("Listening to socket");
        return sockJSHandler;
    }

    private void addBridgeOptions(final String permittedAddress) {
        // enkel regels op dit adres toestaan van front end naar back end
        final PermittedOptions inbound = new PermittedOptions().setAddressRegex(permittedAddress);

        // gelijke adresregels voor van en naar front end
        //noinspection UnnecessaryLocalVariable
        final PermittedOptions outbound = inbound;

        final BridgeOptions options = new BridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);
        sockJSHandler.bridge(options);

    }
}
