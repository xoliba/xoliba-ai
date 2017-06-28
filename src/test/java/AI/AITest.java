package AI;

import Game.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vili on 5.6.2017.
 */
public class AITest {

    private AI ai;
    private Board boardWithTwoSmallTriangles;
    private int[][] table, table1;

    @Before
    public void setUp() {
        ai = new AI(1); //AI plays as red
        table = new int[][]{
                {-2,0,0,0,0,-1,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {1,0,1,0,0,0,0},
                {0,1,0,0,0,0,0},
                {-2,0,0,0,0,0,-2},
        };
        table1 = new int[][]{
                {-2,0,0,0,0,-1,-2},
                {0,0,0,0,-1,0,-1},
                {0,0,0,0,0,0,0},
                {0,0,0,0,-1,0,0},
                {1,0,1,0,0,0,0},
                {0,1,0,0,0,0,-1},
                {-2,0,0,0,0,0,-2},
        };
        boardWithTwoSmallTriangles = new Board(table);
    }

    @Test
    public void AIknowsHowToSurrender() {
        int[][] table = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                table[i][j] = -1;
            }
        }
        TurnData td = new TurnData(true, true, table);
        assertTrue("surrender test:\n" + ai + "\n" + td
                + "data red points " + td.redPoints + " blue points " + td.bluePoints + " score limit " + td.scoreLimit
                , ai.doesWantToSurrender(td));
        ai = new AI(-1);
        assertFalse(ai.doesWantToSurrender(td));
        ai = new AI(1, 3);
        td.bluePoints = 30;
        assertFalse(ai.doesWantToSurrender(td));
    }

    @Test
    public void AIknowsHowToAnswerEndGameRequest() {
        TurnData td = new TurnData(false, true, table1);
        assertFalse("AI should agree only if it wins, AI:\n" + ai + "\n" + td, ai.doesWantToStopPlaying(td));
        ai = new AI(-1);
        assertTrue("AI should agree only if it wins, AI:\n" + ai + "\n" + td, ai.doesWantToStopPlaying(td));
        ai = new AI(1, 4);
        assertFalse("hard AI should agree to end game if player asks, AI:\n" + ai + "\n" + td, ai.doesWantToStopPlaying(td));
    }

    @Test
    public void AIDoesAMove() {
        Board copy = boardWithTwoSmallTriangles.copy();
        assertTrue("the board should be the same if no move was made",
                copy.equals(boardWithTwoSmallTriangles));
        int[][] boardAfterMove = ai.move(table, 1).board;
        assertFalse("the board should be different after a move:\n" +
                "board before move\n" + copy + "board after move\n"
                + new Board(boardAfterMove) , new Board(boardAfterMove).equals(copy));
    }

    @Test
    public void aiCanMoveWithBothColors() {
        ai = new AI(1, 1);
        Board start = new Board(table);
        TurnData td = ai.move(start.copy().board, 1);
        assertFalse(start.equals(td.board));
        ai = new AI(-1, 1);
        td = ai.move(start.copy().board, 1);
        assertFalse(start.equals(td.board));
    }

    @Test
    public void AIcomputesDeepInTheGameTreeWhileThereAreMoves() {
        ai = new AI(1, 10);
        int[][] boardAfter10Moves = ai.move(table, 1).board;
        assertFalse(new Board(boardAfter10Moves).equals(new Board(table)));
    }

    @Test
    public void getDifficultyTest() {
        assertTrue(ai.getDifficulty() > -1);
        ai = new AI(1, 3);
        assertTrue(ai.getDifficulty() == 3);
    }
}
