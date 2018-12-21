"use strict";

let eb = new EventBus("/tetris-16/socket");

eb.onopen = function () {
    console.log("Connection open");

    let obj = new Object();
    obj.requestMB = true;
    let json = JSON.stringify(obj);

    eb.send("tetris.events.mysteryBoxRequest", json);

    eb.registerHandler("tetris.events.mysteryBoxWon", function (err, message) {
        let json = message;

        let body = JSON.parse(json.body);
        let won = JSON.parse(body.won);
        let priceMB = won.price;
        let amount = won.amount;

        mysteryBoxOpening(priceMB, amount);
    });
};

function mysteryBoxOpening(price, amount) {
    console.log("Open the mystery box");

    document.querySelector(".box").classList.remove("hiddenBox");
    document.querySelector(".box").classList.add("showBox");


    const box = document.querySelector(".box"),
        priceLocation = document.querySelector(".priceWon");

    let boxOpened = false;


    box.addEventListener('click', () => {
        if (!boxOpened) {

            /* Open box */
            boxOpened = true;
            box.classList.add('box-open');

            priceLocation.innerHTML = "<img class='price' src='/static/assets/media/daily_streaks/" + price + ".png'>";
            box.style.setProperty('--boxInnerText', `'You won'`);

            let obj = new Object();
            obj.won = price;
            obj.amount = amount;
            obj.alreadyLoggedInToday = true;

            let json = JSON.stringify(obj);

            eb.send("tetris.events.receivedMBReward", json);

            setTimeout(goBackToDailyStreakMenu, 5000);
        }
    });
}

function goBackToDailyStreakMenu() {

    eb.onclose = function () {
        console.log("Connection closed");
    };

    window.location.href = "/static/pages/main_menu.html";
}
