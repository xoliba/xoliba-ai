package AI;

import Game.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

//todo AI computes game dynamically forward, remembering relevant situation from previous computation
//todo AI computes while user thinks next move
public class AI {

    private Board board;
    private int color;
    private Random random;
    private int inceptionTreshold; //how many rounds we go deeper: [1,inf[
    private AlphaBetaXoliba abx;

    public AI(int color) {
        this(color, 3);
    }

    /**
     *
     * @param color which AI plays
     * @param difficulty 1 being the easiest, 3 being still easy to compute (0 does nothing)
     */
    public AI(int color, int difficulty) {
        this.color = color;
        this.random = new Random();
        this.inceptionTreshold = difficulty;
        this.abx = new AlphaBetaXoliba();
    }

    public int getDifficulty() {
        return inceptionTreshold;
    }

    public boolean doesWantToSurrender(int[][] b) {
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
        return data.surrender;
    }

    public TurnData move(int[][] b, int color) {
        board = new Board(b);
        this.color = color;
        System.out.println("AI (color of " + color + ") got a new board:\n" + board);
        TurnData td = new TurnData();

        td = abx.doTheBestMoveForColor(board, inceptionTreshold, color);
        System.out.println("AI did a move:\n" + td);

        return td;
    }

}