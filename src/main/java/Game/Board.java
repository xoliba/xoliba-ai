package Game;

/**
 * Created by vili on 31.5.2017.
 */


public class Board{

    int[][] board;

    public Board() {
        board = new int[7][7];
    }

    public Board(int[][] board) {
        this.board = board;
    }

    @Override
    public String toString() {
        String b = "";
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
