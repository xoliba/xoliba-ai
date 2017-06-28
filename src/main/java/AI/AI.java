package AI;

import Game.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Random;

//todo AI computes game dynamically forward, remembering relevant situation from previous computation
//todo AI computes while user thinks next move
//todo AI should take time of its move and send the best option in the time limit
public class AI {
    //the first guess
    //public static ParametersAI bestParameters = new ParametersAI(7, 1.5, 3, 1, 1, 1);
    //simplified params
    //public static ParametersAI bestParameters = new ParametersAI(7, 1.5, 3, 0, 0, 0);

    //LVL 1 best params atm
    //public static ParametersAI bestParameters = new ParametersAI(4,50,40,16,20,0.1);

    //LVL 2 best params atm
    public static ParametersAI bestParameters = new ParametersAI(40,60,50,50,0.1,30);

    public int color;
    private static int[] waitTimes = new int[]{1,3,9,27,81,243}; //lvl 1,2,3,4,5,6+ seconds (3^n sec)
    private Logger logger = LogManager.getLogger(AI.class);
    private Board board;
    private Random random;
    private int inceptionThreshold; //how many rounds we go deeper: [1,inf[
    private AlphaBetaXoliba abx;
    private ParametersAI parameters;

    public AI(int color) {
        this(color, 3);
    }

    public AI(int color, int difficulty) {
        this(color, difficulty, bestParameters);
    }

    /**
     *
     * @param color which AI plays
     * @param difficulty 1 being the easiest, 3 being still easy to compute (0 does nothing)
     */
    public AI(int color, int difficulty, ParametersAI parameters) {
        this.color = color;
        this.random = new Random();
        this.inceptionThreshold = difficulty;
        this.abx = new AlphaBetaXoliba();
        this.parameters = parameters;
    }

    public int getDifficulty() {
        return inceptionThreshold;
    }

    public boolean doesWantToSurrender(TurnData data) {
        if (color == 1 && data.bluePoints >= 0.6 * data.scoreLimit)
            return false;
        else if (color == -1 && data.redPoints >= 0.6 * data.scoreLimit)
            return false;

        int[][] b = data.board;
        int sum = 0;
        for (int i = 1; i < 6; i++) {
            int weight = 1;
            if (i == 1 || i == 5) { //the corners are more valuable
                weight = 3;
            }
            sum += b[i][0] * weight;
            sum += b[i][6] * weight;
            sum += b[0][i] * weight;
            sum += b[6][i] * weight;
        }

        sum *= color; //if the sum is the same as our color, this will result positive value (good for us)

        if (sum < -10) {
            return true;
        }
        return false;
    }

    public boolean doesWantToStopPlaying(TurnData data) {
        if (!data.surrender) //if other doesn't want to stop
            return false;
        else if (inceptionThreshold < 3) { //if we are easy to win, then we can stop anytime player wants to
            return true;
        } else {
            Board b = new Board(data.board, parameters);
            if (b.calculatePoints() * color < 0) { //if we would lose this round
                return false;
            } else      //if we win then we can stop now
                return true;
        }
    }

    //todo refactor AI to take turn data as a parameter
    public TurnData move(int[][] b, int color, int difficulty, int withoutHit) {
        board = new Board(b, parameters);
        this.color = color;
        if (difficulty > 0 && difficulty < 50)
            this.inceptionThreshold = difficulty;
        logger.debug(this + " got a new board:\n" + board);

        TurnData td = abx.doTheBestMoveForColor(board, inceptionThreshold, color, getMaxWaitSeconds(inceptionThreshold));
        //todo make this more elegant
        if (Board.sameAmountOfStonesOnBoard(board.board, td.board)) {
            td.withoutHit = withoutHit + 1;
        } else {
            td.withoutHit = 0;
        }
        logger.debug("AI did a move:\n" + td);

        return td;
    }

    public TurnData move(int[][] b, int color) {
        return move(b, color, inceptionThreshold,  0);
    }

    public TurnData move(int[][] b) {
        return move(b, color, inceptionThreshold, 0);
    }

    public static int getMaxWaitSeconds(int lvl) {
        int i = 0;
        if (lvl > 0 && lvl < waitTimes.length) {
            i = lvl - 1;
        } else if (lvl >= waitTimes.length) {
            i = waitTimes.length - 1;
        }
        return waitTimes[i];
    }

    @Override
    public String toString() {
        String s = "AI lvl " + inceptionThreshold + " color ";
        s += color == 1 ? "red" : "blue";
        s += " parameters:\n\t" + parameters;
        return s;
    }
}