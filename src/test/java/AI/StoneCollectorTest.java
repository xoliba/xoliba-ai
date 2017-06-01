/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;


import Game.Board;
import Game.Coordinate;
import Game.Move;
import Game.Triangle;
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
    
    public StoneCollectorTest() {
        b = new Board(new int[7][7]);
        s = new StoneCollector();
    }
        
    @Before
    public void setUp() {
    }
    
    @Test
    public void hitTrianglesTest(){
        //Board board, int basis1, int basis2, int bottomH, int tipH, boolean isVertical
        b.board[1][3] = 1;
        b.board[3][1] = 1;
        b.board[5][3] = 1;
        b.board[3][5] = 1;
        
        for (int i = 1; i <= 3; i++) {
            b.board[3][1 + i] = 1;
            b.board[4][1 + i] = -1;
        }
        
        Move m = new Move(new Coordinate(0, 1), new Coordinate(1, 1));
        m.addPossibleTriangle(new Triangle(new Coordinate(3, 1), new Coordinate(3, 5), new Coordinate(5, 3)));
        
        Board board = s.collectStonesFromAnyTriangleAvailable(b, m);
        
        for (int i = 1; i <= 3; i++) {
            assertEquals(0, board.board[3][1 + i]);
            assertEquals(0, board.board[4][1 + i]);
        }
        assertEquals(1, board.board[1][3]);
        assertEquals(1, board.board[5][3]);
        assertEquals(1, board.board[3][5]);
    }
    
}
