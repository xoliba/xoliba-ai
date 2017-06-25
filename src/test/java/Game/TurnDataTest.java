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
    }
}
