/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
        ArrayList<int[]> connected = validator.getConnectedWhites(1, 0);
        for (int[] connected1 : connected) {
            for (int d : connected1) {
                System.out.println(d);
            }
            System.out.println("pöö");
        }
        assertEquals(16, connected.size());
        for (int i = 2; i < 6; i++) {
            assertFalse(connected.contains(new int[]{i,0}));
        }
        for (int i = 1; i < 6; i++) {
            assertTrue(connected.contains(new int[]{i + 1, i}));
        }
        for (int i = 1; i < 7; i++) {
            assertTrue(connected.contains(new int[]{1, i}));
        }
        assertTrue(connected.contains(new int[]{0, 1}));
        assertFalse(connected.contains(new int[]{0, 0}));
        board[1][1] = 1;
        validator.updateBoard(board);
        connected = validator.getConnectedWhites(1, 0);
        assertEquals(10, connected.size());
        for (int i = 1; i < 7; i++) {
            assertFalse(connected.contains(new int[]{1, i}));
        }
    }
    
    @Test
    public void isThisOffBoardTest(){
        assertTrue(validator.isThisOffBoard(new int[]{0, 0}));
        assertTrue(validator.isThisOffBoard(new int[]{6, 6}));
        assertTrue(validator.isThisOffBoard(new int[]{0, 6}));
        assertTrue(validator.isThisOffBoard(new int[]{6, 0}));
        assertFalse(validator.isThisOffBoard(new int[]{1, 0}));
        assertFalse(validator.isThisOffBoard(new int[]{0, 1}));
        assertFalse(validator.isThisOffBoard(new int[]{5, 6}));
        assertFalse(validator.isThisOffBoard(new int[]{6, 5}));
    }
    
}
