import Optimization.*;
import AI.*;

import java.util.List;

import static AI.AI.bestParameters;

public class OptimizationMain {

    public static void main(String[] args) {
        ParameterWriter pw = new ParameterWriter();
        pw.writeNewFileWithParameterValues();
        List<ParametersAI> parameterCombinations = pw.readParameterCombinations();

        for (int i = 0; i < parameterCombinations.size(); i++) {
            ParametersAI p = parameterCombinations.get(i);
            System.out.println( "Default parameters on white: " + bestParameters + "\n" +
                                "versus black with test param " + p);
            new MatchMaker(2, bestParameters,
                            2, p
            ).calculate(10);
        }

    }
}
