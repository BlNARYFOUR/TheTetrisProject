/*jshint globalstrict: true*/
/* jshint esnext: true */
"use strict";

// [RULE]: Socket url MOET tetris-xx/socket/ zijn (xx is groepsnummer met leading zero) zijn
let eb = new EventBus("/tetris-00/socket/");

document.addEventListener('DOMContentLoaded',init);

function init(){
    console.log('listening to messages');
    eb.onopen = handleServerMessages;

}

function handleServerMessages(){
    console.log("[EVENTBUS]  attempting to contact socket server");

    eb.registerHandler('tetris.events.gameinfo', function(error,message){
        if(error){
            console.log('An error has occurred', error);
        }

        console.log('[EVENTBUS] (Gameinfo) received message from vertx Info');
        document.querySelector('.receivedmessage').innerHTML = JSON.stringify(message.body);
    });

    console.log('[EVENTBUS] (Info) sending message to vertx Info');
 eb.send('tetris.events.info',"Give me game info");
 sendPlayerMessage();
}

function sendPlayerMessage(){
    console.log('[EVENTBUS] (Player) sending message to vertx player consumer');
    eb.send("tetris.events.player",{content: "sending a player action"},function(error,reply){
        console.log('message sent, answer', reply);
    })
}
