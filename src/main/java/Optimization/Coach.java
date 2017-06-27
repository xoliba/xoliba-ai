package Optimization;

import AI.ParametersAI;
import Game.RoundRecord;
import Game.TurnData;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static AI.AI.bestParameters;

/**
 * This class is the coach: it will fight different AIs with different parameters.
 * If you are rying to find where the single matches happen, try MatchMaker-class.
 */
public class Coach {
    
    public static final Logger logger = LogManager.getLogger(Optimization.Coach.class.getName());    
    private boolean keepRecord = false;

    public Coach(boolean keepRecord) {
        this.keepRecord = keepRecord;
    }

    public AIMatchResult runOneShowdown(int whiteLVL, ParametersAI whiteParam, int blackLVL, ParametersAI blackParam, int howManyBoards) {
        long start = System.currentTimeMillis();
        System.out.println(getEstimationOfProcessLength(2 * howManyBoards, whiteLVL, blackLVL));
        MatchMaker referee = new MatchMaker(whiteLVL, whiteParam, blackLVL, blackParam, keepRecord, false);
        AIMatchResult match = referee.calculate(howManyBoards, keepRecord);
        logger.info(match.toString());
        logger.info(howLongItTook(start, 2 * howManyBoards) + "\n");

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
            logger.info(record.toString());
            ArrayList<TurnData> turns = record.getTurns();
            for (int i = 0; i < turns.size(); i++) {
                logger.info(turns.get(i).toString());
            }
        }
    }

    public void iterateWithDifferentParameters(boolean[] whichParametersToIterate, int minWeight, int maxWeight, double frequency, int howManyBoards, int whiteLVL, int blackLVL) {
        long start = System.currentTimeMillis();
        int rounds = countHowManyRoundsToBePlayed(whichParametersToIterate, minWeight, maxWeight, frequency, howManyBoards);
        logger.info(getEstimationOfProcessLength(rounds, whiteLVL, blackLVL));

        ParameterWriter pw = new ParameterWriter(minWeight, maxWeight, frequency);
        pw.writeNewFileWithParameterValues();
        logger.trace("generating and writing parameters into a file took " + (System.currentTimeMillis() - start) + " ms\n");

        computeWithAllParameterCombinations(pw, whichParametersToIterate, howManyBoards, whiteLVL, blackLVL);
        logger.info(howLongItTook(start, rounds));
    }

    private void computeWithAllParameterCombinations(ParameterWriter pw, boolean[] whichParametersToIterate,
                                                     int howManyBoards, int whiteDifficulty, int blackDifficulty) {
        List<ParametersAI> parameterCombinations = pw.readParameterCombinations();
        //int combinations = parameterCombinations.size();    //Is this more confusing than the original?
        int paramCount = bestParameters.toArray().length;
        AIMatchResult[] theBestFinalResults = new AIMatchResult[paramCount];
        int bestIndex = 0;
        for (int i = 0; i < parameterCombinations.size(); i++) {
            if (i != 0 && i % (parameterCombinations.size() / paramCount) == 0) {
                logger.info("\n¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ START TESTING WITH NEW PARAMETER ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\n");
                bestIndex++;
            }
            if (!whichParametersToIterate[bestIndex]) {
                continue;
            }

            ParametersAI p = parameterCombinations.get(i);
            logger.info( "  %%% New gladiators in the pit! %%%\n" +
                            "Default parameters on white: " + bestParameters + "\n" +
                            "versus black with test param " + p
                    //+ "\n"
            );

            MatchMaker referee = new MatchMaker(whiteDifficulty, bestParameters, blackDifficulty, p, keepRecord, false);
            AIMatchResult theResult = referee.calculate(howManyBoards);
            logger.info("challengers performance: " + theResult.challengerPerformance + "\n");
            logger.info(theResult.toString());

            if (theBestFinalResults[bestIndex] == null || theBestFinalResults[bestIndex].challengerPerformance < theResult.challengerPerformance) {
                theBestFinalResults[bestIndex] = theResult;
            }
        }

        logger.info("\nThe best parameters and corresponding performance:\n");
        for (int i = 0; i < theBestFinalResults.length; i++) {
            if (theBestFinalResults[i] == null) continue;
            logger.info("for parameter " + (i+1) + "\n" + theBestFinalResults[i] + "\n");
        }

    }

    /**
     * This class will count how many rounds we have to play in order to find best values.
     * So with the return value we can estimate how long the calculation is going to take,
     * changing this wont affect the execution.
     */
    private int countHowManyRoundsToBePlayed(boolean[] whichParametersToIterate, int minWeight,
                                            int maxWeight, double frequency, int howManyBoards) {
        int countOfParam = 0;
        for (int i = 0; i < whichParametersToIterate.length; i++) {
            if (whichParametersToIterate[i]) countOfParam++;
        }
        double parameterValues = (maxWeight - minWeight) / frequency;
        if (parameterValues - Math.floor(parameterValues) <= 0) parameterValues++; //min 0 max 1 fre 0.5 results 3 param combinations, not just 2
        else parameterValues = Math.ceil(parameterValues);
        return (int) parameterValues * countOfParam * howManyBoards * 2;
    }

    /**
     * Get running time estimation related stats as a String.
     */
    private String getEstimationOfProcessLength(int rounds, int whiteLVL, int blackLVL) {
        double[] executionEstimations = new double[] {
                250, 12.5, 2.7, 0.10, 0.018, 1, 1
        }; //how many rounds per second we compute (on my machine) if we iterate two AIs of lvl n (n = 1, 2, 3,..)
        double estimation = rounds * 1.0 / 2 / executionEstimations[whiteLVL - 1];
        estimation += rounds * 1.0 / 2 / executionEstimations[blackLVL - 1];
        String s = "Lets play " + rounds + " rounds in total with AI lvl " + whiteLVL + " (w) " + blackLVL + " (b)!\n" +
                "It might take around " + (int) estimation + " sec to compute all the rounds...\n";
        return s;
    }

    /**
     * Get running time related stats as a String.
     */
    private String howLongItTook(long start, int rounds) {
        long end = System.currentTimeMillis();
        int seconds = (int) (end - start) / 1000;
        String s = "iteration took " + seconds + " seconds\n";
        s += "played " + rounds + " rounds in total, " + (rounds*1.0/seconds*1.0) + " r/s";
        return s;

    }
}
