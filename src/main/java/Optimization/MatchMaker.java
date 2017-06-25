package Optimization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import AI.*;
import Game.Board;
import Game.TurnData;
import Messaging.JsonConverter;

public class MatchMaker {

    int whiteDifficulty = 1;
    int blackDifficulty = 1;
    ParametersAI whiteParameters; //the champ
    ParametersAI blackParameters; //the challenger
    ArrayList<int[][]> boards;
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
        this.boards = readBoards();
    }

    /**
     *
     * @param howManyBoards
     * @return a single double that describes how well the challenger performed; the bigger the better (for the challenger)
     */
    public AIMatchResult calculate(int howManyBoards) {
        //ArrayList<RoundResult> results = new ArrayList<>();
        RoundResult finalResult = new RoundResult();
		
		Semaphore finished = new Semaphore(howManyBoards < boards.size() ? 1 - howManyBoards : 1 - boards.size()); // :P
		Semaphore threadCount = new Semaphore(8);
		Semaphore mutex = new Semaphore(1);
		
        for(int i=0; i<howManyBoards && i<boards.size(); i++) {
			int j = i;		// yes, this is necessary

            if (runSingleThread) {
                RoundResult rr = calculateRoundForBothRoles(boards.get(i));
                rr.roundNo = j+1;
                if (print)
                    System.out.println(rr + "\n" + rr.endGameMessagesToString());
                finalResult.add(rr);
            } else {

                Thread matchThread = new Thread(() -> {
                    acquire(threadCount);

                    RoundResult rr = calculateRoundForBothRoles(boards.get(j));
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

		return new AIMatchResult(finalResult, 2 * howManyBoards, whiteDifficulty, whiteParameters, blackDifficulty, blackParameters);
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

    //TODO this might be broken, might need more testing
    public int[][] playUntilRoundEnded(AI firstAI, AI secondAI, int[][] board, RoundResult rr) {
        int turnsWithoutMoving = 0;
        AI[] ai = new AI[]{firstAI, secondAI};
        //System.out.println("play until round ends, board in the beginning\n" + new Board(board));
        //Right now max moves is set to 100, which is 50 turns
        TurnData result = ai[0].move(board, ai[0].color, ai[0].getDifficulty(), 0);
        //System.out.println("board after one move\n" + new Board(result.board));
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
        System.out.print("\tGame stopped: too many rounds.\n");
        return result.board;
    }

    private int updateGameEndingIndicators(TurnData latestTurnData, int turnsWithoutMoving) {
        if(!latestTurnData.didMove) {
            turnsWithoutMoving += 2;
        } else if(turnsWithoutMoving > 0) {
            //System.out.println("turns without stones hit is " + turnsWithouMoving + ", lets decrease it by one");
            turnsWithoutMoving--;
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
        return turnsWithoutMoving;
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

    //from int[][][] to arraylist<int[][]> through json
    private void writeBoards(int[][][] boards) {
        ArrayList<String> bs = new ArrayList<>();
        for (int i = 0; i < boards.length; i++) {
            bs.add(JsonConverter.jsonifyTable(boards[i]));
        }

        try {
            Path file = Paths.get("./build/resources/main/boardsJSON.txt");
            Files.write(file, bs, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<int[][]> readBoards() {
        ArrayList<int[][]> p = new ArrayList<>();
        try {
            InputStream in = MatchMaker.class.getResourceAsStream("/boardsJSON.txt");
            BufferedReader bufferedReader =  new BufferedReader(new InputStreamReader(in));

            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                if (line.length() < 3) {
                    continue;
                }
                p.add(JsonConverter.parseTable(line));
            }

            bufferedReader.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return p;
    }
}