package AI;


import java.util.ArrayList;



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
