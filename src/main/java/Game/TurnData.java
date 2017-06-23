package Game;


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
	public int color;
	public boolean surrender;//are giving up?
    public int difficulty;  //how much steroids do we give to our AI?
    public int withoutHit;  //how many rounds we have done without hitting a stone?

    public TurnData(boolean startRound, boolean surrender) {
        if (startRound) {
            type = "startRound";
        } else {
            type = "turnData";
        }
        this.surrender = surrender;
    }

    public TurnData(boolean startRound, boolean surrender, int color) {
        this(startRound, surrender);
        this.color = color;
    }

    public TurnData(boolean didMove, Board board, Move move, Triangle triangle, int color) {
        this(didMove, board, move, triangle, color, false);
    }

    public TurnData(boolean didMove, Board board, Move move, Triangle triangle, int color, boolean surrender) {
        this(didMove, board, move, triangle, color, surrender, 2, 0);
    }

    public TurnData(boolean didMove, Board board, Move move, Triangle triangle, int color, boolean surrender, int difficulty, int withoutHit) {
        this.type = "TurnData";
        this.didMove = didMove;
        this.board = board.board;
        this.start = new int[]{move.start.x, move.start.y};
        this.target = new int[]{move.target.x, move.target.y};
		this.color = color;
		this.surrender = surrender;
		this.difficulty = difficulty;
		this.withoutHit = withoutHit;

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
     * constructor for not doing a move
     * @param board the situation before (and after) move
     */
    public TurnData(int[][] board) {
        type = "TurnData";
        didMove = false;
        surrender = false;
        this.board = board;
    }

    @Override
    public String toString() {
        String s = "" + type;
        s += didMove ? " is a move:\n" : " did not move\n";
        if (!didMove) return s;

        s += "surrender: " + surrender + "\n";
        Board b = new Board(board);
        Coordinate c1 = new Coordinate(start[0], start[1]);
        Coordinate c2 = new Coordinate(target[0], target[1]);
        s += b;
        s += "triangles on board (both colors): " + b.getHowManyTrianglesOnBoard() + "\n";
        s += "start " + c1 + "; target " + c2 + ";\n";
        s += new Triangle(c2, new Coordinate(corners[0][0], corners[0][1]), new Coordinate(corners[1][0], corners[1][1])) + "\n";
        s += "turns without hitting a stone " + withoutHit + "\n";
        return s;
    }
}
