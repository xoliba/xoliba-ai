package Game;

import java.util.ArrayList;

/**
 * Created by vili on 1.6.2017.
 */
public class Move {

    public Coordinate start;
    public Coordinate target;
    public ArrayList<Triangle> triangles;

    public Move(Coordinate start, Coordinate target) {
        this.start = start;
        this.target = target;
        triangles = new ArrayList<>();
    }

    public void addPossibleTriangle(Triangle tri) {
        triangles.add(tri);
    }

}
