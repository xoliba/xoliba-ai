package Game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eerop, gilip
 */
public class MoveTest {
    
    Move move;

    @Before
    public void setUp() {
        move = new Move(new Coordinate(), new Coordinate());
    }

    @Test
    public void getBiggestTriangleTest() {
        assertEquals(null, move.getBiggestTriangle()); //no triangles at all
        move.addPossibleTriangle(new Triangle(new Coordinate(0,1), new Coordinate(1,0), new Coordinate(1,2)));
        assertEquals(1, move.getBiggestTriangle().getSize()); //one small tri
        move.addPossibleTriangle(new Triangle(new Coordinate(1,4), new Coordinate(1,0), new Coordinate(3,2)));
        assertEquals(2, move.getBiggestTriangle().getSize()); //one small one med
        move.addPossibleTriangle(new Triangle(new Coordinate(0,1), new Coordinate(1,0), new Coordinate(2,1)));
        assertEquals(2, move.getBiggestTriangle().getSize()); //two smalls one med
        move.addPossibleTriangle(new Triangle(new Coordinate(1,0), new Coordinate(1,6), new Coordinate(6,6)));
        assertEquals(3, move.getBiggestTriangle().getSize()); //there is one big
    }
}
