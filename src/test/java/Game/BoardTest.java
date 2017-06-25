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
                {0,0,0,0,0,0,0},
                {1,0,1,0,0,0,0},
                {-2,1,0,0,0,0,-2},
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
                {0,0,-1,0,-1,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,1,0,1,0,0,0},
                {0,0,1,0,0,0,0},
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
        System.out.println(board1.evaluate());
        assertTrue("symmetric board should be equally good", board1.evaluate() <= 0.0001 && board1.evaluate() >= -0.0001);
        assertTrue("red has a triangle and blue doesn't, so better for red", board3.evaluate() > 0);
        if (board4.getWeights().calculatePointsWeight != 0) { //if we consider the points calculation
            System.out.println(board4 + "board 4 game ended false : true " + board4.evaluate(false) + " " + board4.evaluate(true));
            assertTrue("if the game ended now, the evaluation should be different", board4.evaluate(false) != board4.evaluate(true));
        }
        assertTrue("if game ended now, it should definitely be in reds favor\n" + board4, board4.evaluate(true) > 0);
        assertTrue("red has a bigger triangle than blue, so better for red\n" + board4, board4.evaluate() > 0);

        //these broke when the machine learning parameter development began...
        //evaluating board can be more complex than these test cases

        assertTrue("same sized triangles, but blue has potential for a bigger one", board5.evaluate() < 0);
        assertTrue("same sized triangles, but blue has more stones on board", board6.evaluate() < 0);

    }

    @Test public void calculatePointsTest() {
        Board drawBig = new Board(new int[][]{
                        {-2, 0, 0, 0, 0, 1, -2},
                        {0, 1, 1, 0, 1, 1, 0},
                        {-1, 1, 0, 1, 1, 1, -1},
                        {1, 0, 0, 0, 0, 0, 1},
                        {0, 1, 0, 0, 0, 0, 0},
                        {1, 0, 1, -1, 1, 1, 1},
                        {-2, 1, 1, 1, 1, -1, -2}});
        assertTrue("Draw with big triangles", drawBig.calculatePoints() == 0);
        Board redBoard = new Board(new int[][]{
                        {-2, 0, 0, 0, 0, 1, -2},
                        {0, 1, 1, 0, 1, 1, 0},
                        {0, 1, 0, 1, 1, 1, 0},
                        {1, 0, 0, 0, 0, 0, 1},
                        {0, 1, 0, 0, 0, 0, 0},
                        {1, 0, 1, 0, 1, 1, 1},
                        {-2, 1, 1, 1, 1, 0, -2}});
        assertTrue("Red should win", redBoard.calculatePoints() == 51);
        Board redBoard2 = new Board(new int[][]{
                        {-2, -1, -1, -1, -1, 1, -2},
                        {0, 1, 1, 0, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0},
                        {1, 0, 0, 1, 0, 0, 1},
                        {0, 1, 0, 0, 0, 0, 0},
                        {1, 0, 1, 0, 1, 1, 1},
                        {-2, 1, 1, 0, 1, 0, -2}});
        assertTrue("Red should win", redBoard2.calculatePoints() == 26);
        Board blueBoard = new Board(new int[][]{
                        {-2, -1, -1, -1, -1, 0, -2},
                        {0, 1, 1, 0, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0},
                        {1, 0, 0, 1, -1, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0},
                        {1, 0, 1, 0, 1, 1, 0},
                        {-2, -1, 1, 0, 1, 0, -2}});
        assertTrue("Blue should win", blueBoard.calculatePoints() == -3);
        Board blueBoard2 = new Board(new int[][]{
                {-2,1, -1,-1, 1, 0, -2},
                {0, 1, -1, 1, 0, 1, 0},
                {0, 1, 0,  0, 0, 0, 0},
                {1, 0, 0,  0,-1, 0, 0},
                {0, 1, 0,  0, 0, 0, 0},
                {1, 0, -1, 0, 0, 0, 0},
                {-2, 1, 0, 0, 0, 0, -2}});
        assertTrue("Blue should win", blueBoard2.calculatePoints() == -14);
        Board drawNoTri = new Board(new int[][]{
                        {-2, 0, 1, 0, -1, 0, -2},
                        {1, 1, 1, 0, -1, -1, -1},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 1, 0, -1, -1, -1},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 1, 0, -1, -1, -1},
                        {-2, 0, 1, 0, -1, 0, -2}});
        assertTrue("Draw with no triangles", drawNoTri.calculatePoints() == 0);
        Board drawSmallTri = new Board(new int[][]{
                        {-2, 0, 0, 0, 0, 1, -2},
                        {0, 0, 0, 0, 1, 0, 1},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {-1, 0, -1, 0, 0, 0, 0},
                        {-2, -1, 0, 0, 0, 0, -2}});
        assertTrue("Draw with small triangles", drawSmallTri.calculatePoints() == 0);
    }

    @Test
    public void startingBoardTest() {
        assertTrue(Board.redStartsGame(board6.board));
        assertFalse(Board.redStartsGame(board2.board));
        board1.board[1][0] = 1;
        assertFalse(Board.redStartsGame(board1.board));
    }

    @Test
    public void sameAmountOfStonesOnBoardTest() {
        assertTrue(Board.sameAmountOfStonesOnBoard(board1.board, board1.board));
        assertTrue(Board.sameAmountOfStonesOnBoard(board1.board, board2.board));
        board2.board[0][0] = 0; //the outside-of-the-board corners shouldn't affect the result
        assertTrue(Board.sameAmountOfStonesOnBoard(board1.board, board2.board));
        assertFalse(Board.sameAmountOfStonesOnBoard(board1.board, board3.board));
        assertFalse(Board.sameAmountOfStonesOnBoard(board5.board, board6.board));
        assertFalse("empty board should have different sum than a not empty one!\n" +
                "empty board how many stones: " + new Board(new int[7][7]).howManyStonesOnBoard() +
                "\nnot empty board how many stones: " + board4.howManyStonesOnBoard(), Board.sameAmountOfStonesOnBoard(new int[7][7], board4.board));
    }

    @Test
    public void howManyStonesOnBoard() {
        assertTrue(board1.howManyStonesOnBoard() == 6);
        assertTrue(new  Board().howManyStonesOnBoard() == 0);
        assertTrue(board6.howManyStonesOnBoard() == 8);
    }
}
