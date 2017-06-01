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
        return board.board;
    }

    private void doFirstPossibleMove(Board board) {
        for (int i = 0; i < board.board.length; i++) {
            for (int j = 0; j < board.board[0].length; j++) {
                if (board.board[i][j] == color) { //the stone we are looking at is of my color
                    ArrayList<Coordinate> possibleMoves = validator.getPossibleMoves(new Coordinate(i,j));
                    if (possibleMoves.size() > 0) { //there are possible moves
                        Coordinate target = possibleMoves.get(0);
                        swap(board.board, new Coordinate(i,j), target);
                        board = stoneCollector.collectStonesFromAnyTriangleAvailable(board, target);
                        return; //return after the first possible move
                    }
                }
            }
        }
    }

    public void swap(int[][] board, Coordinate start, Coordinate end){
        int helpValue = board[start.x][start.y];
        board[start.x][start.y] = board[end.x][end.y];
        board[end.x][end.y] = helpValue;
        printInfoFromMove(start, end, helpValue);
    }

    private void printInfoFromMove(Coordinate startingPosition, Coordinate endingPosition, int color) {
        System.out.println("\tAI: swap coordinates " + startingPosition + " and " + endingPosition);
        System.out.println("\tAI: how many triangles formed with the move " + validator.howManyTrianglesFound(endingPosition, color));
    }

}