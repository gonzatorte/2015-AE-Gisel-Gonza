package SerializableTest;
import Map.Mapa;
import Map.Coordinate;
import Map.LightDistanceTable;
import Map.Place;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class main {
    public static void main(String[] args) {
        Mapa map = new Mapa(new Coordinate(0,0),new Coordinate(3,3));
        Place p1 = new Place("0",1,1);
        Place p2 = new Place("1",2,2);
        List<Place> places = new ArrayList<Place>();
        places.add(p1);
        places.add(p2);
        map.places = places;
        LightDistanceTable all_distances = new LightDistanceTable();
        HashMap<Place, Double> interMap = new HashMap<Place, Double>();
        interMap.put(places.get(1), 1.5);
        all_distances.put(places.get(0),interMap);
        map.distances = all_distances;
        
        File f = new File("serializar.jbin");
        Mapa m;
        try {
            ObjectOutputStream oos= new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(map);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } 
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            m = (Mapa)ois.readObject();
            System.out.println(m.places.size());
        }
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
    }
    
}
