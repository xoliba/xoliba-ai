import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import static AI.AI.bestParameters;

public class OptimizationMain {
    public static void main(String[] args) {
        ///*
        new Coach(true).runOneShowdown(1, bestParameters,
                1,
                new AI(1).getBestParameters(1),
                50,
                false);
        //*/
        /*
        boolean[] testTheseParameters = new boolean[]{
                true, true, true, false, false, true};
        new Coach(true).iterateWithDifferentParameters(testTheseParameters, 1, 101, 10,
                100, 4, 4);
        */
    }

}
