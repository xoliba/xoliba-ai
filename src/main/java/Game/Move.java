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
    
    public void removeTriangles(){
        triangles.clear();
    }

    /**
     *
     * @return null if there are no triangles, biggest triangle otherwise
     */
    public Triangle getBiggestTriangle() {
        if (triangles.isEmpty()) return null;

        Triangle biggest = triangles.get(0);
        for (int i = 1; i < triangles.size(); i++) {
            Triangle t = triangles.get(i);
            if (t.getSize() > biggest.getSize()) {
                biggest = t;
            }
        }

        return biggest;
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
        hash = 97 * hash + this.start.hashCode();
        hash = 97 * hash + this.target.hashCode();
        return hash;
    }

}
