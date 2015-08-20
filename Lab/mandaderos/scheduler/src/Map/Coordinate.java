package Map;

import java.io.Serializable;

public class Coordinate implements Serializable, Comparable<Coordinate>{
    public double latit;
    public double longit;

    public Coordinate(double latit, double longit){
        this.latit = latit;
        this.longit = longit;
    }    

    public int compareTo(Coordinate o2) {
        int cmp1;
        cmp1 = new Double(this.longit).compareTo(o2.longit);
        if (cmp1 == 0){
            return new Double(this.latit).compareTo(o2.latit);
        } else {
            return cmp1;
        }
    }
}
