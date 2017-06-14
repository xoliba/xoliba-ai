package AI;

import Game.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;


public class AI {

    private Board board;
    private int color;
    private Random random;
    private int inceptionTreshold; //how many rounds we go deeper
    private AlphaBetaXoliba abx;

    public AI(int color) {
        this(color, 3);
    }

    /**
     *
     * @param color which AI plays
     * @param difficulty 0 being the easiest, 3 being still easy to compute
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

    public TurnData move(int[][] b) {
        board = new Board(b);
        System.out.println("AI (color of " + color + ") got a new board:\n" + board);
        TurnData td = new TurnData();

        if (color == 1) td = abx.doTheBestMoveForRed(board, inceptionTreshold);
        else if (color == -1) td = abx.doTheBestMoveForBlue(board, inceptionTreshold);
        System.out.println("AI did a move:\n" + td);

        return td;
    }

}