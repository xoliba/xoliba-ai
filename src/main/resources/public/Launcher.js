

function start() {
    var size = scale()
    var renderer = new PIXI.CanvasRenderer(2048, 2048);
    renderer.backgroundColor = 0xE5E3DF;
//Add the canvas to the HTML document
    document.body.appendChild(renderer.view);

//Create a container object called the `stage`
    var stage = new PIXI.Container();

    var gameBoard = new Board();
    drawTable(stage, gameBoard.gameBoard);

    renderer.render(stage);

}
