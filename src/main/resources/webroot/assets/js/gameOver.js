"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("exitGame").addEventListener("click", exitGame);
}

function exitGame() {
    location.href = "main_menu.html";
}
