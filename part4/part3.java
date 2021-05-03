import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.crypto.Data;

import java.awt.event.*;
import java.awt.*;

public class part3 {
    // public static String stops_times_path = "/Users/johnwesley/Desktop/Algos
    // /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
    // public static File stop_times = new File(stops_times_path);

    public static File STOP_TIMES;

    public part3(String stopTimesPath) throws IOException {
        STOP_TIMES = new File(stopTimesPath);
    };

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
        return getColumnNames(STOP_TIMES);
    }

    public static int getLinesCount(File filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int count = 0;
        while ((br.readLine()) != null) {
            count++;
        }
        br.close();
        return count;
    }

    public static boolean isTimeValid(String time) {
        final int MAX_HOURS = 23;
        final int MAX_MINUTES = 59;
        final int MAX_SECONDS = 59;

        // final int MIN_HOURS = 00;
        // final int MIN_MINUTES = 00;
        // final int MIN_SECONDS = 00;

        // System.out.println("\n Input time - "+time);
        String time_without_space = time.replaceAll("\\s", "");
        // System.out.println("\n time without space - "+time_without_space);

        String[] individualParts = time_without_space.split(":");
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        try {
            hours = Integer.parseInt(individualParts[0]);
            minutes = Integer.parseInt(individualParts[1]);
            seconds = Integer.parseInt(individualParts[2]);
        } catch (Exception e) {
            // System.err.println(e);
            return false;
        }

        // System.out.println(" hours - " + hours +" minutes - "+ minutes+" Seconds -
        // "+seconds);
        if (hours > MAX_HOURS || minutes > MAX_MINUTES || seconds > MAX_SECONDS) {
            // if (hours < MIN_HOURS || minutes < MIN_MINUTES || seconds < MIN_SECONDS)
            // System.out.println("invalid time - " + time);
            return false;
        } else
            return true;
    }

    public static ArrayList<String> getValidTimes(File filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String st;
        ArrayList<String> validTimes = new ArrayList<String>();
        ArrayList<String> invalidTimes = new ArrayList<String>();
        while ((st = br.readLine()) != null) {
            String[] line = st.split(",");
            // System.out.println(Arrays.toString(line));
            // columns in stop_times.txt
            // [trip_id, arrival_time, departure_time, stop_id, stop_sequence,
            // stop_headsign, pickup_type, drop_off_type, shape_dist_traveled]
            String arrival_time = line[1];
            String departure_time = line[2];
            if (isTimeValid(arrival_time) && isTimeValid(departure_time)) {
                validTimes.add(Arrays.toString(line));
            } else {
                invalidTimes.add(Arrays.toString(line));
            }
        }
        // System.out.println("Valid times - " + validTimes.size() + " Invalid times - "
        // + invalidTimes.size()
        // + " Number of lines - " + lineCount);
        br.close();
        return validTimes;
    }

    public static ArrayList<String> getValidTimesFromStopTimes() throws IOException {
        String stops_times_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
        File stop_times = new File(stops_times_path);
        return getValidTimes(stop_times);
    }

    public static Map<String, ArrayList<String>> createSortedArrivalTimeMap(ArrayList<String> validTimes) {
        int indexOfArrivalTime = 1;
        Map<String, ArrayList<String>> Time_Line = new TreeMap<>();

        for (int i = 0; i < validTimes.size(); i++) {
            String line = validTimes.get(i);
            String[] values = line.split(",");
            String arrivalTime = values[indexOfArrivalTime];

            Time_Line.computeIfAbsent(arrivalTime, k -> new ArrayList<>()).add(line);
        }
        return Time_Line;
    }

    public static Map<String, ArrayList<String>> createSortedArrivalTimeMapFromStopTimes() throws IOException {
        return createSortedArrivalTimeMap(getValidTimesFromStopTimes());
    }

    public static void printTripDetailsFromList(ArrayList<String> list_line) {

        String ColumnLabel0 = "trip_id - ";
        String ColumnLabel1 = " arrival_time - ";
        String ColumnLabel2 = " departure_time - ";
        String ColumnLabel3 = " stop_id - ";
        String ColumnLabel4 = " stop_sequence - ";
        String ColumnLabel5 = " stop_headsign - ";
        String ColumnLabel6 = " pickup_type - ";
        String ColumnLabel7 = " drop_off_type - ";
        String ColumnLabel8 = " shape_dist_traveled - ";
        String[] values = new String[9];
        try {
            for (String line : list_line) {
                String lineWithoutBraces = line.substring(1, line.length() - 1);
                // try {
                String[] strArr = lineWithoutBraces.split(",");
                // mising last elements
                if (strArr.length == (values.length - 1)) {
                    System.arraycopy(strArr, 0, values, 0, strArr.length);
                    int finalIndex = (values.length - 1);
                    values[finalIndex] = " ";
                } else if (strArr.length == values.length) {
                    System.arraycopy(strArr, 0, values, 0, strArr.length);
                }

                System.out.println(ColumnLabel0 + values[0] + ColumnLabel1 + values[1] + ColumnLabel2 + values[2]
                        + ColumnLabel3 + values[3] + ColumnLabel4 + values[4] + ColumnLabel5 + values[5] + ColumnLabel6
                        + values[6] + ColumnLabel7 + values[7] + ColumnLabel8 + values[8]);
            }
        } catch (NullPointerException npe) {
            System.out.println("NO Buses within the given time");
        }

    }

    // Bubbble sort
    public static String[][] sortTripsBasedOnID(String[][] tripsData) {
        boolean sorted = false;
        String[] temp;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < tripsData.length - 1; i++) {
                int int_i = Integer.parseInt(tripsData[i][0]);
                int int_i_1 = Integer.parseInt(tripsData[i + 1][0]);
                if (int_i > int_i_1) {
                    temp = tripsData[i];
                    tripsData[i] = tripsData[i + 1];
                    tripsData[i + 1] = temp;
                    sorted = false;
                }
            }
        }
        return tripsData;
    }

    public static void Part_3_GUI() throws IOException {
        String stops_times_path = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
        File stop_times = new File(stops_times_path);
        ArrayList<String> validTimes = getValidTimes(stop_times);
        Map<String, ArrayList<String>> arrivalTimes_String = createSortedArrivalTimeMap(validTimes);

        JFrame f = new JFrame("PART 3 GUI");

        String[] columnLabels = getColumnNames(stop_times);
        String[][] tableData = new String[10][10];

        DefaultTableModel dtm = new DefaultTableModel(tableData, columnLabels);
        JTable table = new JTable(dtm);

        // taken from
        // https://www.tutorialspoint.com/how-to-set-a-tooltip-to-each-column-of-a-jtableheader-in-java
        // to get the label name when hovered on
        ToolTipHeader tooltipHeader = new ToolTipHeader(table.getColumnModel());
        tooltipHeader.setToolTipStrings(columnLabels);
        table.setTableHeader(tooltipHeader);
        // table.getTableHeader().setOpaque(false);
        // table.getTableHeader().setBackground(Color.blue);

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

        JButton b = new JButton("Show");
        b.setBounds(625, 300, 90, 20);

        String hours[] = { "  0", "  1", "  2", "  3", "  4", "  5", "  6", "  7", "  8", "  9", " 10", " 11", " 12",
                " 13", " 14", " 15", " 16", " 17", " 18", " 19", " 20", " 21", " 22", " 23" };

        String minutes_seconds[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
                "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };

        final JLabel hoursLabel = new JLabel();
        hoursLabel.setBounds(45, 300, 90, 20);
        hoursLabel.setText("Hours");
        final JComboBox hoursInput = new JComboBox(hours);
        hoursInput.setBounds(85, 300, 90, 20);

        final JLabel minutesLabel = new JLabel();
        minutesLabel.setBounds(235, 300, 90, 20);
        minutesLabel.setText("Minutes");
        final JComboBox minutesInput = new JComboBox(minutes_seconds);
        minutesInput.setBounds(285, 300, 90, 20);

        final JLabel secondsLabel = new JLabel();
        secondsLabel.setBounds(435, 300, 90, 20);
        secondsLabel.setText("Seconds");
        final JComboBox secondsInput = new JComboBox(minutes_seconds);
        secondsInput.setBounds(485, 300, 90, 20);

        f.add(hoursLabel);
        f.add(minutesLabel);
        f.add(secondsLabel);

        f.add(hoursInput);
        f.add(minutesInput);
        f.add(secondsInput);

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

        f.add(label);
        f.add(count_label);

        f.add(b);

        f.setLayout(null);
        f.setSize(820, 600);
        f.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String time = hoursInput.getItemAt(hoursInput.getSelectedIndex()) + ":"
                        + minutesInput.getItemAt(minutesInput.getSelectedIndex()) + ":"
                        + secondsInput.getItemAt(secondsInput.getSelectedIndex());
                String displayData = "Time you've input is " + time;
                count_label.setText("");
                label.setText(displayData);

                System.out.println(displayData);

                // String timeForMap = "\\s+".concat(time);
                ArrayList<String> lines = new ArrayList<String>();
                lines = arrivalTimes_String.get(time);
                // printTripDetailsFromList(lines);
                String[][] tripsData;
                try {
                    tripsData = new String[lines.size()][columnLabels.length];
                    for (int i = 0; i < tripsData.length; i++) {
                        String line = lines.get(i);
                        String lineWithoutBraces = line.substring(1, line.length() - 1);
                        String[] values = lineWithoutBraces.split(",");
                        for (int j = 0; j < tripsData[i].length; j++) {
                            try {
                                tripsData[i][j] = values[j];
                            } catch (ArrayIndexOutOfBoundsException aio) {
                                tripsData[i][j] = " ";
                                // for some lines in the file where the last elements is missing
                            }
                        }
                    }
                    int busesCount = tripsData.length;
                    count_label.setText("There seem to be " + busesCount + " buses arriving at " + time);
                    tripsData = sortTripsBasedOnID(tripsData); // sorting trips based on their id using bubble sort for
                                                               // this
                    dtm.setDataVector(tripsData, columnLabels);
                } catch (NullPointerException np) {
                    String errorMessage = "Sorry, There doesn't seem to be any buses at the time you have selected";
                    count_label.setHorizontalAlignment(JLabel.CENTER);
                    // count_label.alignCenter();
                    count_label.setText(errorMessage);
                    System.out.println(errorMessage);
                    // dtm.setDataVector(null, columnLabels);
                    String[][] emptyData = new String[10][columnLabels.length];
                    dtm.setDataVector(emptyData, columnLabels);
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        Part_3_GUI();
    }
}
