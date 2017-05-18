var vres = 1024;

var rectSideLength = vres / 7;
var lineWidth = vres / 120;

var renderer = PIXI.autoDetectRenderer(
	vres, vres,
	{transparent: true}
);

document.body.appendChild(renderer.view);

var stage = new PIXI.Container();

var graphics = new PIXI.Graphics();

graphics.beginFill(0x000000);

drawLines(vres, vres, graphics);

graphics.endFill();

stage.addChild(graphics);

renderer.render(stage);

function drawLines(vres, vres, graphics) {
	vres -= lineWidth;

	for (i = 1; i < 7; i++) {
		//graphics.lineStyle(lineWidth, 0x000000).moveTo(i * rectSideLength, 0).lineTo(i * rectSideLength, vres);
		//graphics.lineStyle(lineWidth, 0x000000).moveTo(0, i * rectSideLength).lineTo(vres, i * rectSideLength);
		graphics.drawRect(i * rectSideLength, 0, lineWidth, vres);
		graphics.drawRect(0, i * rectSideLength, vres, lineWidth);
	}

	graphics.drawRect(vres, rectSideLength, lineWidth, 5 * rectSideLength + lineWidth);
	graphics.drawRect(rectSideLength, vres, 5 * rectSideLength + lineWidth, lineWidth);
	graphics.drawRect(rectSideLength, 0, 5 * rectSideLength + lineWidth, lineWidth);
	graphics.drawRect(0, rectSideLength, lineWidth, 5 * rectSideLength + lineWidth);

	graphics.lineStyle(lineWidth, 0x000000).moveTo(0, rectSideLength).lineTo(rectSideLength, 0);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength, 0).lineTo(vres + lineWidth, rectSideLength);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(0, 6 * rectSideLength).lineTo(rectSideLength, vres + lineWidth);
	graphics.lineStyle(lineWidth, 0x000000).moveTo(6 * rectSideLength + lineWidth, vres + lineWidth).lineTo(vres + lineWidth, 6 * rectSideLength + lineWidth);

	//graphics.lineStyle(lineWidth, 0x000000).moveTo(rectSideLength, rectSideLength).lineTo(6 * rectSideLength, 6 * rectSideLength);
	//graphics.lineStyle(lineWidth, 0x000000).moveTo(0, rectSideLength).lineTo(6 * rectSideLength, vres);
	
	var i = 6;

	for (j = 1; j <= 5; j++) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(0, j * rectSideLength).lineTo(i * rectSideLength, vres + lineWidth);
		i--;
	}

	i = 1;

	for (j = 6; j >= 2; j--) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(i * rectSideLength, 0).lineTo(vres + lineWidth, j * rectSideLength);
		i++;
	}

	i = 2;

	for (j = 2; j <= 6; j++) {
		graphics.lineStyle(0.8 *lineWidth, 0x00000).moveTo(0, j * rectSideLength + lineWidth).lineTo(i * rectSideLength + lineWidth, 0);
		i++;
	}

	i = 1;

	for (j = 1; j <= 5; j++) {
		graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(i * rectSideLength + lineWidth, vres + lineWidth).lineTo(vres + lineWidth, j * rectSideLength + lineWidth);
		i++;
	}

	graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(rectSideLength / 2, rectSideLength / 2).lineTo(6 * rectSideLength + rectSideLength / 2 + lineWidth / 2, 6 * rectSideLength + rectSideLength / 2 + lineWidth / 2);
	graphics.lineStyle(0.8 * lineWidth, 0x000000).moveTo(rectSideLength / 2 + lineWidth / 2, 6 * rectSideLength + rectSideLength / 2 + lineWidth / 2).lineTo(6 * rectSideLength + rectSideLength / 2 + lineWidth / 2, rectSideLength / 2 + lineWidth / 2);
}
