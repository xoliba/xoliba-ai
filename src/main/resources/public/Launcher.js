

function start() {
    var size = scale()
    var app = new PIXI.Application(size, size, {view: document.getElementById("gameboard")});
    app.renderer.backgroundColor = 0xE5E3DF;
//Add the canvas to the HTML document

//Create a container object called the `stage`
//    var stage = new PIXI.Container();

    var gameBoard = new Board();

    drawTable(app.stage, gameBoard);

    var stonesArray = gameBoard.boardTable;

    var padding = size / 10;
    var px = size / 7.5;
    var radius = px / 4;

      PIXI.loader
      .add([
          "images/whiteCircle64.png",
          "images/blueCircle64.png",
          "images/redCircle64.png"
      ])
      .load(setup);

      function setup() {
        for (var i = 0; i < 7; i++) {
          for (var j = 0; j < 7; j++) {
            if(!((i == 0 || i == 6) && (j == 0 || j == 6))){

              var sprite;

              if ()

              sprite = new PIXI.Sprite(
                PIXI.loader.resources["images/whiteCircle64.png"].texture
              );
              sprite.interactive = true;
              sprite.buttonMode = true;
              sprite.x = padding + i * px - radius;
              sprite.y = padding + j * px - radius;
              sprite.width = radius * 2;
              sprite.height = radius * 2;

              sprite.on('click', onClick)

              function onClick(){
                this.x += 20;
                console.log('testi')
              }

              app.stage.addChild(sprite);

            }
          }
        }
      }



      app.renderer.render(app.stage);
}
