package tests;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * Test.
 */
@RunWith(VertxUnitRunner.class)
public class MessageHandlerTest {

    private static final String NEW_MESSAGE = "new message";
    private static final String MESSAGE = "message";
    private static final String RANDOM = "random";
    private static final String NAME = "name";
    private Vertx vertx;
    private EventBus eb;

    @Before
    public void setUp(final TestContext context) throws IOException {
        vertx = Vertx.vertx();
        vertx.deployVerticle("messagehandler.js", context.asyncAssertSuccess());
        eb = vertx.eventBus();
    }

    private void testMessage(final Object message, final TestContext context) {
        if (!(message instanceof JsonObject)) {
            context.fail();
        }
        //noinspection ConstantConditions
        final JsonObject json = (JsonObject) message;
        context.assertEquals(json.getString(NAME), RANDOM);
        context.assertEquals(json.getString(MESSAGE), NEW_MESSAGE);
    }

    @Test
    public void testSendingMessage(final TestContext context) {
        /*
         * Er wordt in 2 callback-functies (lambda's) getest of er een bericht aankomt:
         * 1. op het kanaal "chatty.events.main" (komt het bericht aan)
         * 2. in de reply functie van de oorspronkelijk verstuurder (is er acknowledgement van het versturen)
         * We kunnen hier geen gebruik maken van async.complete(). We initialiseren de async context met een count
         * van 2. Dat betekent dat er 2x een countDown nodig is alvorens de async context complete is.
         */
        final Async async = context.async(2);
        eb.consumer("login.events.main", message -> {
            testMessage(message.body(), context);
            async.countDown();
        });
        eb.send("login.events.message",
            new JsonObject().put(NAME, RANDOM).put(MESSAGE, NEW_MESSAGE),
            reply -> {
                context.assertEquals("OK", reply.result().body());
                async.countDown();
            });
        async.await(2000);
    }

    @After
    public void tearDown(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }
}
