"use strict";

let eb = new EventBus("/tetris-16/socket/");

let userInfo;
let allAvatars;
let avatarInfo;

eb.onopen = function () {
    console.log("Connection open");

    let obj = new Object();
    obj.request = "changeAvatar";
    let json = JSON.stringify(obj);

    eb.send("tetris.events.changeAvatarRequest", json);

    eb.registerHandler("tetris.events.allAvatars", function (err, message) {
        let json = message;
        console.log(json);

        let body = JSON.parse(json.body);
        allAvatars = JSON.parse(body.avatars);
        console.log(allAvatars);
        userInfo = JSON.parse(body.user);
        console.log(userInfo);
        avatarInfo = JSON.parse(body.avatar);
        console.log(avatarInfo);

        setUserInfoInPage();
        showAllImagesOfASpecificPage();
    });


};

eb.onclose = function () {
    console.log("Connection Closed");
};

function setUserInfoInPage() {
    document.querySelector(".userName").innerText = userInfo.username;

    document.getElementById("changeAvatar").innerHTML =
        "<img src='../assets/media/avatars/Avatar_" + avatarInfo.name + ".png' class='user-avatar' alt='avatar' title='avatar'>";

    UserInClan();

    //generateLevelAndXP();
}


let elementSelectables;
let selectedItem;
let avatarArr = [];

function showAllImagesOfASpecificPage() {
    console.log("show all avatars");

    document.getElementById("yes").disabled = true;
    document.getElementById("no").disabled = true;

    document.getElementById("back").addEventListener("click", goBack);
    document.getElementById("save").addEventListener("click", saveAvatar);

    let location = document.getElementById("showAllAvatars");
    let imgList = "";
    let firstSelected = " selected";

    for (let i = 0; i < allAvatars.length; i++){
        avatarArr.push(allAvatars[i].name);

        console.log("boe")
        imgList += "<figure id='" + avatarArr[i] + "' class='selectable" + firstSelected + "'><img data-avatarname='" + avatarArr[i] + "' src='../assets/media/avatars/Avatar_" + avatarArr[i] + ".png' class='avatars'/></figure>";

        firstSelected = "";
    }
    console.log(avatarArr)

    location.innerHTML = imgList;
    console.log("bye")
    selectablesEvents();
}


function selectablesEvents(){
    elementSelectables = document.getElementsByClassName("selectable");

    for (let i = 0; i < elementSelectables.length; i++) {
        elementSelectables[i].addEventListener("click", changeSelected);
    }
}

function changeSelected(e) {
    e.stopPropagation();
    if(e.target.parentNode.classList.contains("selectable")){
        for (let i = 0; i < elementSelectables.length; i++) {
            elementSelectables[i].classList.remove("selected");
        }
        e.target.parentNode.classList.add("selected");

        // Hier wordt de naam van de geselecteerde avatar ingestoken
        selectedItem = e.target.dataset.avatarname;
        console.log(selectedItem);

    }
    e.target.parentNode.classList.add("selected");

    if (e.target.tagName === "FIGURE"){
        e.target.parentNode.classList.remove("selected");
    }

}

function goBack() {
    location.href="main_menu.html";
}

function saveAvatar() {
    document.getElementById("back").disabled = true;
    document.getElementById("save").disabled = true;
    document.getElementById("yes").disabled = false;
    document.getElementById("no").disabled = false;

    document.getElementById("confirm").classList.remove("hidden");
    document.getElementById("confirm").classList.add("show");

    document.getElementById("yes").addEventListener("click", confirmYes);
    document.getElementById("no").addEventListener("click", confirmNo);
}

function confirmYes(e) {
    e.preventDefault();

    let obj = new Object();
    obj.newAvatar = selectedItem;
    let json = JSON.stringify(obj);

    eb.send("tetris.events.newAvatar", json);

    console.log("Avatar has been changed");

    location.href="main_menu.html";
    //TODO verander de avatar naar de gekozen avatar

}

function confirmNo(e) {
    e.preventDefault();

    document.getElementById("back").disabled = false;
    document.getElementById("save").disabled = false;
    document.getElementById("yes").disabled = true;
    document.getElementById("no").disabled = true;

    document.getElementById("confirm").classList.remove("show");
    document.getElementById("confirm").classList.add("hidden");

    document.getElementById(selectedItem).classList.remove("selected");
    document.getElementById(avatars[0]).classList.add("selected");

    selectedItem = null;

}

function UserInClan() {
    //TODO let claninfo be generate in this function and not in html
    if (!userInfo.hasAClan) {
        document.querySelector(".userInfo-2").style.visibility = "hidden";
    }
}

function generateLevelAndXP() {
    let lvl = 1;
    let xp = userInfo.xp;
    let neededXPForNextLvl = 200;

    if (xp < neededXPForNextLvl){
        lvl = 1;

    } else {
        for (neededXPForNextLvl; xp >= neededXPForNextLvl; neededXPForNextLvl += 200) {
            xp -= neededXPForNextLvl;
            lvl += 1;
        }
    }

    xpBar(xp, neededXPForNextLvl);
    document.querySelector(".level").innerHTML = lvl;
    document.querySelector(".xp-text").innerHTML = xp + " / " + neededXPForNextLvl + " xp";

}

function xpBar(xp, neededXPForNetxtLvl) {
    let bar = (xp / neededXPForNetxtLvl) * 100;
    document.querySelector(".xp_value").style.width = 0 + "%";
    document.querySelector(".xp-value").style.width = bar + "%";
}
