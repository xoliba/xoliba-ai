package AI;

/**
 * Created by vili on 21.6.2017.
 */
public class ParametersAI {

    public double triangleWeight; //how valuable is biggest triangle? small x 1, med x 2, big x 3
    public double edgeWeight; //how valuable is a stone on the board edge (not corner)
    public double cornerWeight; //how valuable is a stone in the corner of the board
    public double basisEdgeMediumWeight; //value of a pair of stones, which are on the same edge and both in corners (and form a basis for med sized triangle)
    public double basisBigWeight; //value of a pair of stones, which are on the opposite edges of the board (and form a basis for a big triangle)

    public ParametersAI() {
        this(7, 1.5, 3, 4, 3.5);
    }

    public ParametersAI(double triangle, double edge, double corner, double basisMedium, double basisBig) {
        this.triangleWeight = triangle;
        this.edgeWeight = edge;
        this.cornerWeight = corner;
        this.basisEdgeMediumWeight = basisMedium;
        this.basisBigWeight = basisBig;
    }
}
