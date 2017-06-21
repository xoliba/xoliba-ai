package Optimization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

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

    public double calculate(int amount) {
        AI aiWhite;
        AI aiBlack;
        int whiteWinsPoints = 0;
        int whiteVictories = 0;
        int blackWinsPoints = 0;
        int blackVictories = 0;
        int result;

        for(int i=0; i<amount && i<boards.length; i++) {
            aiWhite = new AI(1, whiteDifficulty, whiteParameters);
            aiBlack = new AI(-1, blackDifficulty, blackParameters);
            if(Board.redStartsGame(boards[i])) {
                result = new Board(getRoundResultBoard(aiWhite, aiBlack, boards[i])).calculatePoints();
            } else {
                result = new Board(getRoundResultBoard(aiBlack, aiWhite, boards[i])).calculatePoints();
            }
            if(result < 0) {
                blackWinsPoints += result;
                blackVictories++;
            } else  {
                whiteWinsPoints += result;
                whiteVictories++;
            }

            //the same board but players switched. Little bit of copy-paste here :)
            aiWhite = new AI(-1, whiteDifficulty, whiteParameters);
            aiBlack = new AI(1, blackDifficulty, blackParameters);
            if(Board.redStartsGame(boards[i])) {
                result = new Board(getRoundResultBoard(aiBlack, aiWhite, boards[i])).calculatePoints();
            } else {
                result = new Board(getRoundResultBoard(aiWhite, aiBlack, boards[i])).calculatePoints();
            }
            if(result < 0) {
                whiteWinsPoints += result;
                whiteVictories++;
            }
            else {
                blackWinsPoints += result;
                blackVictories++;
            }
            System.out.println((i+1) + ". match result: white - black " + whiteWinsPoints + "-" + blackWinsPoints);
        }

        return parseResult(whiteWinsPoints, whiteVictories, blackWinsPoints, blackVictories, 2*amount);
    }

    private double parseResult(int whiteWinsPoints, int whiteVictories, int blackWinsPoints, int blackVictories, int howManyGames) {
        double whiteVicP = ((whiteVictories*1.0)/(howManyGames*1.0)) * 100;
        double blackVicP = ((blackVictories*1.0)/(howManyGames*1.0)) * 100;
        double morePointsWhitePercent = (((whiteWinsPoints-blackWinsPoints)*1.0)/(blackWinsPoints*1.0)) * 100;
        double howManyPointsGivenPerGame = ((whiteWinsPoints+blackWinsPoints)*1.0/howManyGames);
        DecimalFormat formatter = new DecimalFormat("#0.00");
        System.out.println("### FINAL RESULT: white (lvl " + whiteDifficulty + ") - black (lvl " + blackDifficulty + ") " + whiteWinsPoints + "-" + blackWinsPoints + ", (victories w-b " + whiteVictories + "-" + blackVictories + ")\n"
                + "\twhite wins " + formatter.format(whiteVicP) + "% of the games, black " + formatter.format(blackVicP) + "%\n"
                + "\twhite gets " + formatter.format(morePointsWhitePercent) + "% more points than black\n"
                + "\tpoints given per game " + formatter.format(howManyPointsGivenPerGame));
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