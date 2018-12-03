"use strict";

document.addEventListener("DOMContentLoaded", init);

let players = [{name:"Bryan", score:0}, {name:"Brend", score:0}, {name:"Dennis", score:0}, {name:"Reinbert", score:100}];

function init() {
    loadPlayers();
}

function loadPlayers() {
    let list = "";
    for (let i = 0; i < players.length; i++) {
        list += "<li><span class='playersName'>" + players[i].name + "</span><span class='playersScore'>" + players[i].score + "</li>";
    }

    document.getElementById("listOfPlayers").innerHTML = list;

    chat();

}

function chat() {
    let chatinput = document.getElementById("chat-input");
    let chatoutput = document.getElementById("chat-output");
    let textstyle = document.querySelector(".text");
    let chatarray = [];

    let date = new Date();
    let day = "["+date.getDate()+"."+date.getMonth()+"."+date.getFullYear()+"]:";
    let time = "["+date.getHours()+":"+date.getMinutes()+"]:";

    chatinput.addEventListener("keyup", function(event) {

        event.preventDefault();

        if (event.keyCode === 13) {
            send();
        }

    });

    function send() {

        let chattext;
        chattext = time+" "+chatinput.value;
        chatarray.push(chattext);
        let newchatarray = chatarray.join("\n");

        chatoutput.innerText = newchatarray;
        chatinput.value = "";

        // Style
        textstyle.style.backgroundColor = "#9584FF";
        textstyle.style.color = "#fff";
        textstyle.style.padding = "10px 20px";
        textstyle.style.borderRadius = "5px";

    }

}