package Optimization;

import AI.ParametersAI;
import Game.RoundRecord;
import Game.TurnData;

import java.util.ArrayList;
import java.util.List;

import static AI.AI.bestParameters;

/**
 * Created by vili on 25.6.2017.
 */
public class Coach {

    private boolean keepRecord = false;
    private double[] executionEstimations = new double[] {
            250, 12.5, 1.8, 0.10, 0.018, 1, 1
    }; //how many rounds per second we compute (on my machine) if we iterate two AIs of lvl n (n = 1, 2, 3,..)


    public Coach(boolean keepRecord) {
        this.keepRecord = keepRecord;
    }

    public AIMatchResult runOneShowdown(int whiteLVL, ParametersAI whiteParam, int blackLVL, ParametersAI blackParam, int howManyBoards) {
        long start = System.currentTimeMillis();
        System.out.println(getEstimationOfProcessLength(2 * howManyBoards, whiteLVL, blackLVL));
        MatchMaker referee = new MatchMaker(whiteLVL, whiteParam, blackLVL, blackParam,false, false);
        AIMatchResult match = referee.calculate(howManyBoards, keepRecord);
        System.out.println(match);
        System.out.println(howLongItTook(start, 2 * howManyBoards) + "\n");

        if (keepRecord) {
            RoundResult finalResult = match.theFinalResult;
            for (RoundRecord[] bothRounds:finalResult.getAllRounds()) {
                walkThroughRound(bothRounds);
            }
        }
        return match;
    }

    private void walkThroughRound(RoundRecord[] records) {
        for (RoundRecord record:records) {
            System.out.println(record);
            ArrayList<TurnData> turns = record.getTurns();
            for (int i = 0; i < turns.size(); i++) {
                System.out.println(turns.get(i));
            }
        }
    }

    public void iterateWithDifferentParameters(boolean[] whichParametersToIterate, int minWeight, int maxWeight, double frequency, int howManyBoards, int whiteLVL, int blackLVL) {
        long start = System.currentTimeMillis();
        int rounds = countHowManyRoundsToBePlayed(whichParametersToIterate, minWeight, maxWeight, frequency, howManyBoards);
        System.out.println(getEstimationOfProcessLength(rounds, whiteLVL, blackLVL));

        ParameterWriter pw = new ParameterWriter(minWeight, maxWeight, frequency);
        pw.writeNewFileWithParameterValues();
        System.out.println("generating and writing parameters into a file took " + (System.currentTimeMillis() - start) + " ms\n");

        computeWithAllParameterCombinations(pw, whichParametersToIterate, howManyBoards, whiteLVL, blackLVL);
        System.out.println(howLongItTook(start, rounds));
    }

    private void computeWithAllParameterCombinations(ParameterWriter pw, boolean[] whichParametersToIterate , int howManyBoards, int whiteDifficulty, int blackDifficulty) {
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
            if (!whichParametersToIterate[bestIndex]) {
                continue;
            }

            ParametersAI p = parameterCombinations.get(i);
            System.out.println( "  %%% New gladiators in the pit! %%%\n" +
                            "Default parameters on white: " + bestParameters + "\n" +
                            "versus black with test param " + p
                    //+ "\n"
            );

            MatchMaker referee = new MatchMaker(whiteDifficulty, bestParameters, blackDifficulty, p, keepRecord, false);
            AIMatchResult theResult = referee.calculate(howManyBoards);
            System.out.println("challengers performance: " + theResult.challengerPerformance + "\n");
            System.out.println(theResult);

            if (theBestFinalResults[bestIndex] == null || theBestFinalResults[bestIndex].challengerPerformance < theResult.challengerPerformance) {
                theBestFinalResults[bestIndex] = theResult;
            }
        }

        System.out.println("\nThe best parameters and corresponding performance:\n");
        for (int i = 0; i < theBestFinalResults.length; i++) {
            if (theBestFinalResults[i] == null) continue;
            System.out.println("for parameter " + (i+1) + "\n" + theBestFinalResults[i] + "\n");
        }

    }

    private int countHowManyRoundsToBePlayed(boolean[] whichParametersToIterate, int minWeight, int maxWeight, double frequency, int howManyBoards) {
        int countOfParam = 0;
        for (int i = 0; i < whichParametersToIterate.length; i++) {
            if (whichParametersToIterate[i]) countOfParam++;
        }
        double parameterValues = (maxWeight - minWeight) / frequency;
        if (parameterValues - Math.floor(parameterValues) <= 0) parameterValues++; //min 0 max 1 fre 0.5 results 3 param combinations, not just 2
        else parameterValues = Math.ceil(parameterValues);
        return (int) parameterValues * countOfParam * howManyBoards * 2;
    }

    private String getEstimationOfProcessLength(int rounds, int whiteLVL, int blackLVL) {
        double estimation = rounds * 1.0 / 2 / executionEstimations[whiteLVL - 1];
        estimation += rounds * 1.0 / 2 / executionEstimations[blackLVL - 1];
        String s = "Lets play " + rounds + " rounds in total with AI lvl " + whiteLVL + " (w) " + blackLVL + " (b)!\n" +
                "It might take around " + (int) estimation + " sec to compute all the rounds...\n";
        return s;
    }

    private String howLongItTook(long start, int rounds) {
        long end = System.currentTimeMillis();
        int seconds = (int) (end - start) / 1000;
        String s = "iteration took " + seconds + " seconds\n";
        s += "played " + rounds + " rounds in total, " + (rounds*1.0/seconds*1.0) + " r/s";
        return s;

    }
}
