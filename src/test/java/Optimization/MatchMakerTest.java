package Optimization;

import AI.AI;
import AI.ParametersAI;
import Game.Board;
import Game.RoundRecord;
import Messaging.JsonConverter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by vili on 21.6.2017.
 */
public class MatchMakerTest {

    private MatchMaker mm;
    private int[][] board, board1;
    private String b56JSON =
            "[[-2,0,1,1,1,-1,-2],[1,-1,0,-1,1,0,1],[-1,0,-1,-1,-1,-1,0],[1,1,1,-1,-1,1,-1],[-1,-1,1,0,1,0,0],[0,1,1,1,1,-1,0],[-2,0,-1,-1,1,-1,-2]]";

    public MatchMakerTest() {
        this.mm = new MatchMaker(2, AI.bestParameters, 1, AI.bestParameters);
    }

    @Before
    public void setBoard() {
        this.board = new int[][]{
                {-2, 0, 0, 0, 0, 0,-2},
                {-1, 0, 0, 0, 0, 0, 0},
                { 1, 0, 0, 0, 0, 0, 0},
                {-1, 1, 0, 0, 0, 1, 0},
                { 0, 0, 0,-1, 0, 0, 0},
                {-1, 0, 0, 1, 0, 0,-1},
                {-2, 0, 0, 0, 0, 0,-2},
        };
        this.board1 = JsonConverter.parseTable(b56JSON);
    }


    @Test
    public void bothAIsGetBothColorsForOneBoardTest() {
        MatchMaker spiedMM = spy(mm);
        spiedMM.calculateRoundForBothRoles(board);
        verify(spiedMM, times(2)).calculateRound(any(AI.class), any(AI.class), eq(board));
        verify(spiedMM, times(2)).playUntilRoundEnded(any(AI.class), any(AI.class), eq(this.board), any(RoundRecord.class));

        /*
        //for these test to succeed we would need to overwrite the hashcode method of AI, and I don't dare to do it.
        //one can still run these and see if from the report if the code actually works or not
        spiedMM = spy(new MatchMaker(2, AI.bestParameters, 1, AI.bestParameters));
        spiedMM.calculateRoundForBothRoles(this.board);
        verify(spiedMM, times(2)).calculateRound(any(AI.class), any(AI.class), eq(board));
        verify(spiedMM).calculateRound(eq(new AI(1, 1)), eq(new AI(-1, 2)), eq(this.board));
        verify(spiedMM).calculateRound(eq(new AI(1, 2)), eq(new AI(-1, 1)), eq(this.board));
        */
    }

    @Test
    public void calculateRoundGivesRightResult() {
        MatchMaker spiedMM = spy(mm);

        System.out.println("testing with board\n" + new Board(board));
        RoundResult rr = spiedMM.calculateRoundForBothRoles(board);
        System.out.println(rr);
        assertTrue("the deeper looking AI should always be able to win when it plays red",rr.whitePoints > 0);
    }

    @Test
    public void playUntilRoundEndedTest() {
        AI first = new AI(1, 2, new ParametersAI());
        AI second = new AI(-1, 2, new ParametersAI());
        Board b = new Board(board);
        System.out.println("testing with board\n" + b);
        RoundRecord record = mm.playUntilRoundEnded(first, second, board, new RoundRecord(b, first, second));
        Board endBoard = record.getEndBoard();
        System.out.println("after finishing game:\n" + endBoard);
        assertTrue("if red starts and looks two moves ahead, it must win", endBoard.calculatePoints() > 0);
    }

    @Test
    public void sameLevelAIsWithSameParametersGetSameResults() {
        mm = new MatchMaker(2, new ParametersAI(), 2, new ParametersAI());
        Board b = new Board(board1);
        System.out.println("testing with board\n" + b);
        System.out.println("We run the test three times because sometimes it match maker gives different results!?!?\n");
        for (int i = 0; i < 3; i++) {
            RoundResult rr = mm.calculateRoundForBothRoles(board1);
            System.out.println(rr + "\n" + rr.endGameMessagesToString());
            assertTrue("Both AIs should win the same amount when playing the same board with same params and difficulty!",
                    rr.whitePoints == rr.blackPoints && rr.whiteWins == rr.blackWins);
        }
    }
}
