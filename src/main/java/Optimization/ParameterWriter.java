package Optimization;

import AI.ParametersAI;
import Messaging.JsonConverter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static AI.AI.bestParameters;

/**
 * Created by vili on 21.6.2017.
 */
public class ParameterWriter {
    private String fileName = "./build/resources/main/parameters.txt";
    private int minWeight, maxWeight;
    private double frequency;

    public ParameterWriter(int minWeight, int maxWeight, double frequency) {
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.frequency = frequency;
    }

    public ParameterWriter() {
        this(0, 50, 2);
    }

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
        int howManyParams = bestParameters.toArray().length;
        for (int i = 0; i < howManyParams; i++) { //for every parameter
            double[] meters = new double[howManyParams];
            System.arraycopy(bestParameters.toArray(), 0, meters, 0, meters.length);
            for (int j = minWeight; j <= maxWeight; j += frequency) {
                meters[i] = j;
                p.add(new ParametersAI(meters[0], meters[1], meters[2], meters[3], meters[4], meters[5]));
            }
        }
        return p;
    }

    public List<ParametersAI> readParameterCombinations() {
        List<ParametersAI> p = new ArrayList<>();
        try {
            InputStream in = MatchMaker.class.getResourceAsStream("/parameters.txt");
            BufferedReader bufferedReader =  new BufferedReader(new InputStreamReader(in));

            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                if (line.length() < 3) {
                    continue;
                }
                p.add(JsonConverter.parseParameters(line));
            }

            bufferedReader.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return p;
    }
}
