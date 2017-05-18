

function start() {
    var size = scale()
    var renderer = PIXI.autoDetectRenderer(size, size, {view: document.getElementById("gameboard")});
    renderer.backgroundColor = 0xE5E3DF;
//Add the canvas to the HTML document

//Create a container object called the `stage`
    var stage = new PIXI.Container();

    var gameBoard = new Board();
    drawTable(stage, gameBoard);

    renderer.render(stage);

}
