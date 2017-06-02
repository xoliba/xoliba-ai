/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eerop
 */
public class CoordinateTest {
    
    private Coordinate c;
    
    public CoordinateTest() {
        c = new Coordinate(1, 2);
    }
    
    @Test
    public void equalsTest() {
        assertFalse(c.equals(null));
        assertFalse(c.equals(new Board()));
        assertFalse(c.equals(new Coordinate(1, 1)));
        assertTrue(c.equals(new Coordinate(1, 2)));
    }
    
}
