package Game;

import java.util.ArrayList;
import AI.AI;

/**
 * Created by vili on 25.6.2017.
 */
public class RoundRecord {

    public Board startingBoard;
    public int result;
    public AI red, blue;
    public String endGameMessage;
    private ArrayList<TurnData> turns;


    public RoundRecord(Board startingBoard, AI red, AI blue) {
        this.startingBoard = startingBoard;
        this.red = red;
        this.blue = blue;
        turns = new ArrayList<>();
    }

    public void addTurn(TurnData turn) {
        turns.add(turn);
    }

    public ArrayList<TurnData> getTurns() {
        return turns;
    }

    public int howManyMoves() {
        return turns.size();
    }

    public int calculatePoints() {
        result = new Board(turns.get(turns.size() - 1).board).calculatePoints();
        return result;
    }

    public void addEndGameMessage(String message) {
        endGameMessage = message;
    }

    public Board getEndBoard() {
        return new Board(turns.get(turns.size() - 1).board);
    }

    @Override
    public String toString() {
        String s = "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤ ROUND RECORD ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\n"
                + red + "\n" + blue + "\n";
        s += startingBoard + "\n";
        s += "how many moves " + howManyMoves() + "\n";
        s += endGameMessage + "\n";
        s += "result " + result + "\n";
        s += "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\n";
        return s;
    }
}
