import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class StopConnections {
    public static HashMap<Integer, ArrayList<ConnectionNode>> adjacencies;
    public static HashMap<Integer, Stop> IdDetailsMap;

    private static void initMaps() {
        adjacencies = new HashMap<>();
        IdDetailsMap = new HashMap<>();
    }

    public StopConnections() {
        initMaps();
    }

    public StopConnections(File stops, File transfers) throws IOException {
        initMaps();
        readStops(stops);
        readTransfers(transfers);
    }

    private static void readTransfers(File transfer) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(transfer));
        String st;
        ArrayList<String[]> lineList = new ArrayList<String[]>();
        int lineCount = 0;
        try {
            while ((st = br.readLine()) != null) {
                String[] line = st.split(",");
                if (lineCount != 0) {
                    // Cost associated with edges should be 2 if it comes from
                    // transfers.txt with transfer type 0 (which is immediate transfer possible),
                    // and for transfer type 2 the
                    // cost is the minimum transfer time divided by 100.
                    int emptyCode = -1;
                    int from_stop_id = (line[0].equals("") || line[0].equals(" ")) ? emptyCode
                            : Integer.parseInt(line[0]);
                    int to_stop_id = (line[1].equals("") || line[1].equals(" ")) ? emptyCode
                            : Integer.parseInt(line[1]);
                    int transfer_type = (line[2].equals("") || line[2].equals(" ")) ? emptyCode
                            : Integer.parseInt(line[2]);
                    double cost;
                    switch (transfer_type) {
                    case 0:
                        cost = 2;
                        makeConnection(from_stop_id, to_stop_id, cost);
                        break;

                    case 2:
                        double min_transfer_time = Double.parseDouble(line[3]);
                        cost = min_transfer_time / 100;
                        makeConnection(from_stop_id, to_stop_id, cost);
                        break;

                    default:
                        throw new Exception("invalid transfer type at line" + lineCount + "\n" + line);

                    }
                }
                lineCount++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Reading Transfers Completed Line Count - " + lineCount);
        br.close();
    }

    public static void makeConnection(int from_stop_id, int to_stop_id, double cost) {
        adjacencies.computeIfAbsent(from_stop_id, k -> new ArrayList<>());
        adjacencies.computeIfAbsent(to_stop_id, k -> new ArrayList<>());

        adjacencies.get(from_stop_id).add(new ConnectionNode(to_stop_id, cost));
    }

    private static void readStops(File stops) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(stops));
        String st;
        ArrayList<String[]> lineList = new ArrayList<String[]>();
        int lineCount = 0;
        int emptyCode = -1;
        double emptyCodeDouble = -1.0;
        String emptyString = "";
        String[] invalidline = {};
        try {
            while ((st = br.readLine()) != null) {
                String[] line = st.split(",");
                invalidline = line;
                // System.out.println(line.length);
                if (lineCount != 0) {
                    int stop_id = (line[0].equals("") || line[0].equals(" ")) ? emptyCode : Integer.parseInt(line[0]);

                    int stop_code = (line[1].equals("") || line[1].equals(" ")) ? emptyCode : Integer.parseInt(line[1]);

                    String stop_name = (line[2].equals("") || line[2].equals(" ")) ? emptyString
                            : makeMeaningful(line[2]);

                    String stop_desc = (line[3].equals("") || line[3].equals(" ")) ? emptyString : line[3];

                    double stop_lat = (line[4].equals("") || line[4].equals(" ")) ? emptyCodeDouble
                            : Double.parseDouble(line[4]);

                    double stop_lon = (line[5].equals("") || line[5].equals(" ")) ? emptyCodeDouble
                            : Double.parseDouble(line[5]);

                    String zone_id = (line[6].equals("") || line[6].equals(" ")) ? emptyString : line[6];

                    String stop_url = (line[7].equals("") || line[7].equals(" ")) ? emptyString : line[7];

                    int location_type = (line[8].equals("") || line[8].equals(" ")) ? emptyCode
                            : Integer.parseInt(line[8]);

                    String parent_station = (line.length == 9) ? emptyString : line[9];

                    addStop(new Stop(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url,
                            location_type, parent_station));
                }
                lineCount++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Reading Stops Completed Line Count - " + lineCount);
        br.close();
    }

    public static void addStop(Stop stop) {
        adjacencies.put(stop.stop_id, new ArrayList<>());
        IdDetailsMap.put(stop.stop_id, stop);
    }

    // In order for this to provide meaningful search functionality please move
    // keywords flagstop, wb, nb,
    // sb, eb from start of the names to the end of the names of the stops when
    // reading the file into a TST
    // (eg “WB HASTINGS ST FS HOLDOM AVE” becomes “HASTINGS ST FS HOLDOM AVE WB”)
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

    private static double shortestPathCost;

    public static double getShortestPathCost() {
        return shortestPathCost;
    }

    public static boolean isValidStopId(int stop_id) {
        return adjacencies.keySet().contains(stop_id);
    }

    // public static ArrayList<Integer> getShortestPath(int from_stop_id, int
    // to_stop_id, double[] distance) {
    public static ArrayList<Integer> getShortestPath(int from_stop_id, int to_stop_id) {
        if (isValidStopId(from_stop_id) && isValidStopId(to_stop_id)) {
            if (from_stop_id == to_stop_id) {
                shortestPathCost = Double.NEGATIVE_INFINITY;
                System.out.println("Both stops are the same");
                return null;
            }
            HashMap<Integer, Double> distanceTo = new HashMap<>(adjacencies.size());
            HashMap<Integer, Integer> prev = new HashMap<>(adjacencies.size());
            HashSet<Integer> seen = new HashSet<>(adjacencies.size());

            for (int key : adjacencies.keySet()) {
                distanceTo.put(key, Double.POSITIVE_INFINITY);
                prev.put(key, Integer.MAX_VALUE);
                seen.add(key);
            }

            distanceTo.put(from_stop_id, 0.0);

            while (!seen.isEmpty()) {
                int currStop = Integer.MAX_VALUE;
                double minVal = Double.POSITIVE_INFINITY;
                for (int val : seen) {
                    double newVal = distanceTo.get(val);
                    if (newVal < minVal) {
                        minVal = newVal;
                        currStop = val;
                    }
                }
                if (currStop == Integer.MAX_VALUE) {
                    break;
                }
                seen.remove(currStop);

                if (currStop == to_stop_id) {
                    break;
                }

                ArrayList<ConnectionNode> adjacent = adjacencies.get(currStop);

                if (adjacent != null) {
                    for (ConnectionNode adjNode : adjacent) {
                        if (adjNode.cost != Double.POSITIVE_INFINITY && distanceTo.get(currStop) != null) {
                            double adjDist = distanceTo.get(currStop) + adjNode.cost;
                            if (distanceTo.get(adjNode.stop_id) > adjDist) {
                                distanceTo.put(adjNode.stop_id, adjDist);
                                prev.put(adjNode.stop_id, currStop);
                            }
                        }
                    }
                }
            }

            ArrayList<Integer> shortestPath = new ArrayList<>();
            int stop = to_stop_id;
            if (prev.get(stop) != null) {
                if (prev.get(stop) != Integer.MAX_VALUE || stop == from_stop_id) {
                    while (stop != Integer.MAX_VALUE) {
                        shortestPath.add(0, stop);
                        stop = prev.get(stop);
                    }
                }
            }

            if (distanceTo.get(to_stop_id) != null)
                shortestPathCost = distanceTo.get(to_stop_id);

            return shortestPath;
        }
        if (!isValidStopId(from_stop_id)) {
            System.out.println("invalid from stop id " + from_stop_id);
        }
        if (!isValidStopId(to_stop_id)) {
            System.out.println("invalid to stop id " + to_stop_id);
        }
        shortestPathCost = -1.0;
        return null;
    }

    public ArrayList<Stop> getEnrouteStops(ArrayList<Integer> stopIDs){
        ArrayList<Stop> enrouteStopDetails = new ArrayList<>();
        System.out.println("Enroute Stops");
        for(int stop:stopIDs){
            Stop stopDetails = IdDetailsMap.get(stop);
            // stopDetails.printStopDetails();
            System.out.println("stop_id - "+ stopDetails.stop_id+"\tstop_name - "+stopDetails.stop_name);
            enrouteStopDetails.add(stopDetails);
        }
        return enrouteStopDetails;
    }
}
