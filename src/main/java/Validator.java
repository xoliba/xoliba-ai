/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author eerop, vilip
 */
public class Validator {
    
    private int[][] board;

    /**
     * This must be called before asking for possible moves to ensure that the board is at the right state
     * @param board the latest board
     */
    public void updateBoard(int[][] board) {
        this.board = board;
    }

    /**
     *
     * @param originX
     * @param originY
     * @return list of coordinates you can legally move to [ [x1,y1], [x2, y2], ...]
     */
    public ArrayList<int[]> getPossibleMoves(int originX, int originY){
        ArrayList<int[]> possibleTargetCoordinates = getConnectedWhites(originX, originY);

        for (int i = 0; i < possibleTargetCoordinates.size(); i++) {
            int[] coordinate = possibleTargetCoordinates.get(i);

            //if triangle cannot be formed, remove this coordinate from the list
            if (!checkIfFormsATriangle(coordinate, board[coordinate[0]][coordinate[1]])) {
                possibleTargetCoordinates.remove(coordinate);
            }
        }

        return possibleTargetCoordinates;
    }

    /**
     * @return all the white coordinates one can move to in the format
     * [ [x1,y1], [x2, y2], ...]
     */
    private ArrayList<int[]> getConnectedWhites(int originX, int originY) {
        ArrayList<int[]> coordinates = new ArrayList<>();
        return coordinates;
    }

    /**
     *
     * @param coordinate [x,y]
     * @param color of the triangle we are looking for
     * @return can a triangle be formed if there is a stone of given color at this coordinate
     */
    private boolean checkIfFormsATriangle(int[] coordinate, int color) {
        return true;
    }
    
}
