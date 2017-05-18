const ROWS = 7;

var boardTable = new Array(ROWS);

class Board {

    constructor() {
        this.boardTable;
        this.create2DArray();
    }

    create2DArray() {
        for (let i = 0; i < ROWS; i++) {
            boardTable[i] = new Array(ROWS);
            for (let j = 0; j < ROWS; j++) {
                boardTable[i][j] = 0;
            }
        }
    }

    get gameBoard() {
        return boardTable;
    }

}