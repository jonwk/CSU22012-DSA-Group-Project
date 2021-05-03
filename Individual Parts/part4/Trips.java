import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Trips {
    public ArrayList<TripDetails> validData;

    public Trips(File stop_times) throws IOException {
        validData = new ArrayList<>();
        readStopTimes(stop_times);
    }

    public void readStopTimes(File stop_times) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(stop_times));
        String st;
        int lineCount = 0;
        try {
            while ((st = br.readLine()) != null) {
                String[] line = st.split(",");
                int emptyCodeInt = -1;
                float emptyCodeFloat = -1;
                if (lineCount != 0) {
                    int trip_id = emptyCodeInt;
                    int stop_id = emptyCodeInt;
                    int stop_sequence = emptyCodeInt;
                    int stop_headsign = emptyCodeInt;
                    int pickup_type = emptyCodeInt;
                    int drop_off_type = emptyCodeInt;
                    float shape_dist_traveled = emptyCodeFloat;

                    String arrival_time = line[1];
                    String departure_time = line[2];

                    if (!line[0].equals("")) {
                        trip_id = Integer.parseInt(line[0]);
                    }
                    if (!line[3].equals("")) {
                        stop_id = Integer.parseInt(line[3]);
                    }
                    if (!line[4].equals("")) {
                        stop_sequence = Integer.parseInt(line[4]);
                    }
                    if (!line[5].equals("")) {
                        stop_headsign = Integer.parseInt(line[5]);
                    }
                    if (!line[6].equals("")) {
                        pickup_type = Integer.parseInt(line[6]);
                    }
                    if (!line[7].equals("")) {
                        drop_off_type = Integer.parseInt(line[7]);
                    }
                    if ((line.length == 9) && !line[8].equals("")) {
                        shape_dist_traveled = Float.parseFloat(line[8]);
                    }
                    if (isTimeValid(arrival_time) && isTimeValid(departure_time)) {
                        validData.add(new TripDetails(trip_id, arrival_time, departure_time, stop_id, stop_sequence,
                                stop_headsign, pickup_type, drop_off_type, shape_dist_traveled));
                    }
                }
                lineCount++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Reading Stop Times Complete Line Count - " + lineCount);
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
        if((hours<=MAX_HOURS)&&(minutes<=MAX_MINUTES)&&(seconds<=MAX_SECONDS))
            return true;
        else
            return false;
    }

}
