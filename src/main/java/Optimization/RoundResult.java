package Optimization;

/**
 * Created by vili on 21.6.2017.
 */
public class RoundResult {

    public int whiteWins, blackWins, whitePoints, blackPoints;
    public boolean bothWinWithSameColor;
    private int howManyTimesBothAIsWonWithSameColor = 0;
    private int howManyTimesChallengerWonBothGames;

    public RoundResult(int whiteWins, int blackWins, int whitePoints, int blackPoints) {
        this.whiteWins = whiteWins;
        this.blackWins = blackWins;
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
    }

    public RoundResult() {
        this(0,0,0,0);
    }

    /**
     * supposed to be called after all points and wins are set
     */
    public void updateStats() {
        if (whiteWins == blackWins && whiteWins == 1) {
            bothWinWithSameColor = true;
            howManyTimesBothAIsWonWithSameColor++;
        }
        if (blackWins == 2) {
            howManyTimesChallengerWonBothGames++;
        }
    }

    public int getTotalSameColorWins() {
        return howManyTimesBothAIsWonWithSameColor;
    }

    public int getTotalChallangerWinsBothGamesValue() {
        return howManyTimesChallengerWonBothGames;
    }

    public void add(RoundResult rr) {
        this.whiteWins += rr.whiteWins;
        this.blackWins += rr.blackWins;
        this.whitePoints += rr.whitePoints;
        this.blackPoints += rr.blackPoints;
        if (rr.bothWinWithSameColor)
            this.howManyTimesBothAIsWonWithSameColor++;
        this.howManyTimesChallengerWonBothGames += rr.howManyTimesChallengerWonBothGames;
    }

    @Override
    public String toString() {
        String s = "white " + whiteWins + "w:" + whitePoints + "p\t- black "  + blackWins + "w:"  + blackPoints + "p\t";
        if (bothWinWithSameColor)
            s += " both AI's won with same color\t";
        if (blackWins == 2)
            s += "Black wins both games! SOS";
        return s;
    }
}
