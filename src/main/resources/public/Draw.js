var sprites = [];

function drawTable(stage, gameBoard, renderer) {
    let graphics = new PIXI.Graphics();
    size = scale();
    let t = gameBoard.boardTable
    spritesetup(stage, renderer);
    drawLines(stage, size, graphics)
    for (var i = 0; i < t.length; i++) {
        let row = t[i];
        for (var j = 0; j < row.length; j++) {
            let color = row[j]
            //  drawButton(stage, j, i, color, size, graphics, gameBoard, renderer)

        }
    }
    stage.addChild(graphics);
}

function drawButton(stage, x, y, color, size, graphics, gameBoard, renderer) {
    if (color < -1) {
        return
    }

    padding = size / 10
    px = size / 7.5;
    radius = px / 4;
    /*  if (color == 1) {
     
     graphics.beginFill(0xe74c3c); //red
     graphics.lineStyle(1, 0x000000);
     } else if (color == 0) {
     graphics.beginFill(0xffffff); //white
     graphics.lineStyle(1, 0x000000);
     } else if (color == -1) {
     graphics.beginFill(0x0000ff); //blue
     graphics.lineStyle(1, 0x000000);
     }
     
     graphics.drawCircle(padding + x * px, padding + y * px, radius);
     graphics.endFill();
     
     
     
     stage.addChild(buttonSprite);
     */

    stage.addChild(graphics)
}

function spritesetup(stage, renderer) {
    PIXI.loader
            .add("images/whiteCircle64.png")
            .load(setup);

    function setup() {
        for (var i = 0; i < 7; i++) {
            for (var j = 0; j < 7; j++) {
                 if(!((i == 0 || i == 6) && (j == 0 || j == 6))){
                    var sprite = new PIXI.Sprite(
                            PIXI.loader.resources["images/whiteCircle64.png"].texture
                            );
                    padding = size / 10
                    px = size / 7.5;
                    radius = px / 4;
                    sprite.interactive = true;
                    sprite.buttonMode = true;
                    sprite.scale.x *= 0.5;
                    sprite.scale.y *= 0.5
                    sprite.on('click', onClick);
                    sprite.x = padding + i * px - radius;
                    sprite.y = padding + j * px - radius;

                    sprites.push(sprite);

                    stage.addChild(sprite);

                    renderer.render(stage);
                }
            }
        }
    }
}
function onClick() {
    console.log("Click'd");
    // this.sprite.x += 50;
}

function scale() {
    var x = window.innerWidth;
    var y = window.innerHeight;

    var result = Math.min(x, y);
    return result;
}

function drawLines(stage, size, graphics) {
    //Dont touch these :)
    var padding = size / 10;
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
