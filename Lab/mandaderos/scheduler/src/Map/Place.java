package Map;

import java.io.Serializable;

public class Place implements Serializable, Comparable<Place> {
    public String place_id;
    public Coordinate coord;
    public Place(String place_id, double latit, double longit){
        this.coord = new Coordinate(latit, longit);
        this.place_id = place_id;
    }

    public int compareTo(Place p2) {
        return this.place_id.compareTo(p2.place_id);
    }
}
