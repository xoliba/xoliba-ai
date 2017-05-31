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
            b += "[";
            for (int j = 0; j < board[0].length; j++) {
                b += "[" + board[j][i] + "]";
            }
            b += "]\n";
        }
        return b;
    }
}
