public class TripDetails {
    int trip_id;
    String arrival_time;
    String departure_time;
    int stop_id;
    int stop_sequence;
    int stop_headsign;
    int pickup_type;
    int drop_off_type;
    float shape_dist_traveled;

    // trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type,shape_dist_traveled

    public TripDetails(int trip_id, String arrival_time, String departure_time, int stop_id, int stop_sequence,
            int stop_headsign, int pickup_type, int drop_off_type, float shape_dist_traveled) {
        this.trip_id = trip_id;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.stop_id = stop_id;
        this.stop_sequence = stop_sequence;
        this.stop_headsign = stop_headsign;
        this.pickup_type = pickup_type;
        this.drop_off_type = drop_off_type;
        this.shape_dist_traveled = shape_dist_traveled;
    }

    public void printTripDetails() {
        String ColumnLabel0 = "trip_id - ";
        String ColumnLabel1 = " arrival_time - ";
        String ColumnLabel2 = " departure_time - ";
        String ColumnLabel3 = " stop_id - ";
        String ColumnLabel4 = " stop_sequence - ";
        String ColumnLabel5 = " stop_headsign - ";
        String ColumnLabel6 = " pickup_type - ";
        String ColumnLabel7 = " drop_off_type - ";
        String ColumnLabel8 = " shape_dist_traveled - ";
        System.out.println(ColumnLabel0 + trip_id + ColumnLabel1 + arrival_time + ColumnLabel2 + departure_time
                + ColumnLabel3 + stop_id + ColumnLabel4 + stop_sequence + ColumnLabel5 + stop_headsign + ColumnLabel6
                + pickup_type + ColumnLabel7 + drop_off_type + ColumnLabel8 + shape_dist_traveled);
    }
}
