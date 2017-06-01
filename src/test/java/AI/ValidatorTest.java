package AI;

import java.util.ArrayList;

import Game.Board;
import Game.Coordinate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eerop, filip
 */
public class ValidatorTest {
    
    private Validator validator;
    private int[][] board;
    
    public ValidatorTest() {
        validator = new Validator();
    }

    @Before
    public void setUp() {
        this.board = new int[7][7];
        validator.refreshBoard(board);
    }

    private void fillBoardWithRedsInShapeOfX() {
        board = new int[7][7];
        int x = 1; int y = 0;
        for (int i = 1; i < 7; i++) {
            board[x][y] = 1;
            board[6-x][y] = 1;
            x++;
            y++;
        }
        validator.refreshBoard(board);
    }

    @Test
    public void getPossibleMovesTest() {
        board[0][1] = 1;
        Coordinate c = new Coordinate(0,1);
        System.out.println("ValidatorTest, getPossibleMovesTest(): empty\n" + new Board(board));
        assertEquals(0, validator.getPossibleMoves(c).size());
        board[2][1] = 1; board[1][2] = 1;
        System.out.println("getPossibleMovesTest(): one possible move from 0,1\n" + new Board(board));
        assertEquals(1, validator.getPossibleMoves(c).size());
        board[1][0] = -1;
        System.out.println("getPossibleMovesTest(): 0 possible moves from 0,1\n" + new Board(board));
        assertEquals(0, validator.getPossibleMoves(c).size());

        fillBoardWithRedsInShapeOfX(); board[0][1] = 1;
        System.out.println("getPossibleMovesTest(): 2 possible moves from 0,1\n" + new Board(board));
        assertEquals(2, validator.getPossibleMoves(c).size());

        c.x = 3; c.y = 2;
        fillBoardWithRedsInShapeOfX();
        System.out.println("getPossibleMovesTest(): 5 possible moves from 3,2\n" + new Board(board));
        assertEquals(5, validator.getPossibleMoves(c).size());

        for (int i = 0; i < 7; i++) {
            board[i][5] = -1;
        }
        board[3][6] = 1;
        c.x = 3; c.y = 6;
        System.out.println("getPossibleMovesTest(): 2 possible moves from 3,6\n" + new Board(board));
        assertEquals(2, validator.getPossibleMoves(c).size());
    }

    @Test
    public void getConnectecWhitesTest(){
        ArrayList<Coordinate> connected = validator.getConnectedWhites(1, 0);
        assertEquals(16, connected.size());
        for (int i = 2; i < 6; i++) {
            assertTrue(connected.contains(new Coordinate(i, 0)));
        }
        for (int i = 1; i < 6; i++) {
            assertTrue(connected.contains(new Coordinate(i + 1, i)));
        }
        for (int i = 1; i < 7; i++) {
            assertTrue(connected.contains(new Coordinate(1, i)));
        }
        assertTrue(connected.contains(new Coordinate(0, 1)));
        assertFalse(connected.contains(new Coordinate(0, 0)));
        assertFalse(connected.contains(new Coordinate(1, -1)));
        assertFalse(connected.contains(new Coordinate(1, 7)));
        board[1][1] = 1;
        validator.refreshBoard(board);
        connected = validator.getConnectedWhites(1, 0);
        assertEquals(10, connected.size());
        for (int i = 1; i < 7; i++) {
            assertFalse(connected.contains(new Coordinate(1, i)));
        }
    }

    @Test
    public void howManyTrianglesFoundTest() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                assertEquals(0, validator.howManyTrianglesFound(new Coordinate(i,j), 1));
            }
        }

        fillBoardWithRedsInShapeOfX();
        assertEquals(5, validator.howManyTrianglesFound(new Coordinate(3,0), 1));
        assertEquals(0, validator.howManyTrianglesFound(new Coordinate(3,0), -1));
        assertEquals(2, validator.howManyTrianglesFound(new Coordinate(5, 6), 1));
    }

    @Test
    public void lookForTrianglesInTheDistanceOfTest() {
        fillBoardWithRedsInShapeOfX();
        System.out.println("ValidatorTest, lookForTrianglesInTheDistanceOfTest():\n" + new Board(board));
        assertEquals(5, validator.lookForTrianglesInTheDistanceOf(2, new Coordinate(3,0), 1));
        assertEquals(0, validator.lookForTrianglesInTheDistanceOf(4, new Coordinate(3,0), 1));
        assertEquals(0, validator.lookForTrianglesInTheDistanceOf(6, new Coordinate(3,0), 1));

    }

    @Test
    public void lookForTrianglesInOneDirectionTestSmallTriangle() {
        //initializing board with 5 red stones
        board[0][1] = 1; board[1][0] = 1; board[0][3] = 1; board[2][1] = 1;  board[1][4] = 1;
        validator.refreshBoard(board);
        Coordinate h, d1, d2; //hypotenuse, corner candidates 1 and 2

        //hypotenuse off board, corners are red
        h = new Coordinate(-1,2); d1 = new Coordinate(0,1); d2 = new Coordinate(0,3);
        assertEquals(1, validator.lookForTrianglesInOneDirection(h, d1, d2, 1));

        //hypotenuse on board and red, corners are red
        h = new Coordinate(1,0); d2 = new Coordinate(2,1);
        assertEquals(3, validator.lookForTrianglesInOneDirection(h, d1, d2, 1));

        //h on board and white, one of the corners is red
        h = new Coordinate(3,2); d1 = new Coordinate(2,3);
        assertEquals(0, validator.lookForTrianglesInOneDirection(h, d1, d2, 1));

        //h on board and red, one of the corners is red
        h = new Coordinate(1,4); d2 = new Coordinate(0, 3);
        assertEquals(1, validator.lookForTrianglesInOneDirection(h, d1, d2, 1));

        board[0][3] = -1; //lets change one red to blue
        assertEquals(0, validator.lookForTrianglesInOneDirection(h, d1, d2, 1));
    }

    @Test
    public void lookForTrianglesInOneDirectionTestMiddleTriangle() {
        //initializing board with 4 blue stones
        board[3][1] = -1; board[1][3] = -1; board[5][3] = -1;
        validator.refreshBoard(board);
        Coordinate h, d1, d2; //hypotenuse, corner candidates 1 and 2

        //hypotenuse off board, one corner on board and blue
        h = new Coordinate(-1, 5); d1 = new Coordinate(1, 3); d2 = new Coordinate(1, 7);
        assertEquals(0, validator.lookForTrianglesInOneDirection(h, d1, d2, -1));

        //all three off board
        h = new Coordinate(4, 9); d1 = new Coordinate(5, 7);
        assertEquals(0, validator.lookForTrianglesInOneDirection(h, d1, d2, -1));

        //all three on board and blue
        h = new Coordinate(3,1); d1 = new Coordinate(1,3); d2 = new Coordinate(5,3);
        assertEquals(3, validator.lookForTrianglesInOneDirection(h, d1, d2, -1));
    }

    @Test
    public void isCoordinateOnBoardAndRightColorTest() {
        fillBoardWithRedsInShapeOfX();
        System.out.println("ValidatorTest, isCoordinateOnBoardAndRightColorTest():\n" + new Board(board));
        Coordinate c = new Coordinate(1,0);
        assertTrue(validator.isCoordinateOnBoardAndRightColor(c, 1));
        assertFalse(validator.isCoordinateOnBoardAndRightColor(c, -1));
        c.x = 3; c.y = 2;
        assertTrue(validator.isCoordinateOnBoardAndRightColor(c, 1));
        assertFalse(validator.isCoordinateOnBoardAndRightColor(c, -1));
        c.y = -2;
        assertFalse(validator.isCoordinateOnBoardAndRightColor(c, 1));
        c.x = 1; c.y = 2;
        assertTrue(validator.isCoordinateOnBoardAndRightColor(c, 0));
        assertFalse(validator.isCoordinateOnBoardAndRightColor(c, 1));
    }

    @Test
    public void isThisOffBoardTest(){
        assertTrue(validator.isThisOffBoard(new Coordinate(0, 0)));
        assertTrue(validator.isThisOffBoard(new Coordinate(6, 6)));
        assertTrue(validator.isThisOffBoard(new Coordinate(0, 6)));
        assertTrue(validator.isThisOffBoard(new Coordinate(6, 0)));
        assertFalse(validator.isThisOffBoard(new Coordinate(1, 0)));
        assertFalse(validator.isThisOffBoard(new Coordinate(0, 1)));
        assertFalse(validator.isThisOffBoard(new Coordinate(5, 6)));
        assertFalse(validator.isThisOffBoard(new Coordinate(6, 5)));
    }
    
    @Test
    public void areAllDirectionsBlockedTest(){
        assertTrue(validator.areAllDirectionsBlocked(new boolean[]{true, true, true, true}));
        assertFalse(validator.areAllDirectionsBlocked(new boolean[]{true, true, false, true}));
    }
    
    
    
}
