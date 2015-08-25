package Map;

import java.io.Serializable;
import java.util.Locale;

public class Coordinate implements Serializable, Comparable<Coordinate>{
    public double latit;
    public double longit;
    private static final String coordinate_format = "%f,%f";

    public Coordinate(double latit, double longit){
        this.latit = latit;
        this.longit = longit;
    }
    
    @Override
    public String toString(){
        return String.format(Locale.ENGLISH, coordinate_format, this.latit, this.longit);
    }

    public static Coordinate fromString(String str_coord){
        String[] splited = str_coord.split(",");
        double latitud = Double.parseDouble(splited[0]);
        double longitud = Double.parseDouble(splited[1]);
        return new Coordinate(latitud, longitud);
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
