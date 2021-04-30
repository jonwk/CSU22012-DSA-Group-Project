import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class part2 {

    public static TernarySearchTree STOP_NAMES_TST = new TernarySearchTree();

    public static String[] getColumnNames(File filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;
        while ((st = br.readLine()) != null) {
            String[] line = st.split(",");
            br.close();
            return line;
        }
        br.close();
        return null;
    }

    // In order for this to provide meaningful search functionality please move
    // keywords flagstop, wb, nb, sb, eb from start of the names to the end of the
    // names of the stops when reading the file into a TST (eg “WB HASTINGS ST FS
    // HOLDOM AVE” becomes “HASTINGS ST FS HOLDOM AVE WB”)
    public static String makeMeaningful(String stopName) {

        int normalKeywordLength = 2;
        int flagtstopLength = 8;

        String temp = stopName.substring(0, normalKeywordLength).strip().toUpperCase();
        String tempFlagStop = stopName.substring(0, flagtstopLength).strip().toUpperCase();

        if (temp.equals("WB") || temp.equals("NB") || temp.equals("SB") || temp.equals("EB")) {
            String lastPart = stopName.substring(normalKeywordLength + 1);
            String firstPart = stopName.substring(0, normalKeywordLength);
            String meaningfulStr = lastPart.concat(" ").concat(firstPart);
            return makeMeaningful(meaningfulStr);
        }
        if (tempFlagStop.equals("FLAGSTOP")) {
            String lastPart = stopName.substring(flagtstopLength + 1);
            String firstPart = stopName.substring(0, flagtstopLength);
            String meaningfulStr = lastPart.concat(" ").concat(firstPart);
            return makeMeaningful(meaningfulStr);
        } else
            return stopName;
    }

    public static ArrayList<String> getStopNames(File filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;
        ArrayList<String> stopNames = new ArrayList<String>();
        while ((st = br.readLine()) != null) {
            String[] line = st.split(",");
            if (!line[2].equals("stop_name")) {
                String meaningfulName = makeMeaningful(line[2]);
                stopNames.add(meaningfulName);
            }
        }
        br.close();
        return stopNames;
    }

    public static void printDuplicateStations(ArrayList<String> stopNames) {
        ArrayList<String> stopNamesUniques = new ArrayList<String>();
        int duplicateCount = 0;
        System.out.println("------- Duplicate Stop Names -------");
        for (String x : stopNames) {
            if (stopNamesUniques.contains(x)) {
                duplicateCount++;
                System.out.println(duplicateCount + " " + x);
            } else
                stopNamesUniques.add(x);
        }

        System.out.println("Stops count - " + stopNames.size());
        System.out.println("Unique Stops count - " + stopNamesUniques.size());
    }

    public static Map<String, ArrayList<String>> createNameDetailsMap(File filename) throws IOException {

        int indexOfStopName = 2;
        Map<String, ArrayList<String>> Time_Line = new TreeMap<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;

        while ((st = br.readLine()) != null) {
            String[] line = st.split(",");
            if (!line[indexOfStopName].equals("stop_name")) {
                String meaningfulName = makeMeaningful(line[indexOfStopName]);
                Time_Line.computeIfAbsent(meaningfulName, k -> new ArrayList<>()).add(st);
                // stopNames.add(meaningfulName);
            }
        }
        br.close();
        return Time_Line;
    }

    public static void insertStopNamesToTST(ArrayList<String> stopNames) {
        for (String stopName : stopNames) {
            STOP_NAMES_TST.insert(stopName);
        }
    }


    public static void main(String[] args) throws IOException {
        String stops_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        File stops = new File(stops_path);
        String[] stops_column_names = getColumnNames(stops);

        ArrayList<String> stopNames = getStopNames(stops);

        // printDuplicateStations(stopNames);
        insertStopNamesToTST(stopNames);
        String[] HastingsSearch = STOP_NAMES_TST.search("HASTINGS");

        String[] Stop_Names = (String[]) stopNames.toArray();
        // System.out.println();
        // System.out.println(STOP_NAMES_TST.toString());

        for(String x: HastingsSearch)
        System.out.println(x);
        System.out.println("Length"+HastingsSearch.length);

        // System.out.println(Arrays.toString(stops_column_names));

        // Map<String, ArrayList<String>> stopDetails = createNameDetailsMap(stops);

        // Set<String> uniqueStops = new HashSet<String>(stopNames);
        // System.out.println("Unique Stops count: " + uniqueStops.size());

    }
}
