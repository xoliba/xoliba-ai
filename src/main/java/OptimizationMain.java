import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import static AI.AI.bestParameters;

public class OptimizationMain {
    public static void main(String[] args) {
        /*
        new Coach(true).runOneShowdown(2, bestParameters,
                4,
                //new ParametersAI(100,1,1,1,1,1),
                new AI(1).getBestParameters(4),
                2);
        */
        ///*
        boolean[] testTheseParameters = new boolean[]{
                true, true, true, true, true, false};
        new Coach(true).iterateWithDifferentParameters(testTheseParameters, 1, 61, 30,
                50, 4, 4);
        //*/
    }

}
