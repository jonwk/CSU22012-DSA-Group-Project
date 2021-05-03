import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.crypto.Data;

import java.awt.event.*;
import java.awt.*;

public class part2 {

    public static TernarySearchTree STOP_NAMES_TST = new TernarySearchTree();

    public static File STOPS;


    public part2(String stopsPath)throws IOException {
        STOPS = new File(stopsPath);

        ArrayList<String> stopNames = getStopNames(STOPS);
        insertStopNamesToTST(stopNames);
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

    public static String[] getColumnNamesFromStopTimes() throws IOException {
        return getColumnNames(STOPS);
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

    public static String removeSpacesAndCapitlize(String s) {
        s.replace(" ", "");
        return s.toUpperCase();
    }

    public static ArrayList<String> getStopNames(File filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;
        ArrayList<String> stopNames = new ArrayList<String>();
        while ((st = br.readLine()) != null) {
            String[] line = st.split(",");
            if (!line[2].equals("stop_name")) {
                String meaningfulName = makeMeaningful(line[2]);
                stopNames.add(removeSpacesAndCapitlize(meaningfulName));
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

    public static Map<String, ArrayList<String[]>> createNameDetailsMap(File filename) throws IOException {

        int indexOfStopName = 2;
        Map<String, ArrayList<String[]>> Time_Line = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;

        while ((st = br.readLine()) != null) {
            String[] line = st.split(",");
            if (!line[indexOfStopName].equals("stop_name")) {
                String meaningfulName = makeMeaningful(line[indexOfStopName]);
                line[indexOfStopName] = meaningfulName;
                Time_Line.computeIfAbsent(meaningfulName, k -> new ArrayList<>()).add(line);
                // stopNames.add(meaningfulName);
            }
        }
        br.close();
        return Time_Line;
    }

    public static Map<String, ArrayList<String[]>> createNameDetailsMapFromStops() throws IOException {
        return createNameDetailsMap(STOPS);
    }



    public static void insertStopNamesToTST(ArrayList<String> stopNames) {
        for (String stopName : stopNames) {
            STOP_NAMES_TST.insert(stopName);
        }
    }

    public static void part2GUI() throws IOException {

        String stops_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        File stops = new File(stops_path);

        ArrayList<String> stopNames = getStopNames(stops);

        // printDuplicateStations(stopNames);
        insertStopNamesToTST(stopNames);
        Map<String, ArrayList<String[]>> stopDetails = createNameDetailsMapFromStops();

        JFrame f = new JFrame("PART 2 GUI");

        String[] columnLabels = getColumnNames(stops);
        String[][] tableData = new String[10][10];

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

        final JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        // label.crossVerticalAlignment(JLabel.CENTER);s
        // label.setBounds(300, -35, 100, 20);

        label.setSize(table.getWidth(), table.getHeight() * 135 / 100);
        // f.add(label,BorderLayout.CENTER);

        final JLabel count_label = new JLabel();
        // count_label.setBounds(250, -15, 100, 20);
        count_label.setHorizontalAlignment(JLabel.CENTER);
        count_label.setVerticalAlignment(JLabel.CENTER);
        count_label.setSize(table.getWidth(), table.getHeight() * 160 / 100);

        JTextField tf1 = new JTextField(20);
        tf1.setBounds(425, 300, 150, 20);
        JButton b = new JButton("Show");
        b.setBounds(625, 300, 90, 20);

        f.add(tf1);
        f.add(b);
        f.add(label);
        f.add(count_label);

        f.setLayout(null);
        f.setSize(820, 600);
        f.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String input = tf1.getText();
                    Pattern p = Pattern.compile("[^a-z0-9\\- ]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(input);
                    boolean b = m.find();
                    if ((!input.equals("") && !input.equals(" ") && !input.substring(0,2).equals("  ")) && !b ) {
                        String displayData = "Input String - " + input;

                        System.out.println(displayData);
                        String searchInput = removeSpacesAndCapitlize(input);

                        String[] searchResults = STOP_NAMES_TST.search(searchInput);
                        if (searchResults == null) {
                            String errorMessage = "Sorry, There doesn't seem to be any buses at the time you have selected";
                            count_label.setHorizontalAlignment(JLabel.CENTER);
                            // count_label.alignCenter();
                            count_label.setText(errorMessage);
                            System.out.println(errorMessage);
                            // dtm.setDataVector(null, columnLabels);
                            String[][] emptyData = new String[10][columnLabels.length];
                            dtm.setDataVector(emptyData, columnLabels);
                        }
                        ArrayList<String[]> lines = new ArrayList<>();
                        for (String res : searchResults) {
                            ArrayList<String[]> details = stopDetails.get(res);
                            for (String[] d : details) {
                                lines.add(d);
                            }
                        }
                        // lines =
                        // printTripDetailsFromList(lines);
                        String[][] stopsData;
                        stopsData = new String[lines.size()][columnLabels.length];
                        for (int i = 0; i < stopsData.length; i++) {
                            stopsData[i] = lines.get(i);
                        }
                        int stopsCount = stopsData.length;
                        count_label.setText("There seem to be " + stopsCount + " matching bus stops");

                        dtm.setDataVector(stopsData, columnLabels);
                    } else
                        throw new IllegalArgumentException();
                } catch (NullPointerException np) {
                    String errorMessage = "Sorry, There doesn't seem to be any bus stops with the words you have typed in";
                    count_label.setHorizontalAlignment(JLabel.CENTER);
                    // count_label.alignCenter();
                    count_label.setText(errorMessage);
                    System.out.println(errorMessage);
                    // dtm.setDataVector(null, columnLabels);
                    String[][] emptyData = new String[10][columnLabels.length];
                    dtm.setDataVector(emptyData, columnLabels);
                } catch (IllegalArgumentException ie) {
                    System.out.println("print something valid");
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        // String stops_path = "/Users/johnwesley/Desktop/Algos
        // /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
        // File stops = new File(stops_path);
        // String[] stops_column_names = getColumnNames(stops);

        // ArrayList<String> stopNames = getStopNames(stops);

        // printDuplicateStations(stopNames);
        // insertStopNamesToTST(stopNames);
        // String input = "HASTINGS ST";
        // String[] HastingsSearch =
        // STOP_NAMES_TST.search(removeSpacesAndCapitlize(input));

        // Map<String, ArrayList<String[]>> stopDetails = createNameDetailsMap(stops);
        // for (String searchResult : HastingsSearch) {
        // ArrayList<String[]> details = stopDetails.get(searchResult);
        // for (String[] x : details) {
        // for (String s : x) {
        // System.out.print(s + " - ");
        // }
        // System.out.println();
        // }
        // }

        part2GUI();



    }
}
