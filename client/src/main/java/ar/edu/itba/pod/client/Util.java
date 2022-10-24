package ar.edu.itba.pod.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static List<List<String>> parseCSV(Path path, char separator) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<List<String>> csv = new ArrayList<>();
        for (final String line : lines) {
            csv.add(parseLine(line, separator));
        }
        return csv;
    }

    public static List<String> parseLine(String line, char separator) {
        return new ArrayList<>(Arrays.asList(line.split(String.valueOf(separator))));
    }
}
