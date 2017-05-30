/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
            if (!checkIfFormsATriangle(coordinate, board[originX][originY])) {
                possibleTargetCoordinates.remove(coordinate);
            }
        }

        return possibleTargetCoordinates;
    }

    /**
     * @return all the white coordinates one can move to in the format
     * [ [x1,y1], [x2, y2], ...]
     */
    protected ArrayList<int[]> getConnectedWhites(int originX, int originY) {
        int[][] walkingDirections = { {1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1} }; //east, south-east, south, ...
        boolean[] stopWalkingToThisDirection = new boolean[8];

        ArrayList<int[]> coordinates = new ArrayList<>();
        for (int i = 1; i < 7; i++) { //the max distance to walk is 6 steps
            if (areAllDirectionsBlocked(stopWalkingToThisDirection)) {
                break;
            }

            for (int j = 0; j < 8; j++) { //check all eight directions
                if (stopWalkingToThisDirection[j]) {
                    continue; //this direction is blocked already
                }

                int x = originX + i * walkingDirections[j][0]; //the origin + distance to walk on x-axis to this direction
                int y = originY + i * walkingDirections[j][1];
                int[] coordinateToCheck = {x,y};

                if (isThisOffBoard(coordinateToCheck) || board[coordinateToCheck[0]][coordinateToCheck[1]] != 0) {
                    stopWalkingToThisDirection[j] = true;
                    continue;
                }

                //the coordinate is on board and white, and this direction is not blocked
                coordinates.add(coordinateToCheck);
            }
        }

        return coordinates;
    }

    /**
     *
     * @param stopWalkingInThisDirection table of booleans
     * @return are all the 8 directions blocked?
     */
    protected boolean areAllDirectionsBlocked(boolean[] stopWalkingInThisDirection) {
        int blocked = 0;
        for (int i = 0; i < stopWalkingInThisDirection.length; i++) {
            if (stopWalkingInThisDirection[i]) {
                blocked++;
            }
        }

        if (blocked == stopWalkingInThisDirection.length) return true;
        return false;
    }

    /**
     *
     * @param coordinate
     * @return if both x and y are [0,6]
     */
    protected boolean isThisOffBoard(int[] coordinate) {
        return coordinate[0] < 7 && coordinate[0] > -1 && coordinate[1] < 7 && coordinate[0] > -1;
    }

    /**
     *
     * @param coordinate [x,y]
     * @param color
     * @return can a triangle of given color be formed with the coordinate
     */
    protected boolean checkIfFormsATriangle(int[] coordinate, int color) {
        return true;
    }
    
}
