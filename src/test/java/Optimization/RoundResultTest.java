package Optimization;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vili on 22.6.2017.
 */
public class RoundResultTest {

    public RoundResult rr, rr1;

    public RoundResultTest() {
        rr = new RoundResult(1,1,42,34);
        rr1 = new RoundResult(0,2,0, 30);
    }

    @Test
    public void addTest() {
        rr.updateStats();
        assertTrue(rr.getTotalSameColorWins() == 1);
        assertTrue(rr.getTotalChallangerWinsBothGamesValue() == 0);
        rr1.updateStats();
    }
}
