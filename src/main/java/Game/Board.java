package Game;

import AI.ParametersAI;
import AI.Validator;

import java.util.Random;

/**
 * Created by vili on 31.5.2017.
 */


public class Board{

    public int[][] board;
    private Validator validator;  //for evaluating
    private int sizeOfRedsBiggestTriangle = 0;
    private int sizeOfBluesBiggestTriangle = 0;
    private int howManyTrianglesOnBoard = 0;
    private boolean hasBeenEvaluated = false; //for efficiency, possible BUG if the board is changed
    private double value = 0;
    private ParametersAI weights;
    private boolean trianglesHaveBeenLookedFor = false; //for efficiency, possible BUG if the board is changed

    public Board() {
        this(new int[7][7]);
    }

    public Board(int[][] board) {
        this(board, new ParametersAI());
    }

    public Board(int[][] board, ParametersAI parameters) {
        this.board = board;
        this.validator = new Validator();
        this.weights = parameters;
    }

    public static boolean redStartsGame(int[][] board) {
        int sTurn = board[0][1] + board[0][5] + board[1][0] + board[1][6] + board[5][0] + board[5][6] + board[6][1] + board[6][5];

        if (sTurn == 0) {
            for (int i = 1; i < 6; i++) {
                sTurn += board[i][0];
                sTurn += board[i][6];
                sTurn += board[0][i];
                sTurn += board[6][i];
            }
        }

        if (sTurn == 0) {
            Random random = new Random();
            return random.nextBoolean();
        } else {
            return sTurn < 0;
        }
    }

    /**
     * Calculate game points if game ended now. Will return negative if blue wins, and positive if red wins.
     * The amount will tell how much points were awarded.
     */
    public int calculatePoints() {
        if (!trianglesHaveBeenLookedFor)
            findAllTriangles();
        if(sizeOfBluesBiggestTriangle == sizeOfRedsBiggestTriangle) return 0;

        int reds = 0;
        int blues = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == -1) blues++;
                else if(board[i][j] == 1) reds++;
            }
        }
        if(sizeOfBluesBiggestTriangle > sizeOfRedsBiggestTriangle)
            return -sizeOfBluesBiggestTriangle * (17-reds);
        else return sizeOfRedsBiggestTriangle * (17-blues);
    }

    public double evaluate() {
        return evaluate(false);
    }

    /**
     * What is the situation on board, who is winning?
     *
     * @return value that represents the situation: the smaller (negative) is better for blue and bigger (positive) is better for red
     */
    public double evaluate(boolean gameEnded) {
        if (hasBeenEvaluated)
            return value;

        value = 0;

        //Sometimes there is a chance AI favors situation where it rather have 1 stone at corner than 2 stones in the middle.
        //Sometimes this is good: you cant eat corner stone. Bu maybe we should implement some sort of better algorithm
        //when calculating how much value does the ending board give.
        value = sumOfTheStones();
        value += lookForBasis();

        findAllTriangles();
        value += weights.triangleWeight * (sizeOfRedsBiggestTriangle - sizeOfBluesBiggestTriangle);
        if (gameEnded)
            value += weights.calculatePointsWeight * calculatePoints();
        hasBeenEvaluated = true;

        return value;
    }

    public static double evaluate(int[][] b, boolean gameEnded) {
        return new Board(b).evaluate(gameEnded);
    }

    private double sumOfTheStones() {
        return sumOfTheStones(false);
    }

    /**
     * corner, edge and 'normal' stones have different weights that affect the sum
     * @param plain if we ignore the weights and take just a plain sum
     * @return the values of stones on the board added together.
     */
    public double sumOfTheStones(boolean plain) {
        double sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((i == 0 || i == 6) && (j == 0 || j == 6))
                    continue;
                if ((i == 0 || i == 6 || j == 0 || j == 6) && !plain) {
                    if(i == 1 || i == 5 || j == 1 || j == 5) sum += board[i][j] * weights.cornerWeight;  //Its a corner!
                    else sum += board[i][j] * weights.edgeWeight;    //Its at edge!
                } else sum += board[i][j];      //Its just normal stone :(
            }
        }
        return sum;
    }

    /**
     * stones, that are in the corners of the board forming a basis for a triangle give a notable advantage to that color
     * @return the sum of found basis of both colors
     */
    private double lookForBasis() {
        double sum = 0;
        for (int i = 1; i < board.length - 1; i++) { //we dont look at the outside-the-board-corners
            sum += countTheValueOfThisPair(new Coordinate(i, 0)); //the up-down edges
            sum += countTheValueOfThisPair(new Coordinate(0, i)); //the left-right edges
        }
        sum += lookForMediumBasisOnEdges();
        return sum;
    }

    private double lookForMediumBasisOnEdges() {
        double sum = 0;
        Coordinate[][] pairs = new Coordinate[][]{
                {new Coordinate(0,1), new Coordinate(0,5)}, //west
                {new Coordinate(6,1), new Coordinate(6, 5)}, //east
                {new Coordinate(1,0), new Coordinate(5, 0)}, //north
                {new Coordinate(1,6), new Coordinate(5, 6)} //south
        };
        for (int i = 0; i < pairs.length; i++) {
            Coordinate c1 = pairs[i][0];
            Coordinate c2 = pairs[i][1];

            int color = board[c1.x][c1.y];
            if (color != 0 && color == board[c2.x][c2.y]) {
                sum += weights.basisEdgeMediumWeight * color;
            }
        }
        return sum;
    }

    /**
     *
     * @param c a coordinate on the edge of the board
     * @return the value of the pair formed with c and the coordinate on the other side of the board
     */
    private double countTheValueOfThisPair(Coordinate c) {
        int color = board[c.x][c.y];
        if (color == 0)
            return 0; //there cannot be a pair
        else if ((c.x == 0 || c.x == 6) &&  color == board[6 - c.x][c.y]) { //we're on the left or right edge and there is a pair
            return weights.basisBigWeight * color;
        } else if ((c.y == 0 || c.y == 6) && color == board[c.x][6 - c.y]) { //the same on up/down edges
            return weights.basisBigWeight * color;
        }
        return 0; //there is no pair
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
        trianglesHaveBeenLookedFor = true;
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
        return new Board(copy, weights);
    }

    public void swap(Move move){
        int helpValue = board[move.start.x][move.start.y];
        board[move.start.x][move.start.y] = board[move.target.x][move.target.y];
        board[move.target.x][move.target.y] = helpValue;
    }

    public static boolean sameAmountOfStonesOnBoard(int[][] b1, int[][] b2) {
        return new Board(b1).howManyStonesOnBoard() == new Board(b2).howManyStonesOnBoard();
    }

    public int howManyStonesOnBoard() {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if ((i == 0 || i == 6) && (j == 0 || j == 6))
                    continue;
                if (board[i][j] != 0)
                    count++;
            }
        }
    return count;
    }
}
