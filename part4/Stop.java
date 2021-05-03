public class Stop {
    public int stop_id;
    public int stop_code;
    public String stop_name;
    public String stop_desc;
    public double stop_lat;
    public double stop_lon;
    public String zone_id;
    public String stop_url;
    public int location_type;
    public String parent_station;

    public Stop(int stop_id, int stop_code, String stop_name, String stop_desc, double stop_lat, double stop_lon,
            String zone_id, String stop_url, int location_type, String parent_station) {
        this.stop_id = stop_id;
        this.stop_code = stop_code;
        this.stop_name = stop_name;
        this.stop_desc = stop_desc;
        this.stop_lat = stop_lat;
        this.stop_lon = stop_lon;
        this.zone_id = zone_id;
        this.stop_url = stop_url;
        this.location_type = location_type;
        this.parent_station = parent_station;
    }

    public void printStopDetails() {
        String ColumnLabel0 = "stop_id - ";
        String ColumnLabel1 = ", stop_code - ";
        String ColumnLabel2 = ", stop_name - ";
        String ColumnLabel3 = ", stop_desc - ";
        String ColumnLabel4 = ", stop_lat - ";
        String ColumnLabel5 = ", stop_lon - ";
        String ColumnLabel6 = ", zone_id - ";
        String ColumnLabel7 = ", stop_url - ";
        String ColumnLabel8 = ", location_type - ";
        String ColumnLabel9 = ", parent_station - ";

        System.out.println(ColumnLabel0 + stop_id + ColumnLabel1 + stop_code + ColumnLabel2 + stop_name + ColumnLabel3
                + stop_desc + ColumnLabel4 + stop_lat + ColumnLabel5 + stop_lon + ColumnLabel6 + zone_id + ColumnLabel7
                + stop_url + ColumnLabel8 + location_type + ColumnLabel9 + parent_station);
    }
}
