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

    public AI(int color) {
        this.color = color;
        this.validator = new Validator();
        this.stoneCollector = new StoneCollector();
        this.random = new Random();
    }
    
    public int[][] move(int[][] b){
        board = new Board(b);
        System.out.println("AI got a new board:\n" + board);
        validator.refreshBoard(board.board);

        ArrayList<Move> allPossibleMoves = generateAllPossibleMoves(board);

        int possibleMoves = allPossibleMoves.size();
        if (possibleMoves > 0) {
            int i = random.nextInt(allPossibleMoves.size());
            Move m = allPossibleMoves.get(i);
            swap(board.board, m);
            stoneCollector.collectStonesFromBiggestTriangleAvailable(board, m);
            System.out.println("AI did a move:\n" + board);
        } else {
            System.out.println("AI didn't do a move, there is none!");
        }

        return board.board;
    }

    private ArrayList<Move> generateAllPossibleMoves(Board board) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[0].length; j++) {
                if (board.board[i][j] == color) { //the stone we are looking at is of my color
                    moves.addAll(validator.getPossibleMoves(new Coordinate(i,j)));
                }
            }
        }
        return moves;
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
        printInfoFromMove(move, helpValue);
    }

    private void printInfoFromMove(Move move, int color) {
        System.out.println("\tAI: swap coordinates " + move.start + " and " + move.target);
        System.out.println("\tAI: how many triangles formed with the move " + validator.howManyTrianglesFound(move, color));
    }

}