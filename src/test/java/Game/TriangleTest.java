package Game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by vili on 1.6.2017.
 */
public class TriangleTest {

    private Triangle t;

    @Before
    public void setUp() {
        t = new Triangle(new Coordinate(0,1), new Coordinate(1,0), new Coordinate(1,2));
    }

    @Test
    public void triangleKnowsItsSizeTest() {
        assertEquals(1, t.getSize());
        t = new Triangle(new Coordinate(0,1), new Coordinate(2,2), new Coordinate(0,5));
        assertEquals(2, t.getSize());
        t = new Triangle(new Coordinate(1,0), new Coordinate(6,3), new Coordinate(1,6));
        assertEquals(3, t.getSize());
        t = new Triangle(new Coordinate(0,5), new Coordinate(6,5), new Coordinate(3,2));
        assertEquals(3, t.getSize());
    }

}
