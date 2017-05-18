

function drawTable(stage, t) {
    let graphics = new PIXI.Graphics();
    size = scale();

    drawLines(stage, size, graphics)

    for (var i = 0; i < t.length; i++) {
        let row = t[i];
        for (var j = 0; j < row.length; j++) {
            let color = row[j]
            drawButton(stage, j, i, color, size, graphics)
        }
    }
}

function drawButton(stage, x, y, color, size, graphics) {
    if(color < -1) {
        return
    }
    
    px = size / 8;
    radius = px / 4;
    if (color == 1) {
        graphics.beginFill(0xe74c3c); //red
        graphics.lineStyle(1, 0x000000);
    } else if (color == 0) {
        graphics.beginFill(0xffffff); //white
        graphics.lineStyle(1, 0x000000);
    } else if (color == -1) {
        graphics.beginFill(0x0000ff); //blue
        graphics.lineStyle(1, 0x000000);
    }
    graphics.drawCircle(px + x * px, px + y * px, radius);
    graphics.endFill();
    stage.addChild(graphics);
}

function scale() {
    var x = window.innerWidth;
    var y = window.innerHeight;
    
    var result = Math.min(x, y);
    return result;
}

function drawLines(stage, size, graphics) {
    //Line width.
    var lineWidth = size / 120;

    //Dont touch these :)
    var padding = 200;
    var rectSideLength = (size - 2 * padding) / 6;
    var center = size / 2;
    var lineWidth = size / 120;

    for (i = -2; i <= 2; i++) {
        graphics.lineStyle(lineWidth, 0x000000).moveTo(center + i * rectSideLength, center + -3 * rectSideLength).lineTo(center + i * rectSideLength, center + 3 * rectSideLength);
        graphics.lineStyle(lineWidth, 0x000000).moveTo(center + -3 * rectSideLength, center + i * rectSideLength).lineTo(center + 3 * rectSideLength, center + i * rectSideLength);
        graphics.lineStyle(lineWidth, 0x000000).moveTo(center + i * rectSideLength, center - 3 * rectSideLength).lineTo(center + 3 * rectSideLength, center - i * rectSideLength);
        graphics.lineStyle(lineWidth, 0x000000).moveTo(center - 3 * rectSideLength, center + i * rectSideLength).lineTo(center + i * rectSideLength, center - 3 * rectSideLength);
        graphics.lineStyle(lineWidth, 0x000000).moveTo(center - 3 * rectSideLength, center + i * rectSideLength).lineTo(center - i * rectSideLength, center + 3 * rectSideLength);
        graphics.lineStyle(lineWidth, 0x000000).moveTo(center + i * rectSideLength, center + 3 * rectSideLength).lineTo(center + 3 * rectSideLength, center + i * rectSideLength);

    }

    graphics.lineStyle(lineWidth, 0x000000).moveTo(center + 3 * rectSideLength, center + 2 * rectSideLength).lineTo(center + 3 * rectSideLength, center - 2 * rectSideLength);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(center + 2 * rectSideLength, center + 3 * rectSideLength).lineTo(center - 2 * rectSideLength, center + 3 * rectSideLength);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(center - 3 * rectSideLength, center + 2 * rectSideLength).lineTo(center - 3 * rectSideLength, center - 2 * rectSideLength);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(center + 2 * rectSideLength, center - 3 * rectSideLength).lineTo(center - 2 * rectSideLength, center - 3 * rectSideLength);

    graphics.lineStyle(lineWidth, 0x000000).moveTo(center + 2 * rectSideLength, center + 2 * rectSideLength).lineTo(center - 2 * rectSideLength, center - 2 * rectSideLength);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(center - 2 * rectSideLength, center + 2 * rectSideLength).lineTo(center + 2 * rectSideLength, center - 2 * rectSideLength);
}
