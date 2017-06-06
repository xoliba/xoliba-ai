/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Game.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eerop
 */
public class StoneCollectorTest {

    private StoneCollector s;
    private Board b;
    private Move m;

    public StoneCollectorTest() {
        b = new Board(new int[7][7]);
        s = new StoneCollector();
        m = new Move(null, null);
    }

    @Before
    public void setUp() {
    }

    @Test
    public void hitTrianglesTest() {
        b.board[3][1] = 1;
        b.board[3][5] = 1;
        b.board[5][3] = 1;
        b.board[2][3] = 1;    
        b.board[4][1] = 1;
        b.board[4][5] = 1; 
        b.board[1][3] = 1;
        b.board[1][4] = 1;
        b.board[5][4] = 1;
        

        //Testing vertical triangle with heap right from base
        hitTriangleTestVerticalHelper(new Coordinate(3, 1), new Coordinate(3, 5), new Coordinate(5, 3));
        hitTriangleTestVerticalHelper(new Coordinate(3, 1), new Coordinate(5, 3), new Coordinate(3, 5));
        hitTriangleTestVerticalHelper(new Coordinate(3, 5), new Coordinate(3, 1), new Coordinate(5, 3));
        hitTriangleTestVerticalHelper(new Coordinate(3, 5), new Coordinate(5, 3), new Coordinate(3, 1));
        hitTriangleTestVerticalHelper(new Coordinate(5, 3), new Coordinate(3, 5), new Coordinate(3, 1));
        hitTriangleTestVerticalHelper(new Coordinate(5, 3), new Coordinate(3, 1), new Coordinate(3, 5));

        //Testing Vertical triangle with heap left from base
        hitTriangleTestVerticalHelper(new Coordinate(2, 3), new Coordinate(4, 1), new Coordinate(4, 5));
        hitTriangleTestVerticalHelper(new Coordinate(2, 3), new Coordinate(4, 5), new Coordinate(4, 1));
        hitTriangleTestVerticalHelper(new Coordinate(4, 1), new Coordinate(2, 3), new Coordinate(4, 5));
        hitTriangleTestVerticalHelper(new Coordinate(4, 1), new Coordinate(4, 5), new Coordinate(2, 3));
        hitTriangleTestVerticalHelper(new Coordinate(4, 5), new Coordinate(2, 3), new Coordinate(4, 1));
        hitTriangleTestVerticalHelper(new Coordinate(4, 5), new Coordinate(4, 1), new Coordinate(2, 3));
        
        b.board[3][2] = 1;
        
        //Testing Horizontal triangle with heap below it
        hitTriangleTestHorizontalHelper(new Coordinate(1, 3), new Coordinate(5, 3), new Coordinate(3, 5));
        hitTriangleTestHorizontalHelper(new Coordinate(1, 3), new Coordinate(3, 5), new Coordinate(5, 3));
        hitTriangleTestHorizontalHelper(new Coordinate(5, 3), new Coordinate(1, 3), new Coordinate(3, 5));
        hitTriangleTestHorizontalHelper(new Coordinate(5, 3), new Coordinate(3, 5), new Coordinate(1, 3));
        hitTriangleTestHorizontalHelper(new Coordinate(3, 5), new Coordinate(1, 3), new Coordinate(5, 3));
        hitTriangleTestHorizontalHelper(new Coordinate(3, 5), new Coordinate(5, 3), new Coordinate(1, 3));
        
        //Testing Horizontal triangle with heap above it
        hitTriangleTestHorizontalHelper(new Coordinate(1, 4), new Coordinate(5, 4), new Coordinate(3, 2));
        hitTriangleTestHorizontalHelper(new Coordinate(1, 4), new Coordinate(3, 2), new Coordinate(5, 4));
        hitTriangleTestHorizontalHelper(new Coordinate(5, 4), new Coordinate(1, 4), new Coordinate(3, 2));
        hitTriangleTestHorizontalHelper(new Coordinate(5, 4), new Coordinate(3, 2), new Coordinate(1, 4));
        hitTriangleTestHorizontalHelper(new Coordinate(3, 2), new Coordinate(1, 4), new Coordinate(5, 4));
        hitTriangleTestHorizontalHelper(new Coordinate(3, 2), new Coordinate(5, 4), new Coordinate(1, 4));

    }

    private void hitTriangleTestVerticalHelper(Coordinate c1, Coordinate c2, Coordinate c3) {
        for (int i = 1; i <= 3; i++) {
            b.board[3][1 + i] = 1;
            b.board[4][1 + i] = -1;
        }
        m.removeTriangles();
        m.addPossibleTriangle(new Triangle(c1, c2, c3));
        Board board = s.collectStonesFromAnyTriangleAvailable(b, m);

        for (int i = 1; i <= 3; i++) {
            assertEquals(0, board.board[3][1 + i]);
            assertEquals(0, board.board[4][1 + i]);
        }
        assertEquals(1, board.board[3][1]);
        assertEquals(1, board.board[5][3]);
        assertEquals(1, board.board[3][5]);
    }

    private void hitTriangleTestHorizontalHelper(Coordinate c1, Coordinate c2, Coordinate c3) {
        for (int i = 1; i <= 3; i++){
            b.board[1 + i][3] = 1;
            b.board[1 + i][4] = -1;
        }
        m.removeTriangles();
        m.addPossibleTriangle(new Triangle(c1, c2, c3));
        Board board = s.collectStonesFromAnyTriangleAvailable(b, m);
        
        for (int i = 1; i <= 3; i++) {
            assertEquals(0, board.board[1 + i][3]);
            assertEquals(0, board.board[1 + i][4]);
        }
        assertEquals(1, board.board[3][5]);
        assertEquals(1, board.board[1][3]);
        assertEquals(1, board.board[5][3]);
    }

}
