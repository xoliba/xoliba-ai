package AI;

import Game.*;

import java.util.Random;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

/**
 * Created by vili on 13.6.2017.
 */
public class AlphaBetaXoliba {

    private Logger logger = LogManager.getLogger(AlphaBetaXoliba.class);
    private Validator validator;
    private StoneCollector stoneCollector;
    private int inceptionThreshold;
    private ParametersAI params;
    private Random random = new Random();
    private long deadline = Long.MAX_VALUE;

    public AlphaBetaXoliba() {
        validator = new Validator();
        stoneCollector = new StoneCollector();
        inceptionThreshold = 3;
        this.params = AI.bestParameters;
    }

    /**
     * Calls @doTheBestMoveForColor with max wait = 3600sec = 1h
     * @param board
     * @param inceptionThreshold
     * @param color
     * @return
     */
    protected TurnData doTheBestMoveForColor(Board board, int inceptionThreshold, int color) {
        return doTheBestMoveForColor(board, inceptionThreshold, color, 3600);
    }

    /**
     * Decide the best move by using alpha-beta pruning. Will return current best result if max wait is exceeded (and start looking a whole new branch at root level if deadline is not met yet, which might result method to take more time than the deadline).
     * @param board
     * @param inceptionThreshold how deep should we go? 0 means no moves, 1 that we choose our best move, 2 that we consider also the opponents reaction and so on
     * @param color best move for who?
     * @param maxWaitSeconds how many seconds we can maximally spend here
     * @return all relevant info wrapped in a TurnData object
     */
    protected TurnData doTheBestMoveForColor(Board board, int inceptionThreshold, int color, int maxWaitSeconds) {
        if (color != 1 && color != -1) {
            logger.warn("Cannot do a move for any other color than -1 or 1! Was " + color);
            return null;
        }
        long startTime = System.currentTimeMillis();
        deadline = startTime + 1000 * maxWaitSeconds;

        this.params = board.getWeights();
        this.inceptionThreshold = inceptionThreshold;
        TurnData td = new TurnData(board.board);
        double redBest = Integer.MIN_VALUE;
        double blueBest = Integer.MAX_VALUE;

        if (inceptionThreshold <= 0) {
            return td;
        }

        ArrayList<Move> possibleMoves = validator.generateAllPossibleMoves(board, color);
        for (int i = 0; i < possibleMoves.size(); i++) {
            Move m = possibleMoves.get(i);
            if (System.currentTimeMillis() > deadline) {
                logger.info("AI exceeded deadline before going through all possible moves at root level!" +
                        "\n\tmax wait " + maxWaitSeconds + "sec; exceeded by " + ((System.currentTimeMillis() - deadline)) + "ms\n\t" +
                        "managed to process " + (i*1.0/possibleMoves.size()*1.0) * 100 + "% of possible moves");
                break;
            }
            Board b1 = board.copy(); //we generate a board
            b1.swap(m); //where we implement the move
            for (Triangle t:m.triangles) { //and for every triangle that can be formed with that move
                Board b2 = b1.copy(); //we make a board
                stoneCollector.hitStones(b2,t); //and implement the hitting
                int withoutHit = Board.sameAmountOfStonesOnBoard(b1.board, b2.board) ? 1 : 0;
                TurnData aMove = new TurnData(true, b2, withoutHit);
                double v;
                if (color == 1) {
                    v = minValue(aMove, 1, redBest, blueBest, 0); //the blue gets a move
                    if (v > redBest) { //if the result is better than the known best for red
                        redBest = v; //update the best
                        td = new TurnData(true, b2.copy(), m, t, 1);
                    }

                } else if (color == -1) {
                    v = maxValue(aMove, 1, redBest, blueBest, 0); //the red gets a move
                    if (v < blueBest) { //if the result is better than the known best for blue
                        blueBest = v; //update the best
                        td = new TurnData(true, b2.copy(), m, t, -1);
                    }
                }
            }
        }
        deadline = Long.MAX_VALUE;
        return td;
    }

    /**
     * "Reds turn"
     * estimate how good option this board would be for the red
     *
     * @param lastTurn
     * @param inceptionLevel
     * @param redsBest the biggest value predicted atm
     * @param bluesBest the smallest value predicted atm
     * @return the biggest possible (predicted) evaluation outcome from this game situation
     */

    protected double maxValue(TurnData lastTurn, int inceptionLevel, double redsBest, double bluesBest, int turnSkipped) {
        Board board = new Board(lastTurn.board, params);
        if (System.currentTimeMillis() > deadline) {
            logger.trace("exceeded deadline at maxValue by " + (System.currentTimeMillis()-deadline) + "ms, with inception level " + inceptionLevel);
            return board.evaluate(false);
        }
        if (gameEndedAfter(lastTurn, turnSkipped)) {
            return board.evaluate(true);
        }
        if (inceptionLevel >= inceptionThreshold) {
            return board.evaluate(false);
        }

        ArrayList<Move> possibleMoves = validator.generateAllPossibleMoves(board, 1);
        if (possibleMoves.isEmpty()) {
            return minValue(new TurnData(false, board, lastTurn.withoutHit + 1), inceptionLevel + 1, redsBest, bluesBest, turnSkipped+2);
        }
        turnSkipped--;

        double v = Double.MIN_VALUE;
        for (Move m: possibleMoves) { //for all possible moves for red
            Board b1 = board.copy();
            b1.swap(m);
            for (Triangle t: m.triangles) { //for all triangles we might form
                Board b2 = b1.copy(); //lets make a copy of this situation
                stoneCollector.hitStones(b2, t); //lets do the move
                int withoutHit = Board.sameAmountOfStonesOnBoard(b1.board, b2.board) ? lastTurn.withoutHit + 1 : 0;
                TurnData td = new TurnData(true, b2, withoutHit);
                v = Math.max(v, minValue(td, inceptionLevel + 1, redsBest, bluesBest, Math.max(turnSkipped, 0))); //lets keep the best possible outcome
                if (v >= bluesBest) { //this is the alpha-beta part: if our new value is better than the so-far-best (from our perspective)
                    return v;         //that opponent could choose, then we do have look no further
                }
                redsBest = Math.max(redsBest, v); //lets update the best value so far for future minValue invitations
            }
        }
        return v;
    }

    /**
     * "Blues turn"
     * estimate how good option this board would be for the blue
     *
     * @param lastTurn
     * @param inceptionLevel
     * @param redsBest the biggest value predicted atm
     * @param bluesBest the smallest value predicted atm
     * @return the smallest possible (predicted) evaluation outcome from this game situation
     */

    protected double minValue(TurnData lastTurn, int inceptionLevel, double redsBest, double bluesBest, int turnSkipped) {
        Board board = new Board(lastTurn.board, params);
        if (System.currentTimeMillis() > deadline) {
            logger.trace("exceeded deadline at minValue by " + (System.currentTimeMillis()-deadline) + "ms, with inception level " + inceptionLevel);
            return board.evaluate(false);
        }
        if (gameEndedAfter(lastTurn, turnSkipped)) {
            return board.evaluate(true);
        }
        if (inceptionLevel >= inceptionThreshold) {
            return board.evaluate(false);
        }

        ArrayList<Move> possibleMoves = validator.generateAllPossibleMoves(board, -1);
        if (possibleMoves.isEmpty()) {
            return maxValue(new TurnData(false, board, lastTurn.withoutHit + 1), inceptionLevel + 1, redsBest, bluesBest, turnSkipped+2);
        }
        turnSkipped--;

        double v = Double.MAX_VALUE;
        for (Move m: possibleMoves) { //for all possible moves for blue
            Board b1 = board.copy();
            b1.swap(m);
            for (Triangle t: m.triangles) { //for all triangles we might form
                Board b2 = b1.copy(); //lets make a copy of this situation
                stoneCollector.hitStones(b2, t); //lets do the move
                int withoutHit = Board.sameAmountOfStonesOnBoard(b1.board, b2.board) ? lastTurn.withoutHit + 1 : 0;
                TurnData td = new TurnData(true, b2, withoutHit);
                v = Math.min(v, maxValue(td, inceptionLevel + 1, redsBest, bluesBest, Math.max(turnSkipped, 0))); //lets keep the best possible outcome

                //this is the alpha-beta part: if our new value is better than the so-far-best (from our perspective)
                if (v <= redsBest) { //that opponent could choose, then we do have look no further
                    return v;
                }
                bluesBest = Math.min(bluesBest, v); //lets update the best value so far for future maxValue invitations
            }
        }
        return v;
    }

    private boolean gameEndedAfter(TurnData lastTurn, int turnsSkipped) {
        if (lastTurn.withoutHit >= 30) {
            return true;
        } else if (turnsSkipped > 2) {
            return true;
        }
        return false;
    }

    public TurnData doARandomMove(Board board, int color) {
        ArrayList<Move> possibleMoves = validator.generateAllPossibleMoves(board.copy(), color);
        TurnData td = new TurnData(false, false, board.board);

        if (!possibleMoves.isEmpty()) {
            Board b1 = board.copy();
            Move m = possibleMoves.get(random.nextInt(possibleMoves.size()));
            Triangle t = m.triangles.get(random.nextInt(m.triangles.size()));
            stoneCollector.hitStones(b1, t);
            td = new TurnData(true, b1.copy(), m, t, color);
        }
        return td;
    }

    public TurnData doAGreedyMove(Board board, int color) {
        ArrayList<Move> possibleMoves = validator.generateAllPossibleMoves(board.copy(), color);
        TurnData td = new TurnData(false, false, board.board);
        int sizeOfBiggestTriangle = 0;
        int howManyStonesHitWithBiggestTriangle = 0;

        for (Move m:possibleMoves) {
            Board b1 = board.copy();
            b1.swap(m);
            for (Triangle t:m.triangles) {
                int tSize = t.getSize();
                if (tSize < sizeOfBiggestTriangle) {
                    continue;
                }
                Board b2 = b1.copy(); //lets make a copy of this situation
                int howManyStones = b2.howManyStonesOnBoard();
                stoneCollector.hitStones(b2, t); //lets do the move
                howManyStones -= b2.howManyStonesOnBoard();

                if (howManyStones > howManyStonesHitWithBiggestTriangle) {
                    sizeOfBiggestTriangle = tSize;
                    howManyStonesHitWithBiggestTriangle = howManyStones;
                    td = new TurnData(true, b2.copy(), m, t, color);
                }
            }
        }
        return td;

    }
}