import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import static AI.AI.bestParameters;

public class OptimizationMain {

    public static void main(String[] args) {
        /*
        new Coach(true).runOneShowdown(3, bestParameters,
                3, new ParametersAI(5,40,40,5,5,5),
                1);
        */
        ///*
        boolean[] testTheseParameters = new boolean[]{
                true, true, true, true, true, true};
        new Coach(true).iterateWithDifferentParameters(testTheseParameters, 0, 120, 20,
                50, 2, 2);
        //*/
    }

}
