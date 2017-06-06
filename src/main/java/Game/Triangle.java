package Game;

/**
 * Created by vili on 1.6.2017.
 */
public class Triangle {

    private Coordinate[] corners;
    private int size;

    public Triangle(Coordinate c1, Coordinate c2, Coordinate c3) {
        corners = new Coordinate[3];
        corners[0] = c1;
        corners[1] = c2;
        corners[2] = c3;
        calculateSize();
    }

    private void calculateSize() {
        int length = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) continue; //there is no need to compare coordinate with itself
                Coordinate c1 = corners[i];
                Coordinate c2 = corners[j];

                if (c1.x == c2.x && Math.abs(c1.y - c2.y) > length) { //if we have vertical hypotenuse and it's length is bigger than the biggest
                    length = Math.abs(c1.y - c2.y);
                } else if (c1.y == c2.y && Math.abs(c1.x - c2.x) > length) { //if we have horizontal hypotenuse and -:-
                    length = Math.abs(c1.x - c2.x);
                }
            }
        }
        size = length / 2; //results 1, 2 or 3
    }

    public Coordinate[] getCorners() {
        return corners;
    }

    @Override
    public String toString() {
        return "triangle " + corners[0] + " " + corners[1] + " " + corners[2];
    }

    /**
     * @return 1, 2, or 3 according to the size of the triangle
     */
    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass()){
            return false;
        } else if (this.hashCode() == o.hashCode()){
            return true;
        }
        return false;

    }

    @Override
    public int hashCode() {
        int hash = 17;
        for (Coordinate c:corners) {
            hash *= 91 * c.hashCode();
        }
        return hash;
    }
}
