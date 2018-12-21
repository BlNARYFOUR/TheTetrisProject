"use strict";

let eb = new EventBus("http://localhost:8016/tetris/events");

eb.onopen = function () {
    console.log("Connection open");

    let obj = new Object();
    obj.requestMB = true;
    let json = JSON.stringify(obj);

    eb.send("tetris.events.mysteryBoxRequest", json);

    eb.registerHandler("tetris.events.mysteryBoxWon", function (err, message) {
        let json = message;

        let body = JSON.parse(json.body);
        console.log(body);
        let won = JSON.parse(body.won);
        let priceMB = won.price;

        console.log(priceMB);

        mysteryBoxOpening(priceMB);
    });
};

function mysteryBoxOpening(price) {
    console.log("Open the mystery box");

    document.querySelector(".box").classList.remove("hiddenBox");
    document.querySelector(".box").classList.add("showBox");


    const box = document.querySelector(".box"),
        priceLocation = document.querySelector(".priceWon");

    let boxOpened = false;

    console.log(price);

    box.addEventListener('click', () => {
        if (!boxOpened) {

            /* Open box */
            boxOpened = true;
            box.classList.add('box-open');

            priceLocation.innerHTML = "<img class='price' src='/static/assets/media/daily_streaks/" + price + ".png'>";
            box.style.setProperty('--boxInnerText', `'You won'`);

            let obj = new Object();
            obj.won = price;
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
