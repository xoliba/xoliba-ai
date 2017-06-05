package Game;

/**
 * Created by vili on 31.5.2017.
 */


public class Board{

    public int[][] board;

    public Board() {
        board = new int[7][7];
    }

    public Board(int[][] board) {
        this.board = board;
    }


    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass()){
            return false;
        } else if (this.hashCode() == o.hashCode()){
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        int hash = 17;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                hash = 97 * hash + i * board[i][j] * 7 + j * 13;
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        String b = "board " + this.hashCode() + "\n";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                b += "[";
                int c = board[j][i];
                if (c >= 0) b += " "; //makes the table even with some values < 0
                if (c == 0) b += " "; //ads a space if the value is zero
                else b += c; //otherwise adds the number
                b += "]";
            }
            b += "\n";
        }
        return b;
    }
}
