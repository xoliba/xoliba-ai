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
        int[][] boardAfterMove = boardWithTwoSmallTriangles.board.clone();
        assertTrue("the board should be the same if no move was made", new Board(boardAfterMove).equals(boardWithTwoSmallTriangles));
        int[][] tableClone = table.clone();
        System.out.printf("new board\n" + new Board(tableClone));
        boardAfterMove = ai.move(table);
        System.out.printf("the same clone\n" + new Board(tableClone));
        assertFalse("the board should be different after a move:\n" +
                "board before move\n" + new Board(tableClone) + "board after move\n"
                + new Board(boardAfterMove) , new Board(boardAfterMove).equals(new Board(tableClone)));
    }
}
