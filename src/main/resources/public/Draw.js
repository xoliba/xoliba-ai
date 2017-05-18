

function drawTable(stage, t) {
    for (var i = 0; i < t.length; i++) {
        var row = t[i];
        for (var j = 0; j < row.length; j++) {
            var color = row[j]
            drawButton(stage, j, i, color)
        }
    }
}

function drawButton(stage, x, y, color) {
    var size = scale();
    var xpx = size / 10;
    var ypx = size / 10;
    var radius = xpx / 4;
    var graphics = new PIXI.Graphics();
    if (color == 1) {
        graphics.beginFill(0xe74c3c);
    } else if (color == 0) {
        graphics.beginFill(0xffffff);
    } else {
        graphics.beginFill(0x0000ff);
    }
    graphics.drawCircle(xpx + x * xpx, ypx + y * ypx, radius);
    graphics.endFill();
    stage.addChild(graphics);
}

function scale() {
    var x = window.innerWidth;
    return x;
}
