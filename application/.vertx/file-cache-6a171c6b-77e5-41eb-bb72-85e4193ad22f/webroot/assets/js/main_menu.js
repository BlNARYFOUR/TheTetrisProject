"use strict";

let eb = vertx.eventBus;

document.addEventListener("DOMContentLoaded", init);

function init() {
    let myHandler = function(message){
        console.log("I received a message " + message);
    };

    eb.registerHandler("login.events.message", myHandler(), function () {
        console.log("Yippee! The handler info has been propagated across the cluster")
    });

    document.getElementById("logOut").addEventListener("click", logOut);
    document.getElementById("chooseGamemode").addEventListener("click", chooseGamemode);
    document.getElementById("clan").addEventListener("click", clan);
    document.getElementById("shop").addEventListener("click", shop);
    document.getElementById("highScore").addEventListener("click", highScore);
    document.getElementById("buyCubes").addEventListener("click", buyCubes);

}

function logOut() {
    location.href="../index.html";
}

function chooseGamemode() {
    location.href = "chooseGamemode.html";
}

function clan() {
    alert("Will be available in the future!");
}

function shop() {
    alert("Will be available in the future!");
}

function highScore() {
    alert("Will be available in the future!");
}

function buyCubes() {
    alert("Will be available in the future! \n" +
        "(At this moment you can not spend money, we think about your wallet)");
}