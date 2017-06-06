package Game;

import java.security.cert.TrustAnchor;

/**
 * Created by vili on 6.6.2017.
 */
public class TurnData {

    public String type;     //for the JSON parser
    public boolean didMove;
    public int[][] board;   //7x7
    public int[] start;     //[x,y]
    public int[] target;    //[x,y]
    public int[][] corners; //[[x,y],[x,y]]

    public TurnData(boolean didMove, Board board, Move move, Triangle triangle) {
        this.type = "TurnData";
        this.didMove = didMove;
        this.board = board.board;
        this.start = new int[]{move.start.x, move.start.y};
        this.target = new int[]{move.target.x, move.target.y};

        corners = new int[2][2];
        int i = 0;
        for (Coordinate c:triangle.getCorners()) {
            if (c.equals(move.target)) { //we know that the target is part of the triangle
                continue;
            }
            corners[i] = new int[]{c.x, c.y};
            i++;
        }
    }

    /**
     * empty constructor for not doing a move
     */
    public TurnData() {
        type = "TurnData";
        didMove = false;
    }
}
