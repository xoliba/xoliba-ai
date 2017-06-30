import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import static AI.AI.bestParameters;

public class OptimizationMain {
    public static void main(String[] args) {
        ///*
        new Coach(true).runOneShowdown(2, bestParameters,
                6,
                bestParameters,
                //new AI(1).getBestParameters(4),
                2,
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
