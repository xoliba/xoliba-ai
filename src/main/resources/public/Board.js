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
        
        let reds = 17;
        let blues = 17;
        let whites = 11;
        
        for (let i = 0; i < this.boardTable.length; i++) {
            for (let j = 0; j < this.boardTable.length; j++) {                
                let sum = reds + blues + whites;
                let value = Math.floor(Math.random() * sum + 1);
                if((i == 0 || i == 6) && (j == 0 || j == 6)){
                    this.boardTable[i][j] = -2;
                } else if(value <= reds){
                    this.boardTable[i][j] = 1;
                    reds--;
                } else if (value <= reds + blues){
                    this.boardTable[i][j] = -1;
                    blues--;
                } else {
                    this.boardTable[i][j] = 0;
                    whites--;
                }
            }
        }
    }

    get gameBoard() {
        return this.boardTable;
    }

}