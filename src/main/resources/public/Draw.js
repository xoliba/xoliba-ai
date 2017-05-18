

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
        graphics.beginFill(0xe74c3c);
        graphics.lineStyle(1, 0x000000);
    } else if (color == 0) {
        graphics.beginFill(0xffffff);
        graphics.lineStyle(1, 0x000000);
    } else if (color == -1) {
        graphics.beginFill(0x0000ff);
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

    var renderer = PIXI.autoDetectRenderer(
        size, size,
        {transparent: true}
    );

    //Dont touch these :)
    var rectSideLength = size / 7;
    size -= size / 120;

    //Do you believe in magic?
    graphics.beginFill(0x000000);
    for (i = 1; i < 7; i++) {
        graphics.drawRect(i * rectSideLength, 0, lineWidth, size);
        graphics.drawRect(0, i * rectSideLength, size, lineWidth);
    }

    graphics.drawRect(size, rectSideLength, lineWidth, 5 * rectSideLength + lineWidth);
    graphics.drawRect(rectSideLength, size, 5 * rectSideLength + lineWidth, lineWidth);
    graphics.drawRect(rectSideLength, 0, 5 * rectSideLength + lineWidth, lineWidth);
    graphics.drawRect(0, rectSideLength, lineWidth, 5 * rectSideLength + lineWidth);

    graphics.lineStyle(lineWidth, 0x000000).moveTo(0, rectSideLength).lineTo(rectSideLength, 0);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength, 0).lineTo(size + lineWidth, rectSideLength);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(0, 6 * rectSideLength).lineTo(rectSideLength, size + lineWidth);
    graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength + lineWidth, size + lineWidth).lineTo(size + lineWidth, 6 * rectSideLength + lineWidth);

    //graphics.lineStyle(lineWidth, 0x000000).moveTo(rectSideLength, rectSideLength).lineTo(6 * rectSideLength, 6 * rectSideLength);
    //graphics.lineStyle(lineWidth, 0x000000).moveTo(0, rectSideLength).lineTo(6 * rectSideLength, size);

    var i = 6;
    for (j = 1; j <= 5; j++) {
        graphics.lineStyle(lineWidth, 0x000000).moveTo(0, j * rectSideLength).lineTo(i * rectSideLength, size);
        i--;
    }
    i = 1;
    for (j = 6; j >= 1; j--) {
        graphics.lineStyle(lineWidth, 0x000000).moveTo(i * rectSideLength, 0).lineTo(size, j * rectSideLength);
        i++;
    }
    i = 2;
    for (j = 2; j <= 6; j++) {
        graphics.lineStyle(lineWidth, 0x00000).moveTo(0, j * rectSideLength).lineTo(i * rectSideLength, 0);
        i++;
    }
    i = 1;
    for (j = 1; j <= 5; j++) {
        graphics.lineStyle(lineWidth, 0x000000).moveTo(i * rectSideLength, size).lineTo(size, j * rectSideLength);
        i++;
    }

    graphics.endFill();
    stage.addChild(graphics);
    renderer.render(stage);
}
