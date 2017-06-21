package Optimization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import AI.*;
import Game.Board;
import Game.TurnData;

public class MatchMaker {

    int whiteDifficulty = 0;
    int blackDifficulty = 0;
    double[] whiteParameters;
    double[] blackParameters;
    int[][][] boards;

    public MatchMaker(int whiteDifficulty, double[] whiteParameters, int blackDifficulty, double[] blackParameters) {
        this.whiteDifficulty = whiteDifficulty;
        this.blackDifficulty = blackDifficulty;
        this.whiteParameters = whiteParameters;
        this.blackParameters = blackParameters;
        this.boards = getBoards();
    }

    public double calculate(int amount) {
        AI aiWhite;
        AI aiBlack;
        int whiteWons = 0;
        int blackWons = 0;
        int result;

        for(int i=0; i<amount && i<boards.length; i++) {
            aiWhite = new AI(1, whiteDifficulty, whiteParameters);
            aiBlack = new AI(-1, blackDifficulty, blackParameters);
            if(startingTurn(boards[i]) > 0) {
                result = new Board(getRoundResultBoard(aiBlack, aiWhite, boards[i])).calculatePoints();
            } else {
                result = new Board(getRoundResultBoard(aiWhite, aiBlack, boards[i])).calculatePoints();
            }
            if(result < 0) blackWons += result;
            else whiteWons += result;

            //the same board but players switched. Little bit of copy-paste here :)
            aiWhite = new AI(-1, whiteDifficulty, whiteParameters);
            aiBlack = new AI(1, blackDifficulty, blackParameters);
            if(startingTurn(boards[i]) > 0) {
                result = new Board(getRoundResultBoard(aiWhite, aiBlack, boards[i])).calculatePoints();
            } else {
                result = new Board(getRoundResultBoard(aiBlack, aiWhite, boards[i])).calculatePoints();
            }
            if(result < 0) whiteWons += result;
            else blackWons += result;
            System.out.println((i+1) + ". match result: white - black " + whiteWons + "-" + blackWons);
        }

        if(whiteWons > blackWons) {
            System.out.println("### FINAL RESULT: white - black " + whiteWons + "-" + blackWons + " (" + ((whiteWons * 1.0) / (blackWons * 1.0) - 1.0) + ")");
            return (whiteWons * 1.0) / (blackWons * 1.0) - 1.0;
        } else {
            System.out.println("### FINAL RESULT: white - black " + whiteWons + "-" + blackWons + " (-" + ((blackWons * 1.0) / (whiteWons * 1.0) - 1.0) + ")");
            return -((blackWons * 1.0) / (whiteWons * 1.0) - 1.0);
        }
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

    private int startingTurn(int[][] board) {
        int sTurn = board[0][1] + board[0][5] + board[1][0] + board[1][6] + board[5][0] + board[5][6] + board[6][1] + board[6][5];

        if (sTurn == 0) {
            for (int i = 1; i < 6; i++) {
                sTurn += board[i][0];
                sTurn += board[i][6];
                sTurn += board[0][i];
                sTurn += board[6][i];
            }
        }

        return sTurn;
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
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();              
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return boards;
    }
}