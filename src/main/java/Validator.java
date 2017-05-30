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
    public ArrayList<Coordinate> getPossibleMoves(int originX, int originY){
        ArrayList<Coordinate> possibleTargetCoordinates = getConnectedWhites(originX, originY);

        for (int i = 0; i < possibleTargetCoordinates.size(); i++) {
            Coordinate c = possibleTargetCoordinates.get(i);

            //if triangle cannot be formed, remove this coordinate from the list
            if (howManyTrianglesFound(c, board[originX][originY]) == 0) {
                possibleTargetCoordinates.remove(c);
            }
        }

        return possibleTargetCoordinates;
    }

    /**
     * @return all the white coordinates one can move to in the format
     * [ [x1,y1], [x2, y2], ...]
     */
    protected ArrayList<Coordinate> getConnectedWhites(int originX, int originY) {
        int[][] walkingDirections = { {1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1} }; //east, south-east, south, ...
        boolean[] stopWalkingToThisDirection = new boolean[8];

        ArrayList<Coordinate> coordinates = new ArrayList<>();
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
                Coordinate c = new Coordinate(x,y);

                if (isThisOffBoard(c) || board[c.x][c.y] != 0) {
                    stopWalkingToThisDirection[j] = true;
                    continue;
                }

                //the coordinate is on board and white, and this direction is not blocked
                coordinates.add(c);
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
        for (int i = 0; i < stopWalkingInThisDirection.length; i++) {
            if (!stopWalkingInThisDirection[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param c
     * @return if both x and y are [0,6]
     */
    protected boolean isThisOffBoard(Coordinate c) {
        int x = c.x;
        int y = c.y;
        if(x == 0 && (y == 0 || y == 6) || x == 6 && (y == 0 || y == 6)){
            return true; //the corners
        }
        return x > 6 || x < 0 || y > 6 || y < 0;
    }

    /**
     *
     * @param c [x,y]
     * @param color
     * @return can a triangle of given color be formed with the coordinate
     */
    protected int howManyTrianglesFound(Coordinate c, int color) {
        int triangles = 0;
        int[][] hypotenuseDirections = new int[][] {{-1, 0},{1, 0},{0, -1},{0, 1} }; //left, right, up, down
        int[][] edgeDirections = new int[][]{{-1, 1, -1, -1},{1, 1, 1, -1},{-1, -1, 1, -1},{-1, 1, 1, 1}}; //left, right, up, down

        for (int i = 2; i <= 6; i += 2) { //for all possible triangle hypotenuse lengths
            int distanceSide = i / 2;
            for (int j = 0; j < 4; j++) { //for all four directions
                Coordinate hypotenuseCoordinate = new Coordinate(hypotenuseDirections[j][0] * i + c.x, hypotenuseDirections[j][0] * i + c.y);
                Coordinate firstCornerCoordinate = new Coordinate(edgeDirections[j][0] * distanceSide + c.x, edgeDirections[j][1] * distanceSide + c.y);
                Coordinate secondCornerCoordinate = new Coordinate(edgeDirections[j][2] * distanceSide + c.x, edgeDirections[j][3] * distanceSide + c.y);

                triangles += lookForTriangles(c, hypotenuseCoordinate, firstCornerCoordinate, secondCornerCoordinate, color);
            }
        }
        return 0;
    }

    protected int lookForTriangles(Coordinate origin, Coordinate hypotenuseCoordinate, Coordinate firstPossibleCornerCoordinate, Coordinate secondPossibleCornerCoordinate, int color) {
        int triangles = 0;

        if (isThisOffBoard(firstPossibleCornerCoordinate) && isThisOffBoard(secondPossibleCornerCoordinate)) {
            return 0; //both corners are off board, no triangles can be made
        } else if (isThisOffBoard(hypotenuseCoordinate)) { //hypotenuse is off board

        } else {//hypotenuse and at least one of the corners is on board

        }
        return triangles;
    }

    protected boolean isCoordinateColor(Coordinate c, int color) {
        return board[c.x][c.y] == color;
    }


    /*
    //THIS CLASS IS NOT TESTED SEPARATELY. so please refactor patameters as you want.
    lookForTriangles(originX, originY, directionX, directionY, firstChangeX, firstChangeY, secondChangeX, secondChangeY, color) {
        let foundOnThisDirection = 0;
        let triangles = 0;
        let targetX = originX + directionX;
        let targetY = originY + directionY;
        foundOnThisDirection += this.checkDiagonals(originX, originY, firstChangeX, firstChangeY, secondChangeX, secondChangeY, color);


        if (!this.isThisOnBoard(targetX, targetY)) { //if the target is out of board
            if (foundOnThisDirection === 2) { //if there are two on diagonals
                triangles = 1;
            } else {
                triangles = 0;
            }
        } else if (foundOnThisDirection === 0 || (foundOnThisDirection === 1 && this.gameboard[originX + directionX][originY + directionY] !== color)) { //no triangles, target on board
            triangles = 0;
        } else if (foundOnThisDirection === 2 && this.gameboard[originX + directionX][originY + directionY] === color) { //all four stones are the right colour
            triangles = 3;
        } else {
            triangles = 1;
        }

        return triangles;
    }

    isThisOnBoard(x, y) {
        return x >= 0 && x <= 6 && y <= 6 && y >= 0; //if the target is out of board

    }

    //THIS CLASS IS NOT TESTED SEPARATELY. so please refactor patameters as you want.
    checkDiagonals(positionX, positionY, firstChangeX, firstChangeY, secondChangeX, secondChangeY, color) {
        return this.checkIfColour(positionX + firstChangeX, positionY + firstChangeY, color) +
                this.checkIfColour(positionX + secondChangeX, positionY + secondChangeY, color);
    }

    checkIfColour(targetX, targetY, color) {
        var result = 0;
        if (this.isThisOnBoard(targetX, targetY)) {
            if (this.gameboard[targetX][targetY] === color) {
                result++;
            }
        }
        return result;
    }
    */
    
}
