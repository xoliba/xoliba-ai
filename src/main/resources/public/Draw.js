

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


    //Do you believe in magic?

    /*graphics.beginFill(0x000000);

    for (i = 1; i < 6; i++) {
        graphics.drawRect(padding + i * rectSideLength, padding, lineWidth, size - 2 * padding);
		graphics.drawRect(padding, padding + i * rectSideLength, size - 2 * padding, lineWidth);
    }

    graphics.drawRect(size - padding, rectSideLength + padding, lineWidth, 5 * rectSideLength);
	graphics.drawRect(rectSideLength + padding, size - padding, 5 * rectSideLength, lineWidth);
	graphics.drawRect(rectSideLength + padding, padding, 5 * rectSideLength, lineWidth);
	graphics.drawRect(padding, rectSideLength + padding, lineWidth, 5 * rectSideLength);

    graphics.endFill();

	graphics.lineStyle(lineWidth, 0x000000).moveTo(padding, rectSideLength + padding).lineTo(rectSideLength + padding, padding);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength + padding, padding).lineTo(size - padding, rectSideLength + padding);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(padding, 6 * rectSideLength + padding).lineTo(rectSideLength + padding, size - padding);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength + padding, size - padding).lineTo(size - padding, 6 * rectSideLength + padding);
	

    var i = 6;

	for (j = 1; j <= 5; j++) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(padding, j * rectSideLength + padding).lineTo(i * rectSideLength + padding, size - padding);
		i--;
	}

	i = 1;

	for (j = 6; j >= 2; j--) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(i * rectSideLength + padding, padding).lineTo(size - padding, j * rectSideLength + padding);
		i++;
	}

	i = 2;

	for (j = 2; j <= 6; j++) {
		graphics.lineStyle(0.8 *lineWidth, 0x00000).moveTo(padding, j * rectSideLength + padding).lineTo(i * rectSideLength + padding, padding);
		i++;
	}

	i = 1;

	for (j = 1; j <= 5; j++) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(i * rectSideLength + padding, size - padding).lineTo(size - padding, j * rectSideLength + padding);
		i++;
	}

	graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(rectSideLength + padding, rectSideLength + padding).lineTo(6 * rectSideLength + padding, 6 * rectSideLength + padding);
	graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(rectSideLength + padding, 6 * rectSideLength + padding).lineTo(6 * rectSideLength + padding, rectSideLength + padding);
    */
}
