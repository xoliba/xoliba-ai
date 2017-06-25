package Optimization;

import AI.ParametersAI;
import Game.Board;
import Game.RoundRecord;
import Game.TurnData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static AI.AI.bestParameters;
import static org.junit.Assert.*;

/**
 * Created by vili on 25.6.2017.
 */
public class CoachTest {

    private Coach coach;

    @Before
    public void setup() {
        coach = new Coach();
    }

    @Test
    public void AIsShouldActDifferentlyWithDifferentParameters() {
        AIMatchResult match = new Coach().runOneShowdown(2, new ParametersAI(1, 100, 100, 100, 100, 0),
                2, new ParametersAI(100,1,1,1,1,1),
                1, true);

        RoundRecord[] bothRounds = match.theFinalResult.getAllRounds().get(0);
        boolean allTheMovesAreSame = true;
        ArrayList<TurnData> firstRoundTurns = bothRounds[0].getTurns();
        ArrayList<TurnData> secondRoundTurns = bothRounds[1].getTurns();
        for (int i = 0; i < firstRoundTurns.size(); i++) {
            if (i >= secondRoundTurns.size())
                break;
            if (!new Board(firstRoundTurns.get(i).board).equals(new Board(secondRoundTurns.get(i).board))) {
                allTheMovesAreSame = false;
                break;
            }
        }
        assertFalse("if we play with two very differently thinking AIs they cant make all the same moves!", allTheMovesAreSame);
    }
}
