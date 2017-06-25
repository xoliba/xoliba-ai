package Optimization;

import Game.RoundRecord;

import java.util.ArrayList;

/**
 * Created by vili on 21.6.2017.
 */
public class RoundResult {

    public int roundNo;
    public int whiteWins, blackWins, whitePoints, blackPoints;
    public boolean bothWinWithSameColor;
    private RoundRecord[] bothRounds;
    private int howManyTimesBothAIsWonWithSameColor = 0;
    private int howManyTimesChallengerWonMoreGames = 0;
    private ArrayList<RoundRecord[]> allRounds = new ArrayList<>();

    public RoundResult(int roundNo, int whiteWins, int blackWins, int whitePoints, int blackPoints) {
        this.roundNo = roundNo;
        this.whiteWins = whiteWins;
        this.blackWins = blackWins;
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        this.bothRounds = new RoundRecord[2];
    }

    public RoundResult() {
        this(-1,0,0,0,0);
    }

    /**
     * supposed to be called after all points and wins are set
     */
    public void updateStats() {
        if (whiteWins == blackWins && whiteWins == 1) {
            bothWinWithSameColor = true;
            howManyTimesBothAIsWonWithSameColor = 1;
        }
        if (blackWins > whiteWins) {
            howManyTimesChallengerWonMoreGames = 1;
        }
    }

    public void addRoundRecord(RoundRecord rr, boolean first) {
        if (first)
            bothRounds[0] = rr;
        else
            bothRounds[1] = rr;
    }

    public int getTotalSameColorWins() {
        return howManyTimesBothAIsWonWithSameColor;
    }

    public int getTotalChallangerWinsMoreGamesValue() {
        return howManyTimesChallengerWonMoreGames;
    }

    public void add(RoundResult rr, boolean keepRecord) {
        this.whiteWins += rr.whiteWins;
        this.blackWins += rr.blackWins;
        this.whitePoints += rr.whitePoints;
        this.blackPoints += rr.blackPoints;
        if (rr.bothWinWithSameColor)
            this.howManyTimesBothAIsWonWithSameColor++;
        this.howManyTimesChallengerWonMoreGames += rr.howManyTimesChallengerWonMoreGames;
        if (keepRecord) {
            this.allRounds.add(rr.bothRounds);
        }
    }

    public ArrayList<RoundRecord[]> getAllRounds() {
        return allRounds;
    }

    @Override
    public String toString() {
        //String s = roundNo + "." + (roundNo < 100 ? "\t" : "") + "\t"; //works better for some consoles
        String s = roundNo + ".\t";
        s += "white " + whiteWins + "w:" + whitePoints + "p\t- black "  + blackWins + "w:"  + blackPoints + "p\t";
        if (bothWinWithSameColor && blackPoints > whitePoints)
            s += " both won, but black got more points!\t";
        if (howManyTimesChallengerWonMoreGames != 0)
            s += " Black won more games than white!\t";
        return s;
    }

    public String endGameMessagesToString() {
        String s = "";
        for (int i = 0; i < bothRounds.length; i++) {
            s += "\t" + (i+1) + ". round " + bothRounds[i].endGameMessage + "\n";
        }
        return s;
    }
}
