import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.image.BufferedImage;

import java.io.File;

public class part4 extends JFrame {

    private JPanel cardHolder;
    private CardLayout cards;
    private static final String cardA = "A";
    private static final String cardB = "B";
    private static final String cardC = "C";
    private static final String cardD = "D";
    private static Dimension d = new Dimension(820, 600);

    private static String STOPS_PATH = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stops.txt";
    private static String STOP_TIMES_PATH = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/stop_times.txt";
    private static String TRANSFERS_PATH = "/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/transfers.txt";

    // private static part1 part1 = new part1(STOPS_PATH, STOP_TIMES_PATH,
    // TRANSFERS_PATH);
    // private static part2 part2 = new part2(STOPS_PATH);

    private static part1 part1;
    private static part2 part2;
    private static part3 part3;

    public static void initParts() throws IOException {
        part1 = new part1(STOPS_PATH, STOP_TIMES_PATH, TRANSFERS_PATH);
        part2 = new part2(STOPS_PATH);
        part3 = new part3(STOP_TIMES_PATH);
    }

    private class Switcher implements ActionListener {
        String card;

        Switcher(String card) {
            this.card = card;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cards.show(cardHolder, card);
        }
    }

    private JPanel makePart1Panel() {
        try {
            // part1 part1 = new part1();
            final JLabel label2 = new JLabel();
            label2.setHorizontalAlignment(JLabel.CENTER);
            label2.setVerticalAlignment(JLabel.CENTER);
            label2.setSize(55, 10);
            label2.setText("Page 2");

            JPanel part1Panel = new JPanel();
            JButton buttonNextB = new JButton("Next");
            buttonNextB.setBounds(10, 100, 90, 20);
            JButton buttonBackB = new JButton("Back");
            buttonBackB.setBounds(600, 100, 90, 20);

            // String[] columnLabels = {
            // "stop_id","stop_code","stop_name","stop_desc","stop_lat","stop_lon","zone_id","stop_url","location_type","parent_station"};
            // String[] stopColumnLabels = getColumnNames(stops);

            // String[] columnLabels = { "order", "a", "b", "c", "d" };
            part1.setupGraphFiles();
            String[] stopColumnLabels = part1.getColumnNamesFromStops();
            String[] columnLabels = { "order", stopColumnLabels[0], stopColumnLabels[1], stopColumnLabels[2],
                    stopColumnLabels[6] };
            String[][] tableData = new String[20][5];

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

            part1Panel.add(scrollPane, BorderLayout.CENTER);

            final JLabel cost_label = new JLabel();
            // count_label.setBounds(250, -15, 100, 20);
            cost_label.setHorizontalAlignment(JLabel.CENTER);
            cost_label.setVerticalAlignment(JLabel.CENTER);
            cost_label.setSize(table.getWidth(), table.getHeight() * 160 / 100);

            final JLabel path_label = new JLabel();
            path_label.setHorizontalAlignment(JLabel.CENTER);
            path_label.setVerticalAlignment(JLabel.CENTER);
            path_label.setSize(table.getWidth(), table.getHeight() * 160 / 100);

            JLabel error_label = new JLabel();
            error_label.setSize(table.getWidth(), table.getHeight() * 160 / 100);
            error_label.setForeground(Color.RED);
            error_label.setHorizontalAlignment(JLabel.CENTER);
            error_label.setVerticalAlignment(JLabel.CENTER);

            JTextField tf1 = new JTextField(7);
            JTextField tf2 = new JTextField(7);
            tf1.setBounds(225, 300, 150, 20);
            tf2.setBounds(425, 300, 150, 20);

            JLabel fromLabel = new JLabel();
            fromLabel.setText("From Stop ID");
            fromLabel.setBounds(225, 400, 500, 20);
            fromLabel.setHorizontalAlignment(JLabel.CENTER);
            fromLabel.setVerticalAlignment(JLabel.CENTER);

            JLabel toLabel = new JLabel();
            toLabel.setText("Dest. Stop ID");
            toLabel.setBounds(225, 400, 500, 20);
            toLabel.setHorizontalAlignment(JLabel.CENTER);
            toLabel.setVerticalAlignment(JLabel.CENTER);

            JButton inputButton = new JButton("Shortest Path");
            inputButton.setBackground(Color.BLACK);
            inputButton.setForeground(Color.RED);

            buttonNextB.addActionListener(new Switcher(cardC));
            buttonBackB.addActionListener(new Switcher(cardA));
            part1Panel.add(buttonNextB);

            part1Panel.add(fromLabel);
            part1Panel.add(tf1);
            part1Panel.add(toLabel);
            part1Panel.add(tf2);
            part1Panel.add(inputButton);
            part1Panel.add(buttonBackB);
            part1Panel.add(cost_label);
            part1Panel.add(path_label);
            part1Panel.add(error_label);

            inputButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        int fromStopID = Integer.parseInt(tf1.getText());
                        int toStopID = Integer.parseInt(tf2.getText());

                        if (part1.routes.isValidStopId(fromStopID) && part1.routes.isValidStopId(toStopID)) {
                            System.out
                                    .println("Inputs start stop - " + tf1.getText() + " dest stop - " + tf2.getText());
                            // int fromStopID = 71;
                            // int toStopID = 646;
                            // printShortestPathInfo(fromStopID, toStopID);
                            ArrayList<Integer> shortestPath = part1.routes.getShortestPath(fromStopID, toStopID);
                            ArrayList<Stop> enrouteDetails = part1.routes.getEnrouteStops(shortestPath);
                            String[][] tableData = new String[enrouteDetails.size()][5];
                            double shortestCost = part1.routes.getShortestPathCost();

                            if (shortestCost == Double.POSITIVE_INFINITY) {
                                path_label.setText("No route from from " + fromStopID + " to " + toStopID);
                            } else if (shortestCost == Double.NEGATIVE_INFINITY) {
                                path_label.setText("both from and to have the same stop IDs");
                            } else if (shortestCost == -1.0) {
                                // System.out.println("Invalid input");
                                throw new IllegalArgumentException();
                            } else {

                                for (int i = 0; i < enrouteDetails.size(); i++) {
                                    Stop stop = enrouteDetails.get(i);
                                    tableData[i][0] = String.valueOf(i + 1);
                                    tableData[i][1] = (stop.stop_id == -1) ? "" : String.valueOf(stop.stop_id);
                                    tableData[i][2] = (stop.stop_code == -1) ? "" : String.valueOf(stop.stop_code);
                                    tableData[i][3] = stop.stop_name;
                                    tableData[i][4] = stop.zone_id;
                                }
                                for (int i = 0; i < 20; i++) {
                                    if (tableData[i].length == 0) {
                                        tableData[i] = new String[columnLabels.length];
                                    }
                                }
                                dtm.setDataVector(tableData, columnLabels);
                                String costLabelStr = "The cost associated with moving from " + fromStopID + " to "
                                        + toStopID + " is " + shortestCost;

                                String pathStr = "";
                                for (int i = 0; i < shortestPath.size(); i++) {
                                    pathStr.concat(String.valueOf(shortestPath.get(i)));
                                    // System.out.print(shortestPath.get(i));
                                    if (i != shortestPath.size() - 1) {
                                        pathStr.concat(" -> ");
                                        // System.out.print(" -> ");
                                    }
                                }
                                error_label.setText("");
                                cost_label.setText(costLabelStr);
                                // path_label.setText(pathStr);
                                // System.out.println();
                            }

                        } else
                            throw new IllegalArgumentException();

                    } catch (NumberFormatException nfe) {
                        String errorMsg = "Invalid input, please check the stop IDs you have entered.";
                        // System.out.println(errorMsg);
                        cost_label.setText("");
                        path_label.setText("");
                        error_label.setText(errorMsg);
                        String[][] emptyData = new String[20][columnLabels.length];
                        dtm.setDataVector(emptyData, columnLabels);
                    } catch (IllegalArgumentException iae) {
                        String errorMsg = "Invalid input, please check the stop IDs you have entered.";
                        // System.out.println(errorMsg);
                        cost_label.setText("");
                        path_label.setText("");
                        error_label.setText(errorMsg);
                        String[][] emptyData = new String[20][columnLabels.length];
                        dtm.setDataVector(emptyData, columnLabels);
                    }
                }
            });

            return part1Panel;
        } catch (IOException ib) {
            return null;
        }
    }

    private JPanel makePart2Panel() {
        try {
            // partpart2 = new part2(STOPS_PATH);
            Map<String, ArrayList<String[]>> stopDetails = part2.createNameDetailsMapFromStops();

            JPanel part2Panel = new JPanel();

            part2Panel.setPreferredSize(d);
            part2Panel.setMaximumSize(part2Panel.getMaximumSize());
            JButton buttonNextC = new JButton("Next");
            buttonNextC.setBounds(10, 100, 90, 20);
            JButton buttonBackC = new JButton("Back");
            buttonBackC.setBounds(600, 100, 90, 20);

            buttonNextC.addActionListener(new Switcher(cardD));
            buttonBackC.addActionListener(new Switcher(cardB));

            JButton inputButton = new JButton("Show");
            inputButton.setBackground(Color.BLACK);
            inputButton.setForeground(Color.RED);

            inputButton.setBounds(625, 300, 90, 20);

            String[] columnLabels = part2.getColumnNamesFromStopTimes();
            String[][] tableData = new String[20][10];

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

            part2Panel.add(scrollPane, BorderLayout.CENTER);

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

            JLabel error_label = new JLabel();
            error_label.setSize(table.getWidth(), table.getHeight() * 160 / 100);
            error_label.setForeground(Color.RED);
            error_label.setHorizontalAlignment(JLabel.CENTER);
            error_label.setVerticalAlignment(JLabel.CENTER);

            JTextField tf1 = new JTextField(20);
            tf1.setBounds(425, 300, 150, 20);
            tf1.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent fe) {
                    if (tf1.getText().equals("Dest Stop ID")) {
                        tf1.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent fe) {
                    if (tf1.getText().isEmpty()) {
                        tf1.setText("Search Stop Name");
                    }
                }
            });

            inputButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String input = tf1.getText();
                        Pattern p = Pattern.compile("[^a-z0-9\\- ]", Pattern.CASE_INSENSITIVE); // in order to check for
                                                                                                // an empty string input
                        Matcher m = p.matcher(input);
                        boolean b = m.find();
                        if ((!input.equals("") && !input.equals(" ") && !input.substring(0, 2).equals("  ")) && !b) {
                            String displayData = "Input String - " + input;

                            System.out.println(displayData);
                            String searchInput = part2.removeSpacesAndCapitlize(input);

                            String[] searchResults = part2.STOP_NAMES_TST.search(searchInput);
                            if (searchResults == null) {
                                String errorMessage = "Sorry, There doesn't seem to be any buses at the time you have selected";
                                count_label.setText("");
                                error_label.setHorizontalAlignment(JLabel.CENTER);
                                error_label.setText(errorMessage);
                                System.out.println(errorMessage);
                                // dtm.setDataVector(null, columnLabels);
                                String[][] emptyData = new String[20][columnLabels.length];
                                dtm.setDataVector(emptyData, columnLabels);
                            }
                            ArrayList<String[]> lines = new ArrayList<>();
                            for (String res : searchResults) {
                                ArrayList<String[]> details = stopDetails.get(res);
                                for (String[] d : details) {
                                    lines.add(d);
                                }
                            }
                            if (lines.size() == 0)
                                throw new NullPointerException();
                            String[][] stopsData;
                            stopsData = new String[lines.size()][columnLabels.length];
                            for (int i = 0; i < stopsData.length; i++) {
                                stopsData[i] = lines.get(i);
                            }
                            int stopsCount = stopsData.length;
                            error_label.setText("");
                            count_label.setText("\nThere seem to be " + stopsCount
                                    + " matching bus stops with the your input text");

                            dtm.setDataVector(stopsData, columnLabels);
                        } else
                            throw new IllegalArgumentException();
                    } catch (NullPointerException np) {
                        String errorMessage = "Sorry, There doesn't seem to be any bus stops that matches your input.";
                        error_label.setHorizontalAlignment(JLabel.CENTER);
                        // count_label.alignCenter();
                        error_label.setText(errorMessage);
                        count_label.setText("");
                        // System.out.println(errorMessage);
                        // dtm.setDataVector(null, columnLabels);
                        String[][] emptyData = new String[20][columnLabels.length];
                        dtm.setDataVector(emptyData, columnLabels);
                    } catch (IllegalArgumentException ie) {
                        String errorMsg = "Please enter something valid";
                        // System.out.println("print something valid");
                        error_label.setText(errorMsg);
                        count_label.setText("");
                        String[][] emptyData = new String[20][columnLabels.length];
                        dtm.setDataVector(emptyData, columnLabels);
                    }

                }
            });

            part2Panel.add(buttonNextC);

            part2Panel.add(tf1);

            part2Panel.add(inputButton);

            part2Panel.add(buttonBackC);

            part2Panel.add(count_label);
            part2Panel.add(error_label);

            return part2Panel;
        } catch (IOException ib) {
            return null;
        }
    }

    private JPanel makePart3Panel() {
        try {

            Map<String, ArrayList<String>> arrivalTimes_String = part3.createSortedArrivalTimeMapFromStopTimes();
            JPanel part3Panel = new JPanel();

            part3Panel.setPreferredSize(d);
            part3Panel.setMaximumSize(part3Panel.getMaximumSize());
            JButton buttonNextD = new JButton("Home");
            buttonNextD.setBounds(10, 100, 90, 20);
            JButton buttonBackD = new JButton("Back");
            buttonBackD.setBounds(600, 100, 90, 20);

            buttonNextD.addActionListener(new Switcher(cardA));
            buttonBackD.addActionListener(new Switcher(cardC));

            JButton timeInputButton = new JButton("Show");
            timeInputButton.setBackground(Color.BLACK);
            timeInputButton.setForeground(Color.RED);

            timeInputButton.setBounds(625, 300, 90, 20);

            String hours[] = { "  0", "  1", "  2", "  3", "  4", "  5", "  6", "  7", "  8", "  9", " 10", " 11",
                    " 12", " 13", " 14", " 15", " 16", " 17", " 18", " 19", " 20", " 21", " 22", " 23" };

            String minutes_seconds[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                    "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
                    "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
                    "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };

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

            String[] columnLabels = part3.getColumnNamesFromStopTimes();
            // String[] columnLabels = new String[10];
            String[][] tableData = new String[20][10];

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
            table.setLayout(new BorderLayout());

            int N_ROWS = tableData.length;

            Dimension td = new Dimension(800, N_ROWS * table.getRowHeight());
            table.setPreferredScrollableViewportSize(td);
            TableColumn column = null;

            for (int i = 0; i < columnLabels.length; i++) {
                column = table.getColumnModel().getColumn(i);
                column.setPreferredWidth(columnLabels[i].length() * 10);
            }

            for (int i = 0; i < N_ROWS; i++) {
                dtm.addRow(tableData[i]);
            }

            final JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);

            label.setSize(table.getWidth(), table.getHeight() * 135 / 100);

            final JLabel count_label = new JLabel();
            count_label.setHorizontalAlignment(JLabel.CENTER);
            count_label.setVerticalAlignment(JLabel.CENTER);
            count_label.setSize(table.getWidth(), table.getHeight() * 160 / 100);

            part3Panel.add(scrollPane, BorderLayout.CENTER);

            timeInputButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    String time = hoursInput.getItemAt(hoursInput.getSelectedIndex()) + ":"
                            + minutesInput.getItemAt(minutesInput.getSelectedIndex()) + ":"
                            + secondsInput.getItemAt(secondsInput.getSelectedIndex());
                    String displayData = "Time you've input is " + time;
                    count_label.setText("");
                    label.setText(displayData);

                    System.out.println(displayData);

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
                        tripsData = part3.sortTripsBasedOnID(tripsData); // sorting trips based on their id using bubble
                                                                         // sort
                        // for
                        // this
                        dtm.setDataVector(tripsData, columnLabels);
                    } catch (NullPointerException np) {
                        String errorMessage = "Sorry, There doesn't seem to be any buses at the time you have selected";
                        count_label.setHorizontalAlignment(JLabel.CENTER);
                        // count_label.alignCenter();
                        count_label.setText(errorMessage);
                        System.out.println(errorMessage);
                        // dtm.setDataVector(null, columnLabels);
                        String[][] emptyData = new String[20][columnLabels.length];
                        dtm.setDataVector(emptyData, columnLabels);
                    }

                }
            });

            part3Panel.add(buttonNextD);

            part3Panel.add(hoursLabel);
            part3Panel.add(hoursInput);

            part3Panel.add(minutesLabel);
            part3Panel.add(minutesInput);

            part3Panel.add(secondsLabel);
            part3Panel.add(secondsInput);

            part3Panel.add(timeInputButton);

            part3Panel.add(buttonBackD);

            part3Panel.add(count_label);
            // pd.add(label);

            // pd.add(label4);
            return part3Panel;
        } catch (IOException ig) {
            return null;
        }

    }

    private JPanel makeIntroPanel() {
        try {
            final JLabel label1 = new JLabel();
            label1.setHorizontalAlignment(JLabel.CENTER);
            label1.setVerticalAlignment(JLabel.CENTER);
            // label1.setSize(55, 10);
            // label1.setPrefferedSize(200, 40);
            Dimension d1 = new Dimension(820, 150);

            label1.setPreferredSize(d1);
            label1.setText("CSU22012: Data Structures and Algorithms Group Project");
            label1.setFont(new Font("Myriad Pro", Font.PLAIN, 25));

            JPanel pa = new JPanel();
            JButton buttonPartA = new JButton("Part 1");
            JButton buttonPartB = new JButton("Part 2");
            JButton buttonPartC = new JButton("Part 3");

            buttonPartA.addActionListener(new Switcher(cardB));
            buttonPartB.addActionListener(new Switcher(cardC));
            buttonPartC.addActionListener(new Switcher(cardD));

            BufferedImage myPicture = ImageIO.read(
                    new File("/Users/johnwesley/Desktop/Algos /Sem2/ADS-MOCK-TRIAL/inputs/translinklogo1 (1).png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            Dimension d2 = new Dimension(820, 250);
            picLabel.setPreferredSize(d2);

            final JLabel paddingLabel1 = new JLabel();
            paddingLabel1.setHorizontalAlignment(JLabel.CENTER);
            paddingLabel1.setVerticalAlignment(JLabel.CENTER);
            paddingLabel1.setText("      ");

            final JLabel paddingLabel2 = new JLabel();
            paddingLabel2.setHorizontalAlignment(JLabel.CENTER);
            paddingLabel2.setVerticalAlignment(JLabel.CENTER);
            paddingLabel2.setText("      ");

            pa.add(label1);
            pa.add(picLabel);
            pa.add(buttonPartA);
            pa.add(paddingLabel1);
            pa.add(buttonPartB);
            pa.add(paddingLabel2);
            pa.add(buttonPartC);

            // pa.setBackground(Color.CYAN);
            return pa;
        } catch (IOException ioe) {
            return null;
        }
    }

    private void run() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // pa.setLayout(null);
        // pa.setSize(820, 600);

        JPanel pa = makeIntroPanel();

        JPanel pb = makePart1Panel();

        JPanel pc = makePart2Panel();

        JPanel pd = makePart3Panel();

        cardHolder = new JPanel();
        cards = new CardLayout();
        cardHolder.setLayout(cards);
        cardHolder.add(pa, cardA);
        cardHolder.add(pb, cardB);
        cardHolder.add(pc, cardC);
        cardHolder.add(pd, cardD);

        cardHolder.setPreferredSize(d);
        cardHolder.setMaximumSize(cardHolder.getPreferredSize());
        // cardHolder.setSize(820, 600);
        add(cardHolder);

        pack();

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            initParts();
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    new part4().run();
                }
            });
        } catch (Exception ex) {
        }
    }
}

// taken from
// https://www.tutorialspoint.com/how-to-set-a-tooltip-to-each-column-of-a-jtableheader-in-java
// implementation code to set a tooltip text to each column of JTableHeader
class ToolTipHeader extends JTableHeader {
    String[] toolTips;

    public ToolTipHeader(TableColumnModel model) {
        super(model);
    }

    public String getToolTipText(MouseEvent e) {
        int col = columnAtPoint(e.getPoint());
        int modelCol = getTable().convertColumnIndexToModel(col);
        String retStr;
        try {
            retStr = toolTips[modelCol];
        } catch (NullPointerException ex) {
            retStr = "";
        } catch (ArrayIndexOutOfBoundsException ex) {
            retStr = "";
        }
        if (retStr.length() < 1) {
            retStr = super.getToolTipText(e);
        }
        return retStr;
    }

    public void setToolTipStrings(String[] toolTips) {
        this.toolTips = toolTips;
    }
}
