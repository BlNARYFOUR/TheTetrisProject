"use strict";

document.addEventListener("DOMContentLoaded", init);
function init(e) {
    document.getElementById("register").addEventListener("submit", inspectReq);
}

function inspectReq(e) {
    let pass = document.getElementById("password");
    let confirmPass = document.getElementById("confirmPass");

    if(pass.value !== confirmPass.value) {
        e.preventDefault();
        document.getElementById("error").innerText = "Passwords do not match!";
        pass.value = "";
        confirmPass.value = "";
    }
}