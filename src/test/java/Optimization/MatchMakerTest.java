package Optimization;

import AI.AI;
import AI.ParametersAI;
import Game.Board;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by vili on 21.6.2017.
 */
public class MatchMakerTest {

    private MatchMaker mm;
    private int[][] board;

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
    }


    @Test
    public void bothAIsGetBothColorsForOneBoardTest() {
        MatchMaker spiedMM = spy(mm);
        spiedMM.calculateRoundForBothRoles(board);
        verify(spiedMM, times(2)).calculateRound(any(AI.class), any(AI.class), eq(board));
        verify(spiedMM, times(2)).playUntilRoundEnded(any(AI.class), any(AI.class), eq(this.board));
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
        System.out.println("testing with board\n" + new Board(board));
        Board endBoard = new Board(mm.playUntilRoundEnded(first, second, board));
        System.out.println("after finishing game:\n" + endBoard);
        assertTrue("if red starts and looks two moves ahead, it must win", endBoard.calculatePoints() > 0);
    }

}