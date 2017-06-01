package AI;

import Game.Coordinate;

/**
 * Created by vili on 1.6.2017.
 */
public class StoneCollecter {

    private Validator validator;

    public StoneCollecter() {
        validator = new Validator();
    }

    /**
     * Removes all colored stones inside a triangle that can be formed by using stone at coordinate c as a corner.
     * Expects that at least one legal triangle can be formed by using a stone at c;
     *
     * @param board integer table that represents board
     * @param c the coordinate where lies the stone we must use as one corner of the triangle
     */
    public int[][] collectStonesFromAnyTriangleAvailable(int[][] board, Coordinate c) {
        return board;
    }
}
