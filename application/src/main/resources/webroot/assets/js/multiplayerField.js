"use strict";

document.addEventListener("DOMContentLoaded", init);

let block = 32;
let space = 15;

function init() {
    showUserField();
}

function showUserField() {

    let fieldWidth = (16 * block) + (4 * space);
    document.getElementById("userField").style.width = "" + fieldWidth + "px";

    /*let c = document.getElementById("userField");
    let ctx = c.getContext("2d");
    ctx.fillText("Test", 10, 50);*/
}

