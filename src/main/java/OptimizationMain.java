import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import java.util.ArrayList;
import java.util.List;

import static AI.AI.bestParameters;

public class OptimizationMain {


    public static void main(String[] args) {
        /*
        new Coach(true).runOneShowdown(2, bestParameters,
                2, new ParametersAI(100,1,1,1,1,1),
                1);
        */
        ///*
        boolean[] testTheseParameters = new boolean[]{
                true, true, true, false, false, false};
        new Coach(false).iterateWithDifferentParameters(testTheseParameters, 0, 120, 20, 50, 2, 2);
        //*/
    }

}
