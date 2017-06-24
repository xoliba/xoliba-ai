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
        assertTrue(rr.getTotalChallangerWinsMoreGamesValue() == 0);
        rr1.updateStats();
        assertTrue(rr1.getTotalSameColorWins() == 0);
        assertTrue(rr1.getTotalChallangerWinsMoreGamesValue() == 1);
        RoundResult fiRes = new RoundResult();
        fiRes.blackWins++;
        fiRes.whiteWins++;
        fiRes.whitePoints += 18;
        fiRes.blackPoints += 12;
        fiRes.updateStats();
        assertTrue(fiRes.getTotalSameColorWins() == 1);
        fiRes.add(rr);
        assertTrue(fiRes.getTotalSameColorWins() == 2);
        assertTrue(fiRes.getTotalChallangerWinsMoreGamesValue() == 0);
        assertTrue(rr.getTotalSameColorWins() == 1);
        fiRes.add(rr1);
        assertTrue(fiRes.getTotalSameColorWins() == 2);
        assertTrue(fiRes.getTotalChallangerWinsMoreGamesValue() == 1);
        assertTrue(rr1.getTotalChallangerWinsMoreGamesValue() == 1);
    }
}
