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
        System.out.print(new MatchMaker(2, 1).calculate(500));
    }
}
