import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import static AI.AI.bestParameters;

public class OptimizationMain {
    public static void main(String[] args) {
        ///*
        new Coach(true).runOneShowdown(4, bestParameters,
                2,
                //new ParametersAI(100,1,1,1,1,1),
                bestParameters,
                50);
        //*/
        /*
        boolean[] testTheseParameters = new boolean[]{
                true, true, true, true, true, true};
        new Coach(true).iterateWithDifferentParameters(testTheseParameters, 0, 120, 20,
                50, 2, 2);
        */
    }

}
