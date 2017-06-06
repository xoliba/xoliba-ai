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


    /**
     * What is the situation on board, who is winning?
     *
     * @return value that represents the situation: the smaller (negative) is better for blue and bigger (positive) is better for red
     */
    public int evaluate() {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((i == 0 || i == 6) && (j == 0 || j == 6))
                    continue;
                sum += board[i][j];
            }
        }
        //System.out.println("AI: evaluateBoard:\n" + board + "sum: " + sum);
        return sum;
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
                hash = 33 * hash + (i+1) * board[i][j] * 7 + (j+1) * 13;
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        String b = "board " + this.hashCode() + "\n" +
                "value: " + this.evaluate() + "\n";
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

    /**
     * Copies the board to a new board object, which should have the same hash but is a different object in the terms of reference.
     * @return a copy of this board object
     */
    public Board copy() {
        int[][] copy = new int[7][7];

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }
        return new Board(copy);
    }
}
