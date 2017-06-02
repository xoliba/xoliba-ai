package AI;


import Game.*;

import java.util.ArrayList;



public class AI {

    private Board board;
    private int color;
    private Validator validator;
    private StoneCollector stoneCollector;

    public AI(int color) {
        this.color = color;
        this.validator = new Validator();
        this.stoneCollector = new StoneCollector();
    }
    
    public int[][] move(int[][] b){
        board = new Board(b);
        System.out.println("AI got a new board:\n" + board);
        validator.refreshBoard(board.board);
        doFirstPossibleMove(board);
        System.out.println("AI did a move:\n" + board);
        return board.board;
    }

    private void doFirstPossibleMove(Board board) {
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[0].length; j++) {
                if (board.board[i][j] == color) { //the stone we are looking at is of my color
                    ArrayList<Move> possibleMoves = validator.getPossibleMoves(new Coordinate(i,j));
                    if (possibleMoves.size() > 0) { //there are possible moves
                        Move move = possibleMoves.get(0);
                        swap(board.board, move);
                        this.board = stoneCollector.collectStonesFromAnyTriangleAvailable(board, move);
                        return; //return after the first possible move
                    }
                }
            }
        }
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