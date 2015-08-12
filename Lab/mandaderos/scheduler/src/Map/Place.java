package Map;

public class Place{
    public String place_id;
    public Coordinate coord;
    public Place(String place_id, double latit, double longit){
        this.coord = new Coordinate(latit, longit);
        this.place_id = place_id;
    }
}
