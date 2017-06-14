package Game;

import AI.Validator;

/**
 * Created by vili on 31.5.2017.
 */


public class Board{

    public int[][] board;
    private Validator validator;  //for evaluating
    private int sizeOfRedsBiggestTriangle = 0;
    private int sizeOfBluesBiggestTriangle = 0;
    private int howManyTrianglesOnBoard = 0;

    private double triangleValue = 7.0;
    private double edgeValue = 1.5;
    private double cornerValue = 3.0;

    public Board() {
        this(new int[7][7]);
    }

    public Board(int[][] board) {
        this.board = board;
        this.validator = new Validator();
    }

    public void setEdgeDignity(double triangle, double edge, double corner) {
        this.triangleValue = triangle;
        this.edgeValue = edge;
        this.cornerValue = corner;
    }

    /**
     * What is the situation on board, who is winning?
     *
     * @return value that represents the situation: the smaller (negative) is better for blue and bigger (positive) is better for red
     */
    public double evaluate() {
        double e = 0;

        //in any case we cant favor the situation when we have 2 stones or less.
        //Actually we don't even have to calculate them: only thing that matters is biggest triangle.
        //todo does this work properly? How about test scenarios...?
        if(amountOfTheStones(-1) > 2 || amountOfTheStones(1) > 2)
            e = sumOfTheStones();

        findAllTriangles();
        e += triangleValue * (sizeOfRedsBiggestTriangle - sizeOfBluesBiggestTriangle);

        return e;
    }

    private double sumOfTheStones() {
        double sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((i == 0 || i == 6) && (j == 0 || j == 6))
                    continue;
                if (i == 0 || i == 6 || j == 0 || j == 6) {
                    if(i == 1 || i == 5 || j == 1 || j == 5) sum += board[i][j] * cornerValue;
                    else sum += board[i][j] * edgeValue;
                } else sum += board[i][j] * 1;
            }
        }
        return sum;
    }

    private int amountOfTheStones(int color) {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] == color) sum++;
            }
        }
        return sum;
    }

    /**
     * scans through the board updating the sizes of the biggest triangles for both colors
     * in the process
     */
    private void findAllTriangles() {
        validator.refreshBoard(board);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int c = board[i][j];
                if (c != 1 && c != -1) continue;
                //TODO if the size is already 3 no reason to look further

                Move moveOnSpot = new Move(new Coordinate(i,j), new Coordinate(i,j));
                howManyTrianglesOnBoard += validator.howManyTrianglesFound(moveOnSpot, c);

                if (moveOnSpot.triangles.isEmpty()) continue;

                int sizeOfTheT = moveOnSpot.getBiggestTriangle().getSize();
                if (c == 1) {
                    sizeOfRedsBiggestTriangle =  sizeOfTheT > sizeOfRedsBiggestTriangle ? sizeOfTheT : sizeOfRedsBiggestTriangle;
                } else {
                    sizeOfBluesBiggestTriangle =  sizeOfTheT > sizeOfBluesBiggestTriangle ? sizeOfTheT : sizeOfBluesBiggestTriangle;
                }
            }
        }
        //TODO every triangle is found three times, a spot for optimization maybe?
        howManyTrianglesOnBoard /= 3;
    }

    /**
     * must be called after evaluating (otherwise will be 0)
     * @return how many triangles on board, both colors together
     */
    public int getHowManyTrianglesOnBoard() {
        return howManyTrianglesOnBoard;
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

    public void swap(Move move){
        int helpValue = board[move.start.x][move.start.y];
        board[move.start.x][move.start.y] = board[move.target.x][move.target.y];
        board[move.target.x][move.target.y] = helpValue;
    }
}
