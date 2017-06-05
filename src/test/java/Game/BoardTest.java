package Game;

import Game.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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
    }

}
