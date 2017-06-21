package Optimization;

import AI.ParametersAI;
import Messaging.JsonConverter;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vili on 21.6.2017.
 */
public class ParameterWriter {
    private String fileName = "./build/resources/main/parameters.txt";
    public double[] bestParameters = new double[]{7,1.5,3,5,4};

    public void writeNewFileWithParameterValues() {
        List<String> lines = parametersToJsonLines(generateParameters());
        try {
            Path file = Paths.get(fileName);
            Files.write(file, lines, Charset.forName("UTF-8"));
            //Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<String> parametersToJsonLines(ArrayList<ParametersAI> parameters) {
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            lines.add(JsonConverter.jsonifyParameters(parameters.get(i)));
        }
        return lines;
    }

    private ArrayList<ParametersAI> generateParameters() {
        ArrayList<ParametersAI> p = new ArrayList<>();
        for (int i = 0; i < 5; i++) { //for every parameter
            double[] meters = new double[5];
            System.arraycopy(bestParameters, 0, meters, 0, bestParameters.length);
            for (int j = 0; j < 20; j++) { //for values [0,20]
                meters[i] = j;
                p.add(new ParametersAI(meters[0], meters[1], meters[2], meters[3], meters[4]));
            }
        }
        return p;
    }
}
