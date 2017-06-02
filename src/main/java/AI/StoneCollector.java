package AI;

import Game.*;

/**
 * Created by vili on 1.6.2017.
 */
public class StoneCollector {

    private Validator validator;

    public StoneCollector() {
        validator = new Validator();
    }

    /**
     * Removes all colored stones inside a triangle that can be formed by using
     * stone at coordinate c as a corner. Expects that at least one legal
     * triangle can be formed by using this move;
     *
     * @param board integer table that represents board
     * @param m the move that froms tringles
     */
    public Board collectStonesFromAnyTriangleAvailable(Board board, Move m) {
        Triangle t = m.getBiggestTriangle();
        hitStones(board, t);
        return board;
    }

    private void hitStones(Board board, Triangle t) {
        Coordinate c1 = t.getCorners()[0];
        Coordinate c2 = t.getCorners()[1];
        Coordinate c3 = t.getCorners()[2];
        System.out.println("\tStone Collector: hit stones " + t);

        if (c1.x == c2.x) {
            hitTriangles(board, c1.y, c2.y, c1.x, c3.x, true);
        } else if (c1.x == c3.x) {
            hitTriangles(board, c1.y, c3.y, c1.x, c2.x, true);
        } else if (c2.x == c3.x) {
            hitTriangles(board, c2.y, c3.y, c2.x, c1.x, true);
        } else if (c1.y == c2.y) {
            hitTriangles(board, c1.x, c2.x, c1.y, c3.y, false);
        } else if (c1.y == c3.y) {
            hitTriangles(board, c1.x, c3.x, c1.y, c2.y, false);
        } else if (c2.y == c3.y) {
            hitTriangles(board, c2.x, c3.x, c2.y, c1.y, false);
        }
    }

    private void hitTriangles(Board board, int basis1, int basis2, int bottomH, int tipH, boolean isVertical) {
        int min = Math.min(basis1, basis2);
        int max = Math.max(basis1, basis2);
        int n = 1;
        int triangleFloor = bottomH;
        if (tipH < bottomH) {
            n *= -1;
        }
        for (int i = 0; i < Math.abs(tipH - bottomH); i++) {
            for (int j = min; j <= max; j++) {
                if(j == basis1 || j == basis2) {
                    continue;
                }
                
                if (isVertical) {
                    if(board.board[triangleFloor][j] != 0){
                        board.board[triangleFloor][j] = 0;
                    }
                } else {
                    if(board.board[j][triangleFloor] != 0){
                        board.board[j][triangleFloor] = 0;
                    }
                }
            }
            triangleFloor += n;
            min++;
            max--;
        }
    }
}
