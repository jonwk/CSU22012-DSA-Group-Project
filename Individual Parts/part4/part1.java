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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.crypto.Data;

import java.awt.event.*;
import java.awt.*;

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

    public static File STOPS;
    public static File STOP_TIMES;
    public static File TRANSFERS;
    public part1(String stopsPath, String stopTimesPath, String transfersPath){
        STOPS = new File(stopsPath);
        STOP_TIMES = new File(stopTimesPath);
        TRANSFERS = new File(transfersPath);
    }

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

    public static String[] getColumnNamesFromStops() throws IOException {
        return getColumnNames(STOPS);
    }

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

    public void setupGraphFiles() throws IOException {
        // String stops_times_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
        // File stop_times = new File(stops_times_path);

        // String stops_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        // File stops = new File(stops_path);

        // String transfers_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/transfers.txt";
        // File transfers = new File(transfers_path);

        setupGraph(STOPS, STOP_TIMES, TRANSFERS);
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
            for (int i = 0; i < shortestPath.size(); i++) {
                System.out.print(shortestPath.get(i));
                if (i != shortestPath.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
    }

    public static void part1GUI() throws IOException {
        String stops_times_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
        File stop_times = new File(stops_times_path);

        String stops_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        File stops = new File(stops_path);

        String transfers_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/transfers.txt";
        File transfers = new File(transfers_path);

        setupGraph(stops, stop_times, transfers);

        JFrame f = new JFrame("PART 1 GUI");

        // String[] columnLabels = { "stop_id","stop_code","stop_name","stop_desc","stop_lat","stop_lon","zone_id","stop_url","location_type","parent_station"};
        String[] stopColumnLabels = getColumnNames(stops);
        String[] columnLabels = {"order",stopColumnLabels[0],stopColumnLabels[1],stopColumnLabels[2],stopColumnLabels[6]};
        
        String[][] tableData = new String[10][5];

        DefaultTableModel dtm = new DefaultTableModel(tableData, columnLabels);
        JTable table = new JTable(dtm);

        JTableHeader header = table.getTableHeader();
        String fg_color = "#ffffff";
        String bg_color = "#000000";
        header.setBackground(Color.decode(bg_color));
        header.setForeground(Color.decode(fg_color));

        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(true);
        table.setGridColor(Color.decode(bg_color));

        JScrollPane scrollPane = new JScrollPane(table);
        // JScrollBar vScroll = scrollPane.getVerticalScrollBar();
        table.setLayout(new BorderLayout());

        int N_ROWS = tableData.length;

        Dimension d = new Dimension(800, N_ROWS * table.getRowHeight());
        table.setPreferredScrollableViewportSize(d);
        TableColumn column = null;
        // column = table.getColumnModel().getColumn(0);
        // column.setPreferredWidth(columnLabels[0].length() * 10);
        for (int i = 0; i < columnLabels.length; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnLabels[i].length() * 10);
        }

        for (int i = 0; i < N_ROWS; i++) {
            dtm.addRow(tableData[i]);
        }

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(scrollPane, BorderLayout.CENTER);
        f.pack();
        f.setLocationRelativeTo(null);

        JTextField tf1 = new JTextField();
        tf1.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if (tf1.getText().equals("Start Stop ID")) {
                    tf1.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (tf1.getText().isEmpty()) {
                    tf1.setText("Start Stop ID");
                }
            }
        });
        JTextField tf2 = new JTextField();

        tf2.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                if (tf2.getText().equals("Dest Stop ID")) {
                    tf2.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if (tf2.getText().isEmpty()) {
                    tf2.setText("Dest Stop ID");
                }
            }
        });

        tf1.setBounds(225, 300, 150, 20);
        tf2.setBounds(425, 300, 150, 20);
        JButton b = new JButton("Show");
        b.setBounds(625, 300, 90, 20);

        JLabel costLabel = new JLabel();
        costLabel.setBounds(225, 400, 500, 20);
        costLabel.setHorizontalAlignment(JLabel.CENTER);
        costLabel.setVerticalAlignment(JLabel.CENTER);

        JLabel pathLabel = new JLabel();
        pathLabel.setHorizontalAlignment(JLabel.CENTER);
        pathLabel.setVerticalAlignment(JLabel.CENTER);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(225, 500, 500, 20);
        errorLabel.setHorizontalAlignment(JLabel.CENTER);
        errorLabel.setVerticalAlignment(JLabel.CENTER);

        f.add(tf1);
        f.add(tf2);
        f.add(b);
        f.add(costLabel);
        f.add(errorLabel);

        f.setLayout(null);
        f.setSize(820, 600);
        f.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int fromStopID = Integer.parseInt(tf1.getText());
                    int toStopID = Integer.parseInt(tf2.getText());

                    if (routes.isValidStopId(fromStopID) && routes.isValidStopId(toStopID)) {
                        System.out.println("Inputs start stop - " + tf1.getText() + " dest stop - " + tf2.getText());
                        // int fromStopID = 71;
                        // int toStopID = 646;
                        printShortestPathInfo(fromStopID, toStopID);
                        double shortestCost = routes.getShortestPathCost();
                        ArrayList<Integer> shortestPath = routes.getShortestPath(fromStopID, toStopID);
                        ArrayList<Stop> enrouteDetails = routes.getEnrouteStops(shortestPath);
                        String[][] tableData = new String[enrouteDetails.size()][5];
                        for (int i = 0; i < enrouteDetails.size(); i++) {
                            Stop stop = enrouteDetails.get(i);
                            tableData[i][0] = String.valueOf(i+1);
                            tableData[i][1] = String.valueOf(stop.stop_id);
                            tableData[i][2] = String.valueOf(stop.stop_code);
                            tableData[i][3] = stop.stop_name;
                            tableData[i][4] = stop.zone_id;
                        }
                        dtm.setDataVector(tableData, columnLabels);
                        String costLabelStr = "The cost associated with moving from " + fromStopID + " to " + toStopID
                                + " is " + shortestCost;
                        costLabel.setText(costLabelStr);
                    } else
                        throw new IllegalArgumentException();

                } catch (NumberFormatException nfe) {
                    String errorMsg = "Please enter a number";
                    System.out.println(errorMsg);
                    errorLabel.setText(errorMsg);
                    String[][] emptyData = new String[10][columnLabels.length];
                    dtm.setDataVector(emptyData, columnLabels);
                } catch (IllegalArgumentException iae) {
                    String errorMsg = "No stop with the input value";
                    System.out.println(errorMsg);
                    errorLabel.setText(errorMsg);
                    String[][] emptyData = new String[10][columnLabels.length];
                    dtm.setDataVector(emptyData, columnLabels);
                }

            }
        });

    }

    public static void main(String[] args) throws IOException {
        // String stops_times_path = "/Users/johnwesley/Desktop/Algos
        // /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
        // File stop_times = new File(stops_times_path);

        // String stops_path = "/Users/johnwesley/Desktop/Algos
        // /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        // File stops = new File(stops_path);

        // String transfers_path = "/Users/johnwesley/Desktop/Algos
        // /Sem2/ADS-MOCK-TRIAL/inputs/transfers.txt";
        // File transfers = new File(transfers_path);

        // setupGraph(stops, stop_times, transfers);

        // int fromStopID = 71;
        // int toStopID = 646;
        // printShortestPathInfo(fromStopID, toStopID);
        part1GUI();
    }
}
