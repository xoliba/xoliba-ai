package AI;
import static AI.AI.bestParameters;

/**
 * Created by vili on 21.6.2017.
 */
public class ParametersAI {

    public double triangleWeight; //how valuable is biggest triangle? small x 1, med x 2, big x 3
    public double edgeWeight; //how valuable is a stone on the board edge (not corner)
    public double cornerWeight; //how valuable is a stone in the corner of the board
    public double basisEdgeMediumWeight; //value of a pair of stones, which are on the same edge and both in corners (and form a basis for med sized triangle)
    public double basisBigWeight; //value of a pair of stones, which are on the opposite edges of the board (and form a basis for a big triangle)
    public double calculatePointsWeight; //value of points given if game ended on the time of evaluation

    public ParametersAI() {
        this(bestParameters.toArray());
    }

    /**
     *
     * @param parameters length 6
     */
    public ParametersAI(double[] parameters) {
        this(parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], parameters[5]);
    }

    public ParametersAI(double triangle, double edge, double corner, double basisMedium, double basisBig, double pointsGiven) {
        this.triangleWeight = triangle;
        this.edgeWeight = edge;
        this.cornerWeight = corner;
        this.basisEdgeMediumWeight = basisMedium;
        this.basisBigWeight = basisBig;
        this.calculatePointsWeight = pointsGiven;
    }

    public double[] toArray() {
        return new double[]{
                triangleWeight, edgeWeight, cornerWeight, basisEdgeMediumWeight, basisBigWeight, calculatePointsWeight};
    }

    @Override
    public String toString() {
        return "triangle " + triangleWeight + " edge " + edgeWeight + " corner " + cornerWeight +
                " medium basis " + basisEdgeMediumWeight + " big basis " + basisBigWeight
                + " points given " + calculatePointsWeight;
    }
}
