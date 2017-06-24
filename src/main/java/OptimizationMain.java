import Optimization.*;
import AI.*;

import java.util.List;

import static AI.AI.bestParameters;

public class OptimizationMain {

    public static void main(String[] args) {
        ParameterWriter pw = new ParameterWriter();
        pw.writeNewFileWithParameterValues();
        //computeWithAllParameterCombinations(pw, 50);


        new MatchMaker(2, bestParameters,
                1,
                //new ParametersAI(-35, 10, 15, 5, 5),
                bestParameters,
                true, //do we print more?
                true //do we run on single thread
        ).calculate(100);

    }

    private static void computeWithAllParameterCombinations(ParameterWriter pw, int howManyGames) {
        List<ParametersAI> parameterCombinations = pw.readParameterCombinations();
        for (int i = 0; i < parameterCombinations.size(); i++) {
            ParametersAI p = parameterCombinations.get(i);
            System.out.println( "  %%% New gladiators in the pit! %%%\n" +
                    "Default parameters on white: " + bestParameters + "\n" +
                                "versus black with test param " + p + "\n");
            new MatchMaker(1, bestParameters,
                            1, p
            ).calculate(howManyGames);
        }
    }
}
