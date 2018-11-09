"use strict";

let pricesM = ["skin", "100_cubes", "100_xp", "avatar", "nothing"];
let priceM;


function mysteryBox(e) {
    e.preventDefault();

    closeDailyStreaks(e);
    document.getElementById("mysteryBoxCard").classList.remove("hiddenDailyRewards");
    document.getElementById("mysteryBoxCard").classList.add("showDailyRewards");

    let location = document.getElementById("pricesMysteryBox");
    let imgList = "";

    imgList += "<p>This can be in it</p>";

    for (let i = 0; i < pricesM.length; i++){
        imgList += "<figure id='" + pricesM[i] + "'>" +
            "<img data-pricesM='" + pricesM[i] + "' src='../assets/media/daily_streaks/" + pricesM[i].substring(pricesM[i].indexOf("_") + 1) + ".png' class='pricesM' />" +
            "<figcaption>" + pricesM[i].replace("_", " ") + "</figcaption>" +
            "</figure>";
    }

    location.innerHTML = imgList;

    let locationMysteryBOx = document.getElementById("mysteryBox");
    locationMysteryBOx.innerHTML = "<img data-mysteryBox='0' src='../assets/media/daily_streaks/retroBlocks.png' class='mysteryBox'/>" +
        "<input type='submit' value='Open' id='openMysteryBox' class='openMysteryBox-submit'>";

    document.getElementById("openMysteryBox").addEventListener("click", openMysteryBox);
}

function openMysteryBox(e) {
    e.preventDefault();
    //TODO kansberekening Backend

    priceM = pricesM[Math.floor(Math.random()*pricesM.length)];
    console.log("Price: " + priceM);

    alert("Congratulations you will receive " + priceM + " :)");

    document.getElementById("mysteryBoxCard").classList.remove("showDailyRewards");
    document.getElementById("mysteryBoxCard").classList.add("hiddenDailyRewards");


}
