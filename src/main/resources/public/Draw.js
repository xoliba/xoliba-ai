

function drawTable(stage, t) {
    let graphics = new PIXI.Graphics();
    size = scale();

    drawLines(stage, size, graphics)

    /*for (var i = 0; i < t.length; i++) {
        let row = t[i];
        for (var j = 0; j < row.length; j++) {
            let color = row[j]
            drawButton(stage, j, i, color, size, graphics)
        }
    }*/
}

function drawButton(stage, x, y, color, size, graphics) {
    if(color >= -1) {
        //X and Y is anyway the same?
        xpx = size / 10;
        ypx = size / 10;
        radius = xpx / 4;
        if (color == 1) {
            graphics.beginFill(0xe74c3c);
        } else if (color == 0) {
            graphics.beginFill(0xffffff);
            graphics.lineStyle(1, 0x000000);
        } else if (color == -1) {
            graphics.beginFill(0x0000ff);
        }
        graphics.drawCircle(xpx + x * xpx, ypx + y * ypx, radius);
        graphics.endFill();
        stage.addChild(graphics);
    }
}

function scale() {
    var x = window.innerWidth;
    var y = window.innerHeight;
    
    var result = Math.min(x, y);
    return result;
}

function drawLines(stage, size, graphics) {

    var renderer = PIXI.autoDetectRenderer(
        size, size,
        {transparent: true}
    );

    //Dont touch these :)
    var rectSideLength = size / 8;
    var lineWidth = size / 120;
    var padding = rectSizeLength / 2;

    graphics.scale.x = 1;
    graphics.scale.y = 1;

    size -= rectSideLength;

    //Do you believe in magic?

    for (i = 1; i < 7; i++) {
        graphics.drawRect(padding + i * rectSideLength, padding, lineWidth, vres - 2 * padding);
		graphics.drawRect(padding, padding + i * rectSideLength, vres - 2 * padding, lineWidth);
    }

    graphics.drawRect(vres - padding, rectSideLength + padding, lineWidth, 5 * rectSideLength + lineWidth);
	graphics.drawRect(rectSideLength + padding, vres - padding, 5 * rectSideLength + lineWidth, lineWidth);
	graphics.drawRect(rectSideLength + padding, padding, 5 * rectSideLength + lineWidth, lineWidth);
	graphics.drawRect(padding, rectSideLength + padding, lineWidth, 5 * rectSideLength + lineWidth);

	graphics.lineStyle(lineWidth, 0x000000).moveTo(padding, rectSideLength + padding).lineTo(rectSideLength + padding, padding);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength + padding, padding).lineTo(vres - padding + lineWidth, rectSideLength + padding);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(padding, 6 * rectSideLength + padding).lineTo(rectSideLength + padding, vres + lineWidth - padding);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength + lineWidth + padding, vres + lineWidth - padding).lineTo(vres + lineWidth - padding, 6 * rectSideLength + lineWidth + padding);
	

    var i = 6;

	for (j = 1; j <= 5; j++) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(padding, j * rectSideLength + padding).lineTo(i * rectSideLength + padding, vres + lineWidth - padding);
		i--;
	}

	i = 1;

	for (j = 6; j >= 2; j--) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(i * rectSideLength + padding, padding).lineTo(vres + lineWidth - padding, j * rectSideLength + padding);
		i++;
	}

	i = 2;

	for (j = 2; j <= 6; j++) {
		graphics.lineStyle(0.8 *lineWidth, 0x00000).moveTo(padding, j * rectSideLength + lineWidth + padding).lineTo(i * rectSideLength + lineWidth + padding, padding);
		i++;
	}

	i = 1;

	for (j = 1; j <= 5; j++) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(i * rectSideLength + lineWidth + padding, vres + lineWidth - padding).lineTo(vres + lineWidth - padding, j * rectSideLength + lineWidth + padding);
		i++;
	}

	graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(rectSideLength + padding, rectSideLength + padding).lineTo(6 * rectSideLength + lineWidth / 2 + padding, 6 * rectSideLength + lineWidth / 2 + padding);
	graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(rectSideLength + lineWidth / 2 + padding, 6 * rectSideLength + lineWidth / 2 + padding).lineTo(6 * rectSideLength + lineWidth / 2 + padding, rectSideLength + lineWidth / 2 + padding);
}
