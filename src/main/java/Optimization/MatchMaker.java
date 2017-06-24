package Optimization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

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
    boolean runSingleThread = false;

    public MatchMaker(int whiteDifficulty, ParametersAI whiteParameters, int blackDifficulty, ParametersAI blackParameters, boolean print, boolean runSingleThread) {
        this(whiteDifficulty, whiteParameters, blackDifficulty, blackParameters);
        this.print = print;
        this.runSingleThread = runSingleThread;
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
     * @return a single double that describes how well the challenger performed; the bigger the better (for the challenger)
     */
    public double calculate(int howManyBoards) {
        //ArrayList<RoundResult> results = new ArrayList<>();
        RoundResult finalResult = new RoundResult();
		
		Semaphore finished = new Semaphore(howManyBoards < boards.length ? 1 - howManyBoards : 1 - boards.length); // :P
		Semaphore threadCount = new Semaphore(8);
		Semaphore mutex = new Semaphore(1);
		
        for(int i=0; i<howManyBoards && i<boards.length; i++) {
			int j = i;		// yes, this is necessary

            if (runSingleThread) {
                RoundResult rr = calculateRoundForBothRoles(boards[j]);
                rr.roundNo = j+1;
                if (print)
                    System.out.println(rr + "\n" + rr.endGameMessagesToString());
                finalResult.add(rr);
            } else {

                Thread matchThread = new Thread(() -> {
                    acquire(threadCount);

                    RoundResult rr = calculateRoundForBothRoles(boards[j]);
                    rr.roundNo = j+1;

                    acquire(mutex);
                    //results.add(rr);
                    finalResult.add(rr);
                    if (print)
                        System.out.println(rr);
                    mutex.release();
                    threadCount.release();
                    finished.release();
                });

			    matchThread.start();
            }
        }

        if (!runSingleThread) {
		    acquire(finished);
        }

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
        int result = calculateRound(aiWhite, aiBlack, board, rr);
        if (result > 0) {
            rr.whitePoints += result;
            rr.whiteWins++;
        } else if (result < 0) {
            rr.blackPoints -= result;
            rr.blackWins++;
        }
        aiWhite = new AI(-1, whiteDifficulty, whiteParameters);
        aiBlack = new AI(1, blackDifficulty, blackParameters);
        result = calculateRound(aiBlack, aiWhite, board, rr);
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

    public int calculateRound(AI red, AI blue, int[][] board, RoundResult rr) {
        Board endSituation;
        if (Board.redStartsGame(board)) {
            endSituation = new Board(playUntilRoundEnded(red, blue, board, rr));
        } else {
            endSituation = new Board(playUntilRoundEnded(blue, red, board, rr));
        }
        int result = endSituation.calculatePoints();
        return result;
    }

    //TODO this is broken, needs testing
    public int[][] playUntilRoundEnded(AI firstAI, AI secondAI, int[][] board, RoundResult rr) {
        int turnsWithoutMoving = 0;
        AI[] ai = new AI[]{firstAI, secondAI};
        //System.out.println("play until round ends, board in the beginning\n" + new Board(board));
        //Right now max moves is set to 100, which is 50 turns
        TurnData result = ai[0].move(board, ai[0].color, ai[0].getDifficulty(), 0);
        TurnData oldResult = result;
        for(int i=1; i<100; i++) {
            //System.out.println("top of the loop, old result:\n" + oldResult);
            AI acting = ai[i%2];
            result = acting.move(oldResult.board, acting.color, acting.getDifficulty(), oldResult.withoutHit);
            //System.out.println("ai no. " + i%2 + " did a move (result):\n" + result);
            turnsWithoutMoving = updateGameEndingIndicators(result, turnsWithoutMoving);
            if (turnsWithoutMoving > 2 || result.withoutHit == 30) {
                rr.addEndGameMessage(parseGameEndedMessage(i%2 == 0, acting.color, turnsWithoutMoving, result.withoutHit));
                return result.board;
            }
            oldResult = result;
        }
        //System.out.print("\tGame stopped: too many rounds.\n");
        return result.board;
    }

    private int updateGameEndingIndicators(TurnData latestTurnData, int turnsWithouMoving) {
        if(!latestTurnData.didMove) {
            turnsWithouMoving += 2;
        } else if(turnsWithouMoving > 0) {
            //System.out.println("turns without stones hit is " + turnsWithouMoving + ", lets decrease it by one");
            turnsWithouMoving--;
        }

        //Lets check every second turn if AIs are in a loop.
        //Btw changing this to 0 will give slightly different points.
        /*
        if(i%4 == 1) {
            //if the situation is repetitive AND it's not about other AI not able to do a move
            if (new Board(oldResult.board).hashCode() == new Board(result.board).hashCode() && turnsWithouMoving < 2) {
                //There have been same board layout in the past: so the result wont change.
                //So now we assume AI will do the same move with the same board every time.
                System.out.println("return board 'same board':\n" + result);
                return result.board;
            }
            oldResult = result;
        }*/
        return turnsWithouMoving;
    }

    /**
     * @param theFinalResult a round result where all the information from other rounds has been iteratively added
     * @param howManyGames was played in total
     * @return a sum of three percentages that describe how well the challenger played. The bigger the better.
     */
    private double parseResult(RoundResult theFinalResult, int howManyGames) {
        double whiteVicP = ((theFinalResult.whiteWins*1.0)/(howManyGames*1.0)) * 100;
        double blackVicP = ((theFinalResult.blackWins*1.0)/(howManyGames*1.0)) * 100;
        double sameColorWinsPercent = (theFinalResult.getTotalSameColorWins()*1.0)/(howManyGames*0.5) * 100;
        double morePointsWhitePercent = (((theFinalResult.whitePoints-theFinalResult.blackPoints)*1.0)/(theFinalResult.blackPoints*1.0)) * 100;
        double howManyPointsGivenPerGame = ((theFinalResult.whitePoints+theFinalResult.blackPoints)*1.0/howManyGames);
        double percentOfChallengerWonMoreGames = (theFinalResult.getTotalChallangerWinsMoreGamesValue()*1.0)/(howManyGames*0.5) * 100;
        double challengerPerformance = percentOfChallengerWonMoreGames - morePointsWhitePercent + blackVicP;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        System.out.println("\n### FINAL RESULT: white (lvl " + whiteDifficulty + ") - black (lvl " + blackDifficulty + ") " + theFinalResult.whitePoints + "-" + theFinalResult.blackPoints + ", (victories w-b " + theFinalResult.whiteWins + "-" + theFinalResult.blackWins + ")\n"
                + "\twhite wins " + formatter.format(whiteVicP) + "% of the games, black " + formatter.format(blackVicP) + "%\n"
                + "\twhite gets " + formatter.format(morePointsWhitePercent) + "% more points than black\n"
                + "\tpoints given per game " + formatter.format(howManyPointsGivenPerGame) + "\n" +
                "\tboth AIs won with same color " + formatter.format(sameColorWinsPercent) + "% of games\n" +
                "\tpercent of boards that the challenger (black) won more games than white " + formatter.format(percentOfChallengerWonMoreGames) + "%\n" +
                "\tchallenger performance: " + formatter.format(challengerPerformance) + "\n");
        return challengerPerformance;
    }

    private String parseGameEndedMessage(boolean starter, int whosTurn, int turnsWithoutMoving, int movesWithoutHitting) {
        String s = whosTurn == 1 ? "reds" : "blues";
        String reason = turnsWithoutMoving > 2 ? "AI couldn't do a move" : "there were " + movesWithoutHitting + " turns without hitting";
        s = "ended on " + s + (starter ? " (starting AI)" : " (second AI)") + " turn, because " + reason;
        return s;
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