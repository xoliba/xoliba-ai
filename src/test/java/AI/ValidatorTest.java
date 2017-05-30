package AI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import AI.Coordinate;
import AI.Validator;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eerop
 */
public class ValidatorTest {
    
    private Validator validator;
    private int[][] board;
    
    public ValidatorTest() {
        validator = new Validator();
        this.board = new int[7][7];
        validator.updateBoard(board);
    }
    
    @Before
    public void setUp() {
        
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
        validator.updateBoard(board);
        connected = validator.getConnectedWhites(1, 0);
        assertEquals(10, connected.size());
        for (int i = 1; i < 7; i++) {
            assertFalse(connected.contains(new Coordinate(1, i)));
        }
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
