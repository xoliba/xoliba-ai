package AI;


import java.util.ArrayList;
import java.util.Vector;



public class AI {
    
    private int color;
    private Validator validator;

    public AI(int color) {
        this.color = color;
        this.validator = new Validator();
    }
    
    public int[][] move(int[][] board){
        validator.updateBoard(board);
        doFirstPossibleMove(board);

        return board;
    }

    private void doFirstPossibleMove(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == color) { //the stone we are looking at is of my color
                    ArrayList<Coordinate> possibleMoves = validator.getPossibleMoves(i,j);
                    if (possibleMoves.size() > 0) { //there are possible moves
                        swap(board, new Coordinate(i,j), possibleMoves.get(0));
                        return; //return after the first possible move
                    }
                }
            }
        }
    }

    public void swap(int[][] board, Coordinate c1, Coordinate c2){
        int helpValue = board[c1.x][c1.y];
        board[c1.x][c1.y] = board[c2.x][c2.y];
        board[c2.x][c2.y] = helpValue;
    }
}
