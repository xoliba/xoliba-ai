package AI;

import Game.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;


public class AI {

    private Board board;
    private int color;
    private Validator validator;
    private StoneCollector stoneCollector;
    private Random random;
    private int inceptionTreshold; //how many rounds we go deeper

    public AI(int color) {
        this(color, 3);
    }

    /**
     *
     * @param color which AI plays
     * @param difficulty 0 being the easiest, 3 being still easy to compute
     */
    public AI(int color, int difficulty) {
        this.color = color;
        this.validator = new Validator();
        this.stoneCollector = new StoneCollector();
        this.random = new Random();
        this.inceptionTreshold = difficulty;
    }

    public TurnData move(int[][] b) {
        board = new Board(b);
        System.out.println("AI got a new board:\n" + board);
        validator.refreshBoard(board.board);

        ArrayList<Move> allPossibleMoves = generateAllPossibleMoves(board, color);

        TurnData td = new TurnData();
        int possibleMovesCount = allPossibleMoves.size();
        if (possibleMovesCount > 0) {
            td = doTheBestMoveForRed(board);
            System.out.println("AI did a move:\n" + td);
        } else {
            System.out.println("AI didn't do a move, there is none!");
        }

        return td;
    }

    /**
     * Generate all possible moves for this color
     * @param board
     * @param color for who we generate the moves
     * @return the moves
     */
    private ArrayList<Move> generateAllPossibleMoves(Board board, int color) {
        validator.refreshBoard(board.board);
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[0].length; j++) {
                if (board.board[i][j] == color) { //the stone we are looking at is of the right color
                    moves.addAll(validator.getPossibleMoves(new Coordinate(i, j)));
                }
            }
        }
        return moves;
    }

    /**
     * Decide the best move by using alpha-beta pruning
     * @param board
     * @return all relevant info wrapped in a TurnData object
     */
    private TurnData doTheBestMoveForRed(Board board) {
        TurnData td = new TurnData();
        int redBest = Integer.MIN_VALUE;
        int blueBest = Integer.MAX_VALUE;

        for (Move m: generateAllPossibleMoves(board, 1)) { //for every move
            Board b1 = board.copy(); //we generate a board
            swap(b1.board, m); //where we implement the move
            for (Triangle t:m.triangles) { //and for every triangle with that board
                Board b2 = b1.copy(); //we make a board
                stoneCollector.hitStones(b2,t); //and implement the hitting
                int v = minValue(b2, 0, redBest, blueBest); //the opponent gets a move
                if (v > redBest) { //if the result is better than the known best
                    redBest = v; //update the best
                    td = new TurnData(true, b2.copy(), m, t);
                    System.out.println("AI updated it's planned move (redBest " + redBest + "; blueBest " + blueBest + ")\n" + td);
                }
            }
        }
        return td;
    }

    /**
     * Decide the best move by using alpha-beta pruning
     * @param board
     * @return all relevant info wrapped in a TurnData object
     */
    private TurnData theBestMoveForBlue(Board board) {
        TurnData td = new TurnData();
        int redBest = Integer.MIN_VALUE;
        int blueBest = Integer.MAX_VALUE;

        for (Move m: generateAllPossibleMoves(board, -1)) { //for every move
            Board b1 = board.copy(); //we generate a board
            swap(b1.board, m); //where we implement the move
            for (Triangle t:m.triangles) { //and for every triangle with that board
                Board b2 = b1.copy(); //we make a board
                stoneCollector.hitStones(b2,t); //and implement the hitting
                int v = maxValue(b2, 0, redBest, blueBest); //the opponent gets a move
                if (v < blueBest) { //if the result is better than the known best
                    blueBest = v; //update the best
                    td = new TurnData(true, b2, m, t);
                }
            }
        }
        return td;
    }

    /**
     * estimate how good option this board would be for the red
     *
     * @param board
     * @param inceptionLevel
     * @param redsBest the biggest value predicted atm
     * @param bluesBest the smallest value predicted atm
     * @return the biggest possible (predicted) evaluation outcome from this game situation
     */
    private int maxValue(Board board, int inceptionLevel, int redsBest, int bluesBest) {
        //System.out.println("AI maxValue: inceptionLevel " + inceptionLevel + "; redsBest " + redsBest + "; bluesBest" + bluesBest);
        if (inceptionLevel > inceptionTreshold) {
            return board.evaluate();
        }

        ArrayList<Move> possibleMoves = generateAllPossibleMoves(board, 1);
        if (possibleMoves.isEmpty()) {
            return board.evaluate();
        }

        int v = Integer.MIN_VALUE;
        for (Move m: possibleMoves) { //for all possible moves for red
            Board b1 = board.copy();
            swap(b1.board, m);
            for (Triangle t: m.triangles) { //for all triangles we might form
                Board b = b1.copy(); //lets make a copy of this situation
                stoneCollector.hitStones(b, t); //lets do the move
                v = Math.max(v, minValue(b, inceptionLevel + 1, redsBest, bluesBest)); //lets keep the best possible outcome
                if (v >= bluesBest) { //this is the alpha-beta part: if our new value is better than the so-far-best (from our perspective)
                    return v;         //that opponent could choose, then we do have look no further
                }
                redsBest = Math.max(redsBest, v); //lets update the best value so far for future minValue invitations
            }
        }
        return v;
    }

    /**
     * estimate how good option this board would be for the blue
     *
     * @param board
     * @param inceptionLevel
     * @param redsBest the biggest value predicted atm
     * @param bluesBest the smallest value predicted atm
     * @return the smallest possible (predicted) evaluation outcome from this game situation
     */
    private int minValue(Board board, int inceptionLevel, int redsBest, int bluesBest) {
        if (inceptionLevel > inceptionTreshold) {
            return board.evaluate();
        }

        ArrayList<Move> possibleMoves = generateAllPossibleMoves(board, -1);
        if (possibleMoves.isEmpty()) {
            return board.evaluate();
        }

        int v = Integer.MAX_VALUE;
        for (Move m: possibleMoves) { //for all possible moves for blue
            Board b1 = board.copy();
            swap(b1.board, m);
            for (Triangle t: m.triangles) { //for all triangles we might form
                Board b2 = b1.copy(); //lets make a copy of this situation
                stoneCollector.hitStones(b2, t); //lets do the move
                v = Math.min(v, maxValue(b2, inceptionLevel + 1, redsBest, bluesBest)); //lets keep the best possible outcome

                //this is the alpha-beta part: if our new value is better than the so-far-best (from our perspective)
                if (v <= redsBest) { //that opponent could choose, then we do have look no further
                    return v;
                }
                bluesBest = Math.min(bluesBest, v); //lets update the best value so far for future maxValue invitations
            }
        }
        return v;
    }

    private void scanThroughBoardAndExcecuteThisMethodToOurColoredStones(Method methodToExcecute) {
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[0].length; j++) {
                if (board.board[i][j] == color) { //the stone we are looking at is of my color
                    boolean shouldWeContinue = true;
                    try {
                        shouldWeContinue = (boolean) methodToExcecute.invoke(this, new Coordinate(i,j));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (!shouldWeContinue)
                        return;
                }
            }
        }
    }

    /**
     *
     * @param c coordinate that we should move if we are able
     * @return false if we did a move, true if we did not
     */
    public boolean doFirstPossibleMove(Coordinate c) {
        ArrayList<Move> possibleMoves = validator.getPossibleMoves(c);
        if (possibleMoves.size() > 0) { //there are possible moves
            Move move = possibleMoves.get(0);
            swap(board.board, move);
            this.board = stoneCollector.collectStonesFromAnyTriangleAvailable(board, move);
            return false;
        }
        return true;
    }

    public void swap(int[][] board, Move move){
        int helpValue = board[move.start.x][move.start.y];
        board[move.start.x][move.start.y] = board[move.target.x][move.target.y];
        board[move.target.x][move.target.y] = helpValue;
        //printInfoFromMove(move, helpValue);
    }

    private void printInfoFromMove(Move move, int color) {
        System.out.println("\tAI: swap coordinates " + move.start + " and " + move.target);
        System.out.println("\tAI: how many triangles formed with the move " + validator.howManyTrianglesFound(move, color));
    }

}