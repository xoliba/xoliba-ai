import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Optimization.*;
import AI.*;
import Game.Board;
import Game.TurnData;

public class OptimizationMain {

    public static void main(String[] args) {
        System.out.print(new MatchMaker(2, new ParametersAI(7, 1.5, 3, 5, 4),
                                        3, new ParametersAI(7, 1.5, 3, 5, 4)
        ).calculate(50));
    }
}
