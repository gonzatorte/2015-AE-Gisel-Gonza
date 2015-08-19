package Map;

import java.io.Serializable;

public class Coordinate implements Serializable{
    public double latit;
    public double longit;

    public Coordinate(double latit, double longit){
        this.latit = latit;
        this.longit = longit;
    }    
}
