package Game;

import Game.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by vili on 5.6.2017.
 */
public class BoardTest {

    Board board1, board2;

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

}
