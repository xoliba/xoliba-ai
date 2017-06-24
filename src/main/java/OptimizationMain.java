import Optimization.*;
import AI.*;

import java.util.List;

import static AI.AI.bestParameters;

public class OptimizationMain {

    public static void main(String[] args) {
        ParameterWriter pw = new ParameterWriter();
        pw.writeNewFileWithParameterValues();
        computeWithAllParameterCombinations(pw, 50, 1, 1);

/*
        new MatchMaker(2, bestParameters,
                1,
                //new ParametersAI(-35, 10, 15, 5, 5),
                bestParameters,
                true, //do we print more?
                false //do we run on single thread
        ).calculate(50);
*/
    }

    private static void computeWithAllParameterCombinations(ParameterWriter pw, int howManyGames, int whiteDifficulty, int blackDifficulty) {
        List<ParametersAI> parameterCombinations = pw.readParameterCombinations();
        int combinations = parameterCombinations.size();
        int paramCount = bestParameters.toArray().length;
        ParametersAI[] theBestParameters = new ParametersAI[paramCount];
        double[] bestParametersPerformance = new double[paramCount];
        int bestIndex = 0;
        for (int i = 0; i < combinations; i++) {
            if (i != 0 && i % (combinations / paramCount) == 0) {
                System.out.println("\n¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ START TESTING WITH NEW PARAMETER ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\n");
                bestIndex++;
            }
            ParametersAI p = parameterCombinations.get(i);
            System.out.println( "  %%% New gladiators in the pit! %%%\n" +
                    "Default parameters on white: " + bestParameters + "\n" +
                    "versus black with test param " + p
                    //+ "\n"
            );

            MatchMaker referee = new MatchMaker(whiteDifficulty, bestParameters, blackDifficulty, p);
            double challengerPerformance = referee.calculate(howManyGames);

            if (theBestParameters[bestIndex] == null || bestParametersPerformance[bestIndex] < challengerPerformance) {
                theBestParameters[bestIndex] = p;
                bestParametersPerformance[bestIndex] = challengerPerformance;
            }
        }

        System.out.println("\nThe best parameters and corresponding performance:\n");
        for (int i = 0; i < theBestParameters.length; i++) {
            System.out.println(theBestParameters[i] + "\n\tchallenger performance: " + bestParametersPerformance[i] + "\n");
        }
    }
}
