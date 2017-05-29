
import java.util.Vector;



public class AI {
    
    private int color;

    public AI(int color) {
        this.color = color;
    }
    
    public int[][] move(int[][] board){
        swap(board, 1, 1, 1, 2);
        return board;
    }
    
    public void swap(int[][] board, int x1, int y1, int x2, int y2){
        int helpValue = board[x1][y1];
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = helpValue;
    }
}
