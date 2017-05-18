

function start() {
    var renderer = new PIXI.CanvasRenderer(800, 600);
    renderer.backgroundColor = 0xffffff;
//Add the canvas to the HTML document
    document.body.appendChild(renderer.view);

//Create a container object called the `stage`
    var stage = new PIXI.Container();

    var gameBoard = new Board();
    drawTable(stage, gameBoard.gameBoard)

    renderer.render(stage);

}