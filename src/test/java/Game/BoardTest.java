package Game;

import Game.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by vili on 5.6.2017.
 */
public class BoardTest {

    Board board1, board2, board3, board4, board5, board6;

    @Before
    public void setUp() {
        board1 = new Board(new int[][]{
                {-2,0,0,0,0,-1,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,1,0,0,0,0},
                {0,1,0,0,0,0,0},
                {-2,0,0,0,0,0,-2},
        });
        board2 = new Board(new int[][]{
                {-2,0,0,0,0,1,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,1,0,0,0,0},
                {0,-1,0,0,0,0,0},
                {-2,0,0,0,0,0,-2},
        });
        board3 = new Board(new int[][]{
                {-2,0,0,0,0,0,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,1,0,0,0,0},
                {0,1,0,0,0,0,0},
                {-2,0,0,0,0,0,-2},
        });
        board4 = new Board(new int[][]{
                {-2,0,0,0,0,-1,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,0,0,1,0,0},
                {0,0,0,0,0,0,0},
                {-2,0,1,0,0,0,-2},
        });
        board5 = new Board(new int[][]{
                {-2,-1,0,0,0,-1,-2},
                {0,0,0,0,0,0,0},
                {0,0,0,-1,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,0,0,1,0,0},
                {0,0,0,0,0,0,0},
                {-2,0,1,0,0,-1,-2},
        });
        board6 = new Board(new int[][]{
                {-2,-1,0,0,0,-1,-2},
                {0,0,0,0,0,0,0},
                {0,0,-1,-1,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,0,0,1,0,0},
                {0,0,0,0,-1,0,0},
                {-2,0,1,0,0,0,-2},
        });
    }

    @Test
    public void equalsTest() {
        assertTrue(board1.equals(board1));
        assertFalse(board1.equals(board2));
        assertTrue(new Board().equals(new Board()));

        board2 = board1.copy();
        assertTrue(board1.equals(board2));
        board1.board[0][1] = 1;
        assertFalse("these should not be equal\n" + board1 + "\n" + board2,board1.equals(board2));
    }

    @Test
    public void copyTest() {
        assertTrue("board copy should have a different object reference",
                board1.copy() != board1);
        assertTrue("board copy should equal with the original board",
                board1.equals(board1.copy()));
        assertTrue("copied boards table should have a different reference",
                board1.board != board1.copy().board);
    }

    @Test public void evaluateTest() {
        assertEquals("symmetric board should be equally good", 0, board1.evaluate());
        assertTrue("red has a triangle and blue doesn't, so better for red", board3.evaluate() > 0);
        assertTrue("red has a bigger triangle than blue, so better for red", board4.evaluate() > 0);
        assertTrue("same sized triangles, but blue has potential for a bigger one", board5.evaluate() < 0);
        assertTrue("same sized triangles, but blue has more stones on board", board6.evaluate() < 0);
    }

}
