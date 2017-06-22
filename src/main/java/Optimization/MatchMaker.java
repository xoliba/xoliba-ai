package Optimization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import AI.*;
import Game.Board;
import Game.TurnData;

public class MatchMaker {

    int whiteDifficulty = 1;
    int blackDifficulty = 1;
    ParametersAI whiteParameters; //the champ
    ParametersAI blackParameters; //the challenger
    int[][][] boards;
    boolean print = false;

    public MatchMaker(int whiteDifficulty, ParametersAI whiteParameters, int blackDifficulty, ParametersAI blackParameters, boolean print) {
        this(whiteDifficulty, whiteParameters, blackDifficulty, blackParameters);
        this.print = print;
    }

    public MatchMaker(int whiteDifficulty, ParametersAI whiteParameters, int blackDifficulty, ParametersAI blackParameters) {
        this.whiteDifficulty = whiteDifficulty;
        this.blackDifficulty = blackDifficulty;
        this.whiteParameters = whiteParameters;
        this.blackParameters = blackParameters;
        this.boards = getBoards();
    }

    /**
     *
     * @param howManyBoards
     * @return how many percents of games black (the challenger) won both games
     */
    public double calculate(int howManyBoards) {
        ArrayList<RoundResult> results = new ArrayList<>();
        RoundResult finalResult = new RoundResult();
		
		Semaphore finished = new Semaphore(howManyBoards < boards.length ? 1 - howManyBoards : 1 - boards.length); // :P
		Semaphore threadCount = new Semaphore(8);
		Semaphore mutex = new Semaphore(1);
		
        for(int i=0; i<howManyBoards && i<boards.length; i++) {
			int j = i;		// yes, this is necessary
			
			Thread matchThread = new Thread(() -> {
				acquire(threadCount);
				
				RoundResult rr = calculateRoundForBothRoles(boards[j]);
				
				acquire(mutex);
				results.add(rr);
				finalResult.add(rr);
				if (print)
					System.out.println((j+1) + ".\tround result: " + rr);
				
				mutex.release();
				threadCount.release();
				finished.release();
			});
			
			matchThread.start();
        }
		
		acquire(finished);
		
		return parseResult(finalResult, 2*howManyBoards);
    }
	
	private static void acquire(Semaphore semaphore) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted");
		}
	}

    public RoundResult calculateRoundForBothRoles(int[][] board) {
        AI aiWhite, aiBlack;
        RoundResult rr = new RoundResult();

        aiWhite = new AI(1, whiteDifficulty, whiteParameters);
        aiBlack = new AI(-1, blackDifficulty, blackParameters);
        int result = calculateRound(aiWhite, aiBlack, board);
        if (result > 0) {
            rr.whitePoints += result;
            rr.whiteWins++;
        } else if (result < 0) {
            rr.blackPoints -= result;
            rr.blackWins++;
        }
        aiWhite = new AI(-1, whiteDifficulty, whiteParameters);
        aiBlack = new AI(1, blackDifficulty, blackParameters);
        result = calculateRound(aiBlack, aiWhite, board);
        if(result > 0) {
            rr.blackPoints += result;
            rr.blackWins++;
        } else if (result < 0) {
            rr.whitePoints -= result;
            rr.whiteWins++;
        }

        rr.updateStats();

        return rr;
    }

    public int calculateRound(AI red, AI blue, int[][] board) {
        Board endSituation;
        if (Board.redStartsGame(board)) {
            endSituation = new Board(playUntilRoundEnded(red, blue, board));
        } else {
            endSituation = new Board(playUntilRoundEnded(blue, red, board));
        }
        int result = endSituation.calculatePoints();
        return result;
    }

    /**
     * @param theFinalResult a round result where all the information from other rounds has been iteratively added
     * @param howManyGames was played in total
     * @return
     */
    private double parseResult(RoundResult theFinalResult, int howManyGames) {
        double whiteVicP = ((theFinalResult.whiteWins*1.0)/(howManyGames*1.0)) * 100;
        double blackVicP = ((theFinalResult.blackWins*1.0)/(howManyGames*1.0)) * 100;
        double sameColorWinsPercent = (theFinalResult.getTotalSameColorWins()*1.0)/(howManyGames*0.5) * 100;
        double morePointsWhitePercent = (((theFinalResult.whitePoints-theFinalResult.blackPoints)*1.0)/(theFinalResult.blackPoints*1.0)) * 100;
        double howManyPointsGivenPerGame = ((theFinalResult.whitePoints+theFinalResult.blackPoints)*1.0/howManyGames);
        double percentOfChallengerWonBothGames = (theFinalResult.getTotalChallangerWinsBothGamesValue()*1.0)/(howManyGames*0.5) * 100;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        System.out.println("### FINAL RESULT: white (lvl " + whiteDifficulty + ") - black (lvl " + blackDifficulty + ") " + theFinalResult.whitePoints + "-" + theFinalResult.blackPoints + ", (victories w-b " + theFinalResult.whiteWins + "-" + theFinalResult.blackWins + ")\n"
                + "\twhite wins " + formatter.format(whiteVicP) + "% of the games, black " + formatter.format(blackVicP) + "%\n"
                + "\twhite gets " + formatter.format(morePointsWhitePercent) + "% more points than black\n"
                + "\tpoints given per game " + formatter.format(howManyPointsGivenPerGame) + "\n" +
                "\tboth AIs won with same color " + formatter.format(sameColorWinsPercent) + "% of games\n" +
                "\tpercent of boards that the challenger (black) won both games " + formatter.format(percentOfChallengerWonBothGames) + "%\n");
        return percentOfChallengerWonBothGames;
    }

    //TODO this is broken
    public int[][] playUntilRoundEnded(AI firstAI, AI secondAI, int[][] board) {
        int turnsWithoutStonesHit = 0;
        AI[] ai = new AI[]{firstAI, secondAI};

        //Right now max moves is set to 100.
        TurnData result = ai[0].move(board);
        TurnData oldResult = result;
        for(int i=1; i<100; i++) {
            result = ai[i%2].move(board);
            if(result.didMove == false) {
                turnsWithoutStonesHit+=2;
                if(turnsWithoutStonesHit >= 3) {
                    return result.board;
                }
            } else if(turnsWithoutStonesHit > 0) {
                turnsWithoutStonesHit--;
            }
            //Lets check every second turn if AIs are in a loop.
            //Btw changing this to 0 will give slightly different points.
            if(i%4 == 1) {
                if(new Board(oldResult.board).hashCode() == new Board(result.board).hashCode()) {
                    //There have been same board layout in the past: so the result wont change.
                    //So now we assume AI will do the same move with the same board every time.
                    return result.board;
                }
                oldResult = result;
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