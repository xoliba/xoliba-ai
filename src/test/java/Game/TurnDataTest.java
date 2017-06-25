package Game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by vili on 25.6.2017.
 */
public class TurnDataTest {

    private Board b;
    private TurnData td;

    @Before
    public void setup() {
        b = new Board(new int[][]{
                {-2,0,0,0,0,-1,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,0,0,1,0,0},
                {0,0,0,0,0,0,0},
                {-2,0,1,0,0,0,-2},
        });
        td = new TurnData(true, b.copy(), 5);
    }

    @Test
    public void equalsTest() {
        assertTrue("hash code shouldn't be 0, but it was",td.hashCode() != 0);
        assertTrue(td.hashCode() == td.hashCode() && td.equals(td));
        TurnData td1 = new TurnData(true, b.copy(), 5);
        assertTrue(td1.equals(td));
        td1 = new TurnData(false, b.copy(), 5);
        assertFalse(td1.equals(td));
        td1 = new TurnData(true, b.copy(), 0);
        assertFalse(td1.equals(td));
        td1 = new TurnData(true, b.copy(), new Move(new Coordinate(0, 1), new Coordinate(1, 0)), new Triangle(new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(2,1)), 1, false, 2, 0);
        td = new TurnData(true, b.copy(), new Move(new Coordinate(0, 1), new Coordinate(1, 0)), new Triangle(new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(2,1)), 1, false, 2, 0);
        assertTrue(td1.equals(td));
        Board b1 = b.copy();
        b1.board[4][0]=-1;
        System.out.println(b + " " + b1);
        td = new TurnData(true, b1.copy(), new Move(new Coordinate(0, 1), new Coordinate(1, 0)), new Triangle(new Coordinate(1, 0), new Coordinate(0, 1), new Coordinate(2,1)), 1, false, 2, 0);
        assertFalse("td1\n" + td1 + "td\n" + td, td1.equals(td));
    }
}
