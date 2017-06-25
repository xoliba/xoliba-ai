package Optimization;

import AI.ParametersAI;

import java.text.DecimalFormat;

/**
 * Created by vili on 25.6.2017.
 */
public class AIMatchResult {
    public double whiteVicP, blackVicP, sameColorWinsPercent, morePointsWhitePercent, howManyPointsGivenPerGame, percentOfChallengerWonMoreGames, challengerPerformance;
    int whiteLVL, blackLVL;
    RoundResult theFinalResult;
    public ParametersAI whiteParam, blackParam;

    /**
     * @param theFinalResult a round result where all the information from other rounds has been iteratively added
     * @param howManyGames was played in total
     */
    public AIMatchResult(RoundResult theFinalResult, int howManyGames, int whiteLVL, ParametersAI whiteParam, int blackLVL, ParametersAI blackParam) {
        this.whiteVicP = ((theFinalResult.whiteWins * 1.0) / (howManyGames * 1.0)) * 100;
        this.blackVicP = ((theFinalResult.blackWins * 1.0) / (howManyGames * 1.0)) * 100;
        this.sameColorWinsPercent = (theFinalResult.getTotalSameColorWins() * 1.0) / (howManyGames * 0.5) * 100;
        this.morePointsWhitePercent = (((theFinalResult.whitePoints - theFinalResult.blackPoints) * 1.0) / (theFinalResult.blackPoints * 1.0)) * 100;
        this.howManyPointsGivenPerGame = ((theFinalResult.whitePoints + theFinalResult.blackPoints) * 1.0 / howManyGames);
        this.percentOfChallengerWonMoreGames = (theFinalResult.getTotalChallangerWinsMoreGamesValue() * 1.0) / (howManyGames * 0.5) * 100;
        this.challengerPerformance = percentOfChallengerWonMoreGames - morePointsWhitePercent + blackVicP;
        this.whiteLVL = whiteLVL;
        this.blackLVL = blackLVL;
        this.theFinalResult = theFinalResult;
        this.whiteParam = whiteParam;
        this.blackParam = blackParam;
    }

    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        String s = "##################################################\n";
        s += "FINAL RESULT: white (lvl " + whiteLVL + ") - black (lvl " + blackLVL + ") " + theFinalResult.whitePoints + "-" + theFinalResult.blackPoints + ", (victories w-b " + theFinalResult.whiteWins + "-" + theFinalResult.blackWins + ")\n";
        s += "the champs params: " + whiteParam + "\n";
        s += "challenger params: " + blackParam + "\n\n";
        s += "\twhite wins " + formatter.format(whiteVicP) + "% of the games, black " + formatter.format(blackVicP) + "%\n"
                + "\twhite gets " + formatter.format(morePointsWhitePercent) + "% more points than black\n"
                + "\tpoints given per game " + formatter.format(howManyPointsGivenPerGame) + "\n" +
                "\tboth AIs won with same color " + formatter.format(sameColorWinsPercent) + "% of games\n" +
                "\tpercent of boards that the challenger (black) won more games than white " + formatter.format(percentOfChallengerWonMoreGames) + "%\n" +
                "\tchallenger performance: " + formatter.format(challengerPerformance) + "\n";
        s += "##################################################\n";
        return s;
    }

}
