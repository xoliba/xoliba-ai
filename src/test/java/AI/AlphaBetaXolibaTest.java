package AI;

import Game.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by vili on 14.6.2017.
 */
public class AlphaBetaXolibaTest {

    private AlphaBetaXoliba abx;
    private int[][] table, table1;

    @Before
    public void setUp() {
        abx = new AlphaBetaXoliba();
        table  = new int[][]{
                {-2,-1, 0, 1,-1, 0,-2},
                {-1, 0, 0, 0, 0, 0,-1},
                { 1, 0, 0, 0, 0, 0, 0},
                {-1, 1, 0, 0, 0, 1, 0},
                { 0, 0, 0,-1, 0, 0, 0},
                {-1, 0, 0, 1, 0, 0,-1},
                {-2, 0, 0, 0, 0, 0,-2},
        };
        table1  = new int[][]{
                {-2, 0, 0, 0, 0, 0,-2},
                {-1, 0, 0, 0, 0, 0, 0},
                { 1, 0, 0, 0, 0, 0, 0},
                {-1, 1, 0, 0, 0, 1, 0},
                { 0, 0, 0,-1, 0, 0, 0},
                {-1, 0, 0, 1, 0, 0,-1},
                {-2, 0, 0, 0, 0, 0,-2},
        };

    }

    @Test
    public void bestMoveForColorTest() {
        Board b = new Board(table);
        System.out.println("\tTesting with board:\n" + b);

        assertTrue(abx.doTheBestMoveForColor(b, 1, 0) == null);

        TurnData td = abx.doTheBestMoveForColor(b.copy(), 1, 1);
        System.out.println(td);
        assertTrue(td != null);
        assertTrue(td.didMove && td.board != null);
        assertFalse(b + "\nshould not equal\n" + new Board(td.board), b.equals(new Board(td.board)));

        td = abx.doTheBestMoveForColor(b.copy(), 1, -1);
        System.out.println(td);
        assertTrue(td != null);
        assertTrue(td.didMove && td.board != null);
        assertFalse(b + "\nshould not equal\n" + new Board(td.board), b.equals(new Board(td.board)));

    }

    @Test
    public void maxValueTest() {
        Board b = new Board(table1);
        System.out.println("\tTesting with board:\n" + b);

        AlphaBetaXoliba spiedABX = spy(abx);
        TurnData td = spiedABX.doTheBestMoveForColor(b.copy(), 0, 1); //with threshold 0 we should only look for our moves and take the best one
        System.out.println("\tRed did a move:\n" + td);
        verify(spiedABX, atLeastOnce()).minValue(any(Board.class), anyInt(), anyInt(), anyInt());
        verify(spiedABX, never()).maxValue(any(Board.class), anyInt(), anyInt(), anyInt());

        spiedABX = spy(new AlphaBetaXoliba());
        td = spiedABX.doTheBestMoveForColor(b.copy(), 1, 1); //with threshold 1 we should consider what the opponent will do after our move
        System.out.println("\tRed did a move:\n" + td);
        verify(spiedABX, atLeastOnce()).maxValue(any(Board.class), anyInt(), anyInt(), anyInt());
        verify(spiedABX, atLeastOnce()).maxValue(any(Board.class), anyInt(), anyInt(), anyInt());

    }

}
