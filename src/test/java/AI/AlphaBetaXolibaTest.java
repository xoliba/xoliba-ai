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
    private int[][] table, table1, table2, table3, table4;

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
        table2  = new int[][]{
                {-2, 0, 0, 0, 0, 0,-2},
                { 0, 0, 0, 0, 0, 0,-1},
                { 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0,-1, 0, 0},
                { 0, 1, 0, 0, 0, 0, 0},
                { 1, 0, 0, 0, 0, 0,-1},
                {-2, 1, 0, 0, 0,-1,-2},
        };
        table3  = new int[][]{
                {-2, 0, 0, 0, 0, 0,-2},
                { 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0},
                { 0, 1, 0, 0, 0, 0,-1},
                { 0, 0, 0, 0, 0,-1, 0},
                { 1, 0, 0, 0, 0, 0,-1},
                {-2, 1, 0, 0, 0,-1,-2},
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
        TurnData td = spiedABX.doTheBestMoveForColor(b.copy(), 0, 1); //with threshold 0 we should not do anything
        System.out.println("\tRed did a move with threshold 0:\n" + td + "\n");
        assertFalse(td.didMove);

        spiedABX = spy(new AlphaBetaXoliba());
        td = spiedABX.doTheBestMoveForColor(b.copy(), 1, 1); //with threshold 1 we should only look for our moves and take the best one
        System.out.println("\tRed did a move with threshold 1:\n" + td);
        verify(spiedABX, atLeastOnce()).minValue(any(TurnData.class), anyInt(), anyDouble(), anyDouble(), anyInt());
        verify(spiedABX, never()).maxValue(any(TurnData.class), anyInt(), anyDouble(), anyDouble(), anyInt());
        assertTrue(td.didMove);
        assertTrue("do the big triangle!",td.board[2][6] == 1);

        spiedABX = spy(new AlphaBetaXoliba());
        td = spiedABX.doTheBestMoveForColor(b.copy(), 2, 1); //with threshold 2 we should consider what the opponent will do after our move
        System.out.println("\tRed did a move with threshold 2:\n" + td);
        verify(spiedABX, atLeastOnce()).maxValue(any(TurnData.class), anyInt(), anyDouble(), anyDouble(), anyInt());
        verify(spiedABX, atLeastOnce()).maxValue(any(TurnData.class), anyInt(), anyDouble(), anyDouble(), anyInt());
        assertTrue("do the middle sized triangle, and block opponent from hitting you!", td.board[3][1] == 1);

        spiedABX = spy(new AlphaBetaXoliba());
        td = spiedABX.doTheBestMoveForColor(b.copy(), 3, 1); //with threshold 3 we should consider what situation we could get after opponents turn
        System.out.println("\tRed did a move with threshold 3:\n" + td);
        assertTrue("do the middle sized triangle, and get an opportunity to hit more points from opponent", td.board[3][5] == 1);
    }

    @Test
    public void minMaxAndGameEnding() {
        Board b = new Board(table2);
        Board b1 = b.copy();
        b1.swap(new Move(new Coordinate(3, 4), new Coordinate(4,5)));
        Board b2 = b1.copy();
        b2.swap(new Move(new Coordinate(5, 0), new Coordinate(5,2)));
        Board b3 = b2.copy();
        b3.swap(new Move(new Coordinate(4, 5), new Coordinate(5,4)));
        System.out.println("\tTesting with board b:\n" + b + "\nand b1:\n" + b1 + "\nand b2\n" + b2 + "\nand b3\n" + b3);

        AlphaBetaXoliba spiedABX = spy(new AlphaBetaXoliba());
        spiedABX.minValue(new TurnData(true, b.copy(), 28), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        //we well continue calling until the 30 move limit
        verify(spiedABX, atLeastOnce()).maxValue(eq(new TurnData(true, b1.copy(), 29)), anyInt(), anyDouble(), anyDouble(), eq(0));
        verify(spiedABX, never()).minValue(eq(new TurnData(true, b2.copy(), 29)), anyInt(), anyDouble(), anyDouble(), eq(0));
        verify(spiedABX, atLeastOnce()).minValue(eq(new TurnData(true, b2.copy(), 30)), anyInt(), anyDouble(), anyDouble(), eq(0));
        verify(spiedABX, never()).maxValue(eq(new TurnData(true, b2.copy(), 30)), anyInt(), anyDouble(), anyDouble(), eq(0));

        //after we reach 30 moves without hitting we shouldn't go further
        spiedABX = spy(new AlphaBetaXoliba());
        spiedABX.minValue(new TurnData(true, b2.copy(), 30), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        verify(spiedABX, never()).maxValue(any(TurnData.class), anyInt(), anyDouble(), anyDouble(), eq(0));

        //after 29 moves without hitting we should want to make the final, winning move!
        System.out.println("ask for min value without hit 29 board\n" + b2);
        b.swap(new Move(new Coordinate(5,0), new Coordinate(5, 2)));
        spiedABX = spy(new AlphaBetaXoliba());
        double minVal1 = spiedABX.minValue(new TurnData(true, b2.copy(), 29), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        verify(spiedABX, times(1)).maxValue(eq(new TurnData(true, b.copy(), 30)), anyInt(), anyDouble(), anyDouble(), eq(0));
        verify(spiedABX, times(1)).maxValue(eq(new TurnData(true, b3.copy(), 30)), anyInt(), anyDouble(), anyDouble(), eq(0));
        double maxVal1 = spiedABX.maxValue(new TurnData(true, b.copy(), 30), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        double maxVal2 = spiedABX.maxValue(new TurnData(true, b3.copy(), 30), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        assertTrue("min value should always choose the medium triangle move, because game is going to end on the next move",minVal1 == maxVal1);
        assertTrue("the move with game ending should always be better for blue when the biggest triangle is bigger", maxVal1 < maxVal2);
    }

}
