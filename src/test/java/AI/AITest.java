package AI;

import Game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by vili on 5.6.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AITest {

    private AI ai;
    private Board boardWithTwoSmallTriangles;
    private int[][] table;

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
        boardWithTwoSmallTriangles = new Board(table);
    }

    @Test
    public void AIDoesAMove() {
        Board copy = boardWithTwoSmallTriangles.copy();
        assertTrue("the board should be the same if no move was made",
                copy.equals(boardWithTwoSmallTriangles));
        int[][] boardAfterMove = ai.move(table).board;
        assertFalse("the board should be different after a move:\n" +
                "board before move\n" + copy + "board after move\n"
                + new Board(boardAfterMove) , new Board(boardAfterMove).equals(copy));
    }

    @Test
    public void aiCanMoveWithBothColors() {
        AI mockedAI = mock(AI.class);

        mockedAI.move(new int[7][7]);
    }

    @Test
    public void AIcomputesDeepInTheGameTreeWhileThereAreMoves() {
        ai = new AI(1, 10);
        int[][] boardAfter10Moves = ai.move(table).board;
        assertFalse(new Board(boardAfter10Moves).equals(new Board(table)));
    }

    @Test
    public void maxValueTest() {
        AI mockAI = mock(AI.class);

        when(mockAI.getDifficulty()).thenReturn(1);

        System.out.println(mockAI.getDifficulty());
        verify(mockAI).getDifficulty();
    }
}
