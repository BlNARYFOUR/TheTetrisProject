"use strict";

let pricesM;
let pricesMB = [];
let mbAmount = [];
let mbAvatar;
let mbSkin;

function mysteryBox(message) {
    let json = message;
    console.log(json);
    let body = JSON.parse(json.body);
    console.log(body);
    pricesM = JSON.parse(body.prices);
    mbAvatar = JSON.parse(body.avatar);
    mbSkin = JSON.parse(body.skin);

    showMysteryBox();

}

function showMysteryBox() {

    closeDailyStreaks();
    document.getElementById("mysteryBoxCard").classList.remove("hiddenDailyRewards");
    document.getElementById("mysteryBoxCard").classList.add("showDailyRewards");

    pricesMB.length = 0;

    let location = document.getElementById("pricesMysteryBox");
    let imgList = "";

    imgList += "<p>This can be in it</p>";

    let src;

    for (let i = 0; i < pricesM.length; i++){

        switch (pricesM[i].price){
            case "skin":
                pricesMB.push(mbSkin);
                mbAmount.push(pricesM[i].amount);
                src = "/static/assets/media/skin/skin_";
                break;
            case "avatar":
                pricesMB.push(mbAvatar);
                mbAmount.push(pricesM[i].amount);
                src = "/static/assets/media/avatars/Avatar_";
                break;
            default:
                pricesMB.push(pricesM[i].price);
                mbAmount.push(pricesM[i].amount);
                src = "/static/assets/media/daily_streaks/";
        }

        imgList += "<figure id='" + pricesMB[i] + "'>" +
            "<img data-pricesM='" + pricesMB[i] + "' src='" + src + pricesMB[i] + ".png' class='pricesM' />" +
            "<figcaption>" + pricesMB[i] + "</figcaption>" +
            "</figure>";
    }

    location.innerHTML = imgList;

    let locationMysteryBOx = document.getElementById("mysteryBox");
    locationMysteryBOx.innerHTML = "<img data-showMysteryBox='0' src='/static/assets/media/daily_streaks/mystery_box.png' class='showMysteryBox'/>" +
        "<input type='submit' value='Open' id='openMysteryBox' class='openMysteryBox-submit'>";

    document.getElementById("openMysteryBox").addEventListener("click", openMysteryBox);
}

function openMysteryBox(e) {
    e.preventDefault();

    location.href = "/static/pages/mystery_box.html";
}

