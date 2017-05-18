

function drawTable(stage, t) {
    for (var i = 0; i < t.length; i++) {
        let row = t[i];
        for (var j = 0; j < row.length; j++) {
            let color = row[j]
            drawButton(stage, j, i, color)
        }
    }
}

function drawButton(stage, x, y, color) {
    size = scale();
    xpx = size / 10;
    ypx = size / 10;
    radius = xpx / 4;
    let graphics = new PIXI.Graphics();
    if (color == 1) {
        graphics.beginFill(0xe74c3c);
    } else if (color == 0) {
        graphics.beginFill(0xffffff);
        graphics.lineStyle(1, 0x000000);
    } else {
        graphics.beginFill(0x0000ff);
    }
    graphics.drawCircle(xpx + x * xpx, ypx + y * ypx, radius);
    graphics.endFill();
    stage.addChild(graphics);
}

function scale() {
    var x = window.innerWidth;
    var y = window.innerHeight;
    
    var result = Math.min(x, y);
    return result;
}
