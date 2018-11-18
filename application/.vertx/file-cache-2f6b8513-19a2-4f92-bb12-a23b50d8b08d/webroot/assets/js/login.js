"use strict";

document.addEventListener("DOMContentLoaded", init);

function init(e) {
    e.preventDefault();

    document.getElementById('error').innerText = getCookie("info");
}

function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');

    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            let val = c.substring(name.length, c.length);
            val.replace("+", " ");

            return val;
        }
    }

    return "";
}