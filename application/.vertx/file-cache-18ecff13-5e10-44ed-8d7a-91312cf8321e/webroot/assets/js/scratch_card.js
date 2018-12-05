"use strict";

let pricesS = [];
let boxes = 3;
let boxesOpen = [];
let priceS;
let priceInScratch = [];
let prices;
let scAmount = [];

//TODO amount verder afwerken

function scratchCard(message){
    let json = message;
    let body = JSON.parse(json.body);
    console.log(body);
    prices = JSON.parse(body.prices);
    showScratchCard();
}


function showScratchCard() {

    closeDailyStreaks();
    document.getElementById("scratchCard").classList.remove("hiddenDailyRewards");
    document.getElementById("scratchCard").classList.add("showDailyRewards");

    pricesS.length = 0;

    let location = document.getElementById("pricesScratch");
    let imgList = "";

    imgList += "<p>This can be in it</p>";
    console.log("halloa");

    for (let j = 0; j < prices.length; j++){
        pricesS.push(prices[j].price);
        scAmount.push(prices[j].amount);
        console.log(amount);


        // TODO TIJDELIJLK
        if (pricesS[j] === "skin"){
            imgList += "<figure id='" + pricesS[j] + "'>" +
                "<img data-pricesS='" + pricesS[j] + "' src='../assets/media/daily_streaks/retroBlocks.png' class='pricesS' />" +
                "</figure>";
        } else {

            imgList += "<figure id='" + pricesS[j] + "'>" +
                "<img data-pricesS='" + pricesS[j] + "' src='../assets/media/daily_streaks/" + pricesS[j] + ".png' class='pricesS' />" +
                "</figure>";
        }

    }
    location.innerHTML = imgList;


    let locationScratch = document.getElementById("scratch");
    let boxesList = "";

    for (let k = 0; k < boxes; k++){
        priceS = pricesS[Math.floor(Math.random()*pricesS.length)];
        console.log("in box " + priceS);
        priceInScratch.push(priceS);
        boxesList += '<div class="container" id="js-container' + k + '">\n' +
            '<canvas class="canvas' + k + '" id="js-canvas' + k + '"></canvas>\n' +
            '<div class="form">\n' +
            '<div id="text_price_' + k + '" class="visible">\n' +
            '<img src="../assets/media/daily_streaks/' +  priceS + '.png">\n' +
            '</div>\n' +
            '</div>\n' +
            '</div>';
    }
    locationScratch.innerHTML = boxesList;

    drawScratch();
}

function drawScratch() {
    let isDrawing, lastPoint;
    let container    = document.getElementById('js-container'),
        canvas0      = document.getElementById('js-canvas0'),
        canvasWidth0 = canvas0.width,
        canvasHeight0= canvas0.height,
        ctx0          = canvas0.getContext('2d'),

        canvas1      = document.getElementById('js-canvas1'),
        canvasWidth1 = canvas1.width,
        canvasHeight1= canvas1.height,
        ctx1         = canvas1.getContext('2d'),

        canvas2      = document.getElementById('js-canvas2'),
        canvasWidth2 = canvas2.width,
        canvasHeight2= canvas2.height,
        ctx2         = canvas2.getContext('2d'),

        image        = new Image(),
        image2       = new Image(),
        image3       = new Image(),
        brush        = new Image();

    image.src = "../assets/media/daily_streaks/scratchFields.png";
    image2.src = "../assets/media/daily_streaks/scratchFields.png";
    image3.src = "../assets/media/daily_streaks/scratchFields.png";

    loadImage(canvas0, image, ctx0);
    loadImage(canvas1, image2, ctx1);
    loadImage(canvas2, image3, ctx2);

    brush.src = 'data:image/png;base64,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';

    mouseAndTouchActions(canvas0, ctx0, canvasWidth0, canvasHeight0, lastPoint, isDrawing, brush);
    mouseAndTouchActions(canvas1, ctx1, canvasWidth1, canvasHeight1, lastPoint, isDrawing, brush);
    mouseAndTouchActions(canvas2, ctx2, canvasWidth2, canvasHeight2, lastPoint, isDrawing, brush);

}

function loadImage(canvas, image, ctx) {
    image.onload = function() {
        // ctx.drawImage(image, 10, 10);
        ctx.drawImage(image, 0, 0);
        // Show the form when Image is loaded.
    };
}

function mouseAndTouchActions(canvas, ctx, width, height, lastPoint, isDrawing, brush) {
    canvas.addEventListener('mousedown', handleMouseDown, false);
    canvas.addEventListener('touchstart', handleMouseDown, false);
    canvas.addEventListener('mousemove', handleMouseMove, false);
    canvas.addEventListener('touchmove', handleMouseMove, false);
    canvas.addEventListener('mouseup', handleMouseUp, false);
    canvas.addEventListener('touchend', handleMouseUp, false);

    function distanceBetween(point1, point2) {
        return Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2));
    }

    function angleBetween(point1, point2) {
        return Math.atan2( point2.x - point1.x, point2.y - point1.y );
    }

    // Only test every `stride` pixel. `stride`x faster,
    // but might lead to inaccuracy
    function getFilledInPixels(stride) {
        if (!stride || stride < 1) { stride = 1; }

        let pixels   = ctx.getImageData(0, 0, width, height),
            pdata    = pixels.data,
            l        = pdata.length,
            total    = (l / stride),
            count    = 0;

        // Iterate over all pixels
        for(let i = count = 0; i < l; i += stride) {
            if (parseInt(pdata[i]) === 0) {
                count++;
            }
        }

        return Math.round((count / total) * 100);
    }

    function getMouse(e, canvas) {
        let offsetX = 0, offsetY = 0, mx, my;

        if (canvas.offsetParent !== undefined) {
            do {
                offsetX += canvas.offsetLeft;
                offsetY += canvas.offsetTop;
            } while ((canvas = canvas.offsetParent));
        }

        mx = (e.pageX || e.touches[0].clientX) - offsetX;
        my = (e.pageY || e.touches[0].clientY) - offsetY;

        return {x: mx, y: my};
    }

    function handlePercentage(filledInPixels) {
        filledInPixels = filledInPixels || 0;
        //console.log(filledInPixels + '%');
        if (filledInPixels > 30) {
            canvas.parentNode.removeChild(canvas);
            boxesOpen.push(canvas);
            controleIfYouWon();
        }
    }

    function handleMouseDown(e) {
        isDrawing = true;
        lastPoint = getMouse(e, canvas);
    }

    function handleMouseMove(e) {
        if (!isDrawing) { return; }

        e.preventDefault();

        let currentPoint = getMouse(e, canvas),
            dist = distanceBetween(lastPoint, currentPoint),
            angle = angleBetween(lastPoint, currentPoint),
            x, y;

        for (let i = 0; i < dist; i++) {
            x = lastPoint.x + (Math.sin(angle) * i) - 25;
            y = lastPoint.y + (Math.cos(angle) * i) - 25;
            ctx.globalCompositeOperation = 'destination-out';
            ctx.drawImage(brush, x, y);
        }

        lastPoint = currentPoint;
        handlePercentage(getFilledInPixels(32));
    }

    function handleMouseUp(e) {
        isDrawing = false;
    }
}

function controleIfYouWon() {
    let same = [];
    let notSame = [];

    same.length = 0;
    notSame.length = 0;

    if (boxesOpen.length === boxes) {
        for (let i = 0; i < priceInScratch.length; i++){
            console.log("0 " + priceInScratch[0]);
            if (priceInScratch[0] === priceInScratch[i]){
                same.push(priceInScratch[i]);
            }else {
                notSame.push(priceInScratch[i]);
            }
        }
        console.log("same " + same);
        let price = new Object();

        if (same.length === 3){

            alert("You won " + same[0]);

            price.won = same[0];
            price.alreadyLoggedInToday = true;
            let json = JSON.stringify(price);

            eb.send("tetris.events.priceScratchCard", json);


        } else {
            alert("Better luck next time!");
            price.lose = "Better luck next time";
            price.alreadyLoggedInToday = true;
            let json = JSON.stringify(price);

            eb.send("tetris.events.priceScratchCard", json);

            document.getElementById("scratchCard").classList.remove("showDailyRewards");
            document.getElementById("scratchCard").classList.add("hiddenDailyRewards");

            for (let h = 0; h < boxes; h++){
                document.getElementById("text_price_" + h).classList.remove("visible");
                document.getElementById("text_price_" + h).classList.add("invisible");
            }

        }
    }
}