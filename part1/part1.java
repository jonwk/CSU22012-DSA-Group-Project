import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class part1 {

    // * Finding shortest paths between 2 bus stops (as input by the user),
    // returning the list of stops en route as well as the associated “cost”.
    // * Stops are listed in stops.txt and connections (edges) between them come
    // from stop_times.txt and transfers.txt files.
    // * All lines in transfers.txt are edges (directed), while in stop_times.txt an
    // edge should be added only between 2 consecutive stops with the same trip_id.
    // * eg first 3 entries in stop_times.txt are
    // * 9017927, 5:25:00, 5:25:00,646,1,,0,0,
    // * 9017927, 5:25:50, 5:25:50,378,2,,0,0,0.3300
    // * 9017927, 5:26:28, 5:26:28,379,3,,0,0,0.5780
    // * This should add a directed edge from 646 to 378, and a directed edge from
    // 378 to 379 (as they’re on the same trip id 9017927).
    // * Cost associated with edges should be as follows:
    // * 1 if it comes from stop_times.txt,
    // * 2 if it comes from transfers.txt with transfer type 0 (which is immediate
    // transfer possible),
    // * and for transfer type 2 the cost is the minimum transfer time divided by
    // 100.

    // public static String[] getColumnNames(File filename) throws IOException {
    // BufferedReader br = new BufferedReader(new FileReader(filename));
    // String st;
    // while ((st = br.readLine()) != null) {
    // String[] line = st.split(",");
    // br.close();
    // return line;
    // }
    // br.close();
    // return null;
    // }

    // // prints the lines with a parent station name
    // public static void printIfParent(File filename) throws IOException {
    // BufferedReader br = new BufferedReader(new FileReader(filename));
    // String st;
    // while ((st = br.readLine()) != null) {
    // String[] line = st.split(",");
    // if (line.length == 10) {
    // System.out.println(Arrays.toString(line));
    // }
    // }
    // br.close();
    // }

    // // to check if any of the data hsa stop url
    // // spoiler there are none
    // public static void printIfURL(File filename) throws IOException {
    // BufferedReader br = new BufferedReader(new FileReader(filename));
    // String st;
    // // int count = 0;
    // while ((st = br.readLine()) != null) {
    // String[] line = st.split(",");
    // if (!line[7].equals(" ")) {
    // // if(line[7].equals(" ")){
    // // System.out.println(line[7]);
    // System.out.println(Arrays.toString(line));
    // // count++;
    // }
    // }
    // // System.out.println("lines without stop url are "+count);
    // br.close();
    // }

    // public static Map<String, ArrayList<String[]>> makeStringMap(File filename,
    // int keyIndex) throws IOException {
    // Map<String, ArrayList<String[]>> map = new HashMap<String,
    // ArrayList<String[]>>();

    // BufferedReader br = new BufferedReader(new FileReader(filename));
    // String st;
    // ArrayList<String[]> lineList = new ArrayList<String[]>();
    // int lineCount = 0;
    // while ((st = br.readLine()) != null) {
    // String[] line = st.split(",");
    // if (lineCount != 0) {
    // lineList.add(line);
    // }
    // lineCount++;

    // }
    // br.close();

    // for (int i = 0; i < lineList.size(); i++) {
    // String[] line = lineList.get(i);
    // String keyStr = line[keyIndex];

    // map.computeIfAbsent(keyStr, k -> new ArrayList<>()).add(line);
    // }

    // return map;
    // }

    // public static Map<String, ArrayList<String[]>> StopIdTripIdMap; // key is
    // trip ID

    // public static void makeEdgeMaps() throws IOException {
    // String transfers_path = "/Users/johnwesley/Desktop/Algos
    // /Sem2/ADS-MOCK-TRIAL/inputs/transfers.txt";
    // File transfers = new File(transfers_path);
    // int fromStopIdIndex = 0;
    // transfersMap = makeStringMap(transfers, fromStopIdIndex);

    // String stops_times_path = "/Users/johnwesley/Desktop/Algos
    // /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
    // File stop_times = new File(stops_times_path);
    // // makeStringMap(File filename, Map<String, ArrayList<String[]>> map, int
    // // keyIndex) throws IOException {
    // int tripIdIndex = 0;
    // TripIDMap = makeStringMap(stop_times, tripIdIndex);

    // int stopIdIndex = 3;
    // StopIdTripIdMap = makeStringMap(stop_times, stopIdIndex);
    // }

    public static StopConnections routes;
    public static Trips trips;

    public static void setupGraph(File stops, File stop_times, File transfers) throws IOException {
        System.out.println("Setting up Graph");

        routes = new StopConnections(stops, transfers);
        trips = new Trips(stop_times);

        for (int t = 1; t < trips.validData.size(); t++) {
            TripDetails trip1 = trips.validData.get(t - 1);
            TripDetails trip2 = trips.validData.get(t);
            int cost = 1;
            if (trip1.trip_id == trip2.trip_id) {
                routes.makeConnection(trip1.stop_id, trip2.stop_id, cost);
            }
        }
        System.out.println("Finishing setting graph");

    }

    public static void printShortestPathInfo(int fromStopID, int toStopID) {
        ArrayList<Integer> shortestPath = routes.getShortestPath(fromStopID, toStopID);
        double shortestCost = routes.getShortestPathCost();

        if (shortestCost == Double.POSITIVE_INFINITY) {
            System.out.println("No route from from " + fromStopID + " to " + toStopID);
        } else if (shortestCost == Double.NEGATIVE_INFINITY) {
            System.out.println("both are same");
        } else if (shortestCost == -1.0) {
            System.out.println("Invalid input");
        } else {
            System.out.println("Cost from " + fromStopID + " to " + toStopID + " is: " + shortestCost);
            ArrayList<Stop> details = routes.getEnrouteStops(shortestPath);
            for(int i = 0; i < shortestPath.size();i++){
                System.out.print(shortestPath.get(i));
                if(i != shortestPath.size() - 1){
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        String stops_times_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
        File stop_times = new File(stops_times_path);

        String stops_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        File stops = new File(stops_path);

        String transfers_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/transfers.txt";
        File transfers = new File(transfers_path);

        setupGraph(stops, stop_times, transfers);

        int fromStopID = 71;
        int toStopID = 646;
        printShortestPathInfo(fromStopID, toStopID);

    }
}
