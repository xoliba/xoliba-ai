const ROWS = 7;


class Board {

    constructor() {
        this.boardTable = new Array(ROWS);
        this.create2DArray();
    }

    create2DArray() {
        for (let i = 0; i < ROWS; i++) {
            this.boardTable[i] = new Array(ROWS);
            for (let j = 0; j < ROWS; j++) {
                this.boardTable[i][j] = 0;
            }
        }
    }

    get gameBoard() {
        return this.boardTable;
    }

}