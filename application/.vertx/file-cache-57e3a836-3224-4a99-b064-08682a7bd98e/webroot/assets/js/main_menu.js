"use strict";

document.addEventListener("DOMContentLoaded", init);

function init() {
    document.getElementById("logOut").addEventListener("click", logOut);

}

function logOut() {
    location.href="startScreen.html";
}