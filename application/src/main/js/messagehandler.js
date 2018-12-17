var eb = vertx.eventBus();

eb.consumer("login.events.message", function(message) {
    console.log(message.body());
    message.reply("OK");
    eb.publish("login.events.main", message.body());
});

eb.consumer("")