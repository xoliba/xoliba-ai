import Game.RoundRecord;
import Game.TurnData;
import Optimization.*;
import AI.*;

import java.util.ArrayList;
import java.util.List;

import static AI.AI.bestParameters;

public class OptimizationMain {


    public static void main(String[] args) {
        new Coach().runOneShowdown(2, bestParameters,
                2, new ParametersAI(100,1,1,1,1,1),
                1, true);

        /*
        boolean[] testTheseParameters = new boolean[]{
                true, true, true, false, false, false};
        new Coach().iterateWithDifferentParameters(testTheseParameters, 0, 1000, 500, 100, 2, 2);
        */
    }

}
