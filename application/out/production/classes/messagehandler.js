var eb = vertx.eventBus();

eb.consumer("tetris.events.register.server", function(message) {
    console.log(message.body());
    message.reply("OK");
    eb.publish("tetris.events.register.client", message.body());
});