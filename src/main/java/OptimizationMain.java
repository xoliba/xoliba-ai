import Optimization.*;
import AI.*;

import java.util.List;

import static AI.AI.bestParameters;

public class OptimizationMain {

    public static void main(String[] args) {
        testOneSetup(3, 1, 50);
        //iterateWithDifferentParameters(0, 100, 10, 50, 1, 1);

    }

    private static void testOneSetup(int whiteLVL, int blackLVL, int howManyBoards) {
       MatchMaker referee = new MatchMaker(whiteLVL, bestParameters,
                blackLVL,
                //new ParametersAI(-35, 10, 15, 5, 5),
                bestParameters,
                true, //do we print more?
                false //do we run on single thread
        );
        System.out.println(referee.calculate(howManyBoards));
    }

    private static void iterateWithDifferentParameters(int minWeight, int maxWeight, double frequency, int howManyBoards, int whiteLVL, int blackLVL) {
        ParameterWriter pw = new ParameterWriter(minWeight, maxWeight, frequency);
        pw.writeNewFileWithParameterValues();
        computeWithAllParameterCombinations(pw, howManyBoards, whiteLVL, blackLVL);
    }

    private static void computeWithAllParameterCombinations(ParameterWriter pw, int howManyBoards, int whiteDifficulty, int blackDifficulty) {
        long start = System.currentTimeMillis();
        List<ParametersAI> parameterCombinations = pw.readParameterCombinations();
        int combinations = parameterCombinations.size();
        int paramCount = bestParameters.toArray().length;
        AIMatchResult[] theBestFinalResults = new AIMatchResult[paramCount];
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
            AIMatchResult theResult = referee.calculate(howManyBoards);
            System.out.println("challengers performance: " + theResult.challengerPerformance + "\n");

            if (theBestFinalResults[bestIndex] == null || theBestFinalResults[bestIndex].challengerPerformance < theResult.challengerPerformance) {
                theBestFinalResults[bestIndex] = theResult;
            }
        }

        System.out.println("\nThe best parameters and corresponding performance against white parameters:\n" +
                theBestFinalResults[0].whiteParam + "\n"
        );
        for (int i = 0; i < theBestFinalResults.length; i++) {
            System.out.println("for parameter " + (i+1) + "\n" + theBestFinalResults[i] + "\n");
        }

        long end = System.currentTimeMillis();
        System.out.println("iteration took " + (end - start) / 1000 + " seconds");
    }
}
