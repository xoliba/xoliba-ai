package AI;

import Game.*;

/**
 * Created by vili on 1.6.2017.
 */
public class StoneCollector {

    private Validator validator;

    public StoneCollector() {
        validator = new Validator();
    }

    /**
     * Removes all colored stones inside a triangle that can be formed by using stone at coordinate c as a corner.
     * Expects that at least one legal triangle can be formed by using a stone at c;
     *
     * @param board integer table that represents board
     * @param m the coordinate where lies the stone we must use as one corner of the triangle
     */
    public Board collectStonesFromAnyTriangleAvailable(Board board, Move m) {

        return board;
    }
}
