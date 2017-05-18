const ROWS = 7;


class Board {

    constructor() {
        this.boardTable = new Array(ROWS);
        this.create2DArray();
        this.generateStartingBoard();
    }

    create2DArray() {
        for (let i = 0; i < ROWS; i++) {
            this.boardTable[i] = new Array(ROWS);
            for (let j = 0; j < ROWS; j++) {
                this.boardTable[i][j] = 0;
            }
        }
    }

    generateStartingBoard() {
        //for now this is enough
        for (let i = 0; i < this.boardTable.length; i++) {
            for (let j = 0; j < this.boardTable.length; j++) {
                this.boardTable[i][j] = Math.floor(Math.random() * (2 + 1) ) -1;;
            }
        }
    }

    get gameBoard() {
        return this.boardTable;
    }



}