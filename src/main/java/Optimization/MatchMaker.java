package Optimization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import AI.*;
import Game.Board;
import Game.TurnData;

public class MatchMaker {

    int whiteDifficulty = 1;
    int blackDifficulty = 1;
    ParametersAI whiteParameters;
    ParametersAI blackParameters;
    int[][][] boards;

    public MatchMaker(int whiteDifficulty, ParametersAI whiteParameters, int blackDifficulty, ParametersAI blackParameters) {
        this.whiteDifficulty = whiteDifficulty;
        this.blackDifficulty = blackDifficulty;
        this.whiteParameters = whiteParameters;
        this.blackParameters = blackParameters;
        this.boards = getBoards();
    }

    /**
     *
     * @param howManyGames
     * @return how many percents white got more points than black
     */
    public double calculate(int howManyGames) {
        ArrayList<RoundResult> results = new ArrayList<>();
        RoundResult finalResult = new RoundResult();
        for(int i=0; i<howManyGames && i<boards.length; i++) {
            RoundResult rr = calculateRoundForBothRoles(boards[i]);
            results.add(rr);
            finalResult.add(rr);
            System.out.println((i+1) + ".\tround result: " + rr);
        }

        return parseResult(finalResult.whitePoints, finalResult.whiteWins, finalResult.blackPoints, finalResult.blackWins, finalResult.getTotalSameColorWins(), 2*howManyGames);
    }

    public RoundResult calculateRoundForBothRoles(int[][] board) {
        AI aiWhite, aiBlack;
        RoundResult rr = new RoundResult();

        aiWhite = new AI(1, whiteDifficulty, whiteParameters);
        aiBlack = new AI(-1, blackDifficulty, blackParameters);
        int result = calculateRound(aiWhite, aiBlack, board);
        if(result > 0) {
            rr.whitePoints += result;
            rr.whiteWins++;
        } else  {
            rr.blackPoints += result;
            rr.blackWins++;
        }
        aiWhite = new AI(-1, whiteDifficulty, whiteParameters);
        aiBlack = new AI(1, blackDifficulty, blackParameters);
        result = calculateRound(aiBlack, aiWhite, board);
        if(result > 0) {
            rr.blackPoints += result;
            rr.blackWins++;
        } else  {
            rr.whitePoints += result;
            rr.whiteWins++;
        }

        if (rr.whiteWins == rr.blackWins && rr.whiteWins == 1)
            rr.bothWinWithSameColor = true;

        return rr;
    }

    public int calculateRound(AI red, AI blue, int[][] board) {
        Board endSituation;
        if (Board.redStartsGame(board)) {
            endSituation = new Board(getRoundResultBoard(red, blue, board));
        } else {
            endSituation = new Board(getRoundResultBoard(blue, red, board));
        }
        int result = endSituation.calculatePoints();
        return result;
    }

    private double parseResult(int whiteWinsPoints, int whiteVictories, int blackWinsPoints, int blackVictories, int howManySameColorWins, int howManyGames) {
        double whiteVicP = ((whiteVictories*1.0)/(howManyGames*1.0)) * 100;
        double blackVicP = ((blackVictories*1.0)/(howManyGames*1.0)) * 100;
        double sameColorWinsPercent = (howManySameColorWins*1.0)/(howManyGames*0.5) * 100;
        double morePointsWhitePercent = (((whiteWinsPoints-blackWinsPoints)*1.0)/(blackWinsPoints*1.0)) * 100;
        double howManyPointsGivenPerGame = ((whiteWinsPoints+blackWinsPoints)*1.0/howManyGames);
        DecimalFormat formatter = new DecimalFormat("#0.00");
        System.out.println("### FINAL RESULT: white (lvl " + whiteDifficulty + ") - black (lvl " + blackDifficulty + ") " + whiteWinsPoints + "-" + blackWinsPoints + ", (victories w-b " + whiteVictories + "-" + blackVictories + ")\n"
                + "\twhite wins " + formatter.format(whiteVicP) + "% of the games, black " + formatter.format(blackVicP) + "%\n"
                + "\twhite gets " + formatter.format(morePointsWhitePercent) + "% more points than black\n"
                + "\tpoints given per game " + formatter.format(howManyPointsGivenPerGame) + "\n" +
                "\tboth AIs won with same color " + formatter.format(sameColorWinsPercent) + "% of games");
        return morePointsWhitePercent;

    }

    //Starting AI first.
    private int[][] getRoundResultBoard(AI aiWhite, AI aiBlack, int[][] board) {
        TurnData result = null;
        TurnData oldResult = aiWhite.move(board);
        int turnsWithoutStonesHit = 0;

        //Right now max moves is set to 100.
        for(int i=0; i<100; i++) {
            result = aiWhite.move(board);
            if(result.didMove == false) {
                result = aiBlack.move(board);
                if(result.didMove == false) {
                    //Either one cant move
                    return result.board;
                }
                result = aiWhite.move(board);
                if(result.didMove == false) {
                    return result.board;
                }
            }

            result = aiBlack.move(board);
            if(new Board(oldResult.board).hashCode() == new Board(result.board).hashCode()) {
                //There have been same board layout in the past: so the result wont change.
                //So now we assume AI will do the same move with the same board every time.
                return result.board;
            }
            oldResult = result;
            if(result.didMove == false) {
                result = aiWhite.move(board);
                if(result.didMove == false) {
                    //Either one cant move
                    return result.board;
                }
                result = aiBlack.move(board);
                if(result.didMove == false) {
                    return result.board;
                }
            }
        }
        System.out.print("Game stopped: too many rounds. ");
        return result.board;
    }

    private int[][][] getBoards() {
        int[][][] boards = new int[10000][7][7];
        String line = null;

        try {
            InputStream in = MatchMaker.class.getResourceAsStream("/boards.txt");
            BufferedReader bufferedReader =  new BufferedReader(new InputStreamReader(in));

            int i=0;
            int row=0;
            String[] numbers;
            while(i<10000 && (line = bufferedReader.readLine()) != null) {
                if(row < 7) {
                    numbers = line.split(" ");
                    for(int j=0; j<numbers.length; j++) {
                        boards[i][row][j] = Integer.parseInt(numbers[j]);
                    }
                    row++;
                } else if(row < 8) {
                    row++;
                } else {
                    row = 0;
                    i++;
                }
            }

            bufferedReader.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return boards;
    }
}