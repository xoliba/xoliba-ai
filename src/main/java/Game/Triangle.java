package Game;

/**
 * Created by vili on 1.6.2017.
 */
public class Triangle {

    private Coordinate[] corners;

    public Triangle(Coordinate c1, Coordinate c2, Coordinate c3) {
        corners = new Coordinate[3];
        corners[0] = c1;
        corners[1] = c2;
        corners[2] = c3;
    }

    public Coordinate[] getCorners() {
        return corners;
    }

    @Override
    public String toString() {
        return "triangle " + corners[0] + " " + corners[1] + " " + corners[2];
    }
}
