package Map.Kml;

import Map.Mapa;
import Map.MapaGenerator;
import Map.Place;
import com.almworks.sqlite4java.SQLiteException;
import de.micromata.opengis.kml.v_2_2_0.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import scheduler.events.Event;
import scheduler.problem.MandaderoTaskQueue;
import scheduler.problem.ProblemInstance;
import scheduler.problem.Schedule;

public class KmlManager {
    int color_depth = 0;
    int color_count = 0;
    int color_offset = 256;
    int mandaderos_count = 0;
    Folder lastFolderTimeSpan = null;
    TimeSpan lastTimeSpan = null;
    Kml kml;
    private Document document;
    Calendar now = Calendar.getInstance();
    int offsetNow = 10; // en minutos
    
    
    public void load_Solution_From_KML(String filename, Schedule sched) throws SQLiteException{
        File file = new File(filename);
        Kml kml2 = Kml.unmarshal(file);
        Document documento = (Document) kml2.getFeature ();
        List <Feature> t = documento.getFeature (); 
        for(Object o : t){
            if (o instanceof Folder){
                Folder f = (Folder)o;
                List<Feature> placemarks = f.getFeature();
                MandaderoTaskQueue mandadero = new MandaderoTaskQueue();
                for(Object obj : placemarks){
                    Placemark placemark = (Placemark) obj;
                    if (placemark.getGeometry() instanceof Point){
                        Point point = (Point) placemark.getGeometry();
                        Double latitud = point.getCoordinates().get(0).getLatitude();
                        Double longitud = point.getCoordinates().get(0).getLongitude();
                        //el id tome el nombre que se le pone a la etiqueta porque el que retorna google es null
                        Place place = new Place(placemark.getName(),latitud,longitud);
                        sched.problem.mapa.addPlace(place);
                        mandadero.add(place);
                    }              
                }
                sched.tasks_queues.add(mandadero);
            }
        }
    }
    /*
    La idea es crear un kml con puntos para indicar cada uno de los places
    */
    public static LinkedList<Place> get_places_From_KML(String kmlPath){
        File file = new File(kmlPath);
        System.out.println(kmlPath);
        Kml kml2 = Kml.unmarshal(file);
        Document documento = (Document) kml2.getFeature ();
        List <Feature> t = documento.getFeature (); 
        int cantMandaderos=0;
        LinkedList<Place> lp= new LinkedList<Place>();
        for(Object o : t){
            if (o instanceof Folder){
                Folder folder = (Folder)o;
                List<Feature> lf = folder.getFeature();
                for(Object ob : lf){
                    if (ob instanceof Placemark){
                        Placemark placemark = (Placemark)ob;
                        Point point = (Point) placemark.getGeometry();
                        Double latitud = point.getCoordinates().get(0).getLatitude();
                        Double longitud = point.getCoordinates().get(0).getLongitude();
                        //el id tome el nombre que se le pone a la etiqueta porque el que retorna google es null
                        lp.add(new Place(placemark.getName(),latitud,longitud));
                        System.out.println(placemark.getName());
                    }
                }
            }
        }
        return lp;
    }
    
    public KmlManager(String filename){
        kml = new Kml();
        document = kml.createAndSetDocument().withName(filename);
    }
    public KmlManager(){
        kml = new Kml();
        document = kml.createAndSetDocument().withName("Solution");
    }
    /*
    Agrega un timeSpan para todos los mandaderos involucrados
    */
    public int get_Color(){
        if (mandaderos_count < Math.pow(2, color_depth+1) - 1){
            color_count += color_offset;
        } else {
            color_offset = color_offset/2;
            color_count = color_offset;
            color_depth++;
        }
        return color_count;
    }
    public String add_cero(int s){
        if (s<10){
            return "0"+s;
        }
        return ""+s;
    }
    //formato para timespan: 1999-07-16T07:30:15Z AAAA-MM-DDTHH:MM:SSZ
    public String get_Date_to_TimeSpan(Event event){
        Calendar cal1 = (Calendar)now.clone();
        cal1.add(Calendar.MINUTE,event.time*offsetNow);
        return cal1.get(Calendar.YEAR)+"-"+add_cero(cal1.get(Calendar.MONTH)+1)
    +"-"+add_cero(cal1.get(Calendar.DATE))+"T"+add_cero(cal1.get(Calendar.HOUR_OF_DAY))
    +":"+add_cero(cal1.get(Calendar.MINUTE))+":"+add_cero(cal1.get(Calendar.SECOND))+"Z";
    }
    public void apply_reschedule(ProblemInstance prob, Schedule sched, Event event){
        color_depth = 0;
        color_count = 0;
        color_offset = 256;
        mandaderos_count = sched.tasks_queues.size();       
        if (lastFolderTimeSpan != null){
            lastTimeSpan.setEnd(get_Date_to_TimeSpan(event));
            lastFolderTimeSpan.setTimePrimitive(lastTimeSpan);
        }
        Folder folder1 = document.createAndAddFolder().withName("EventTime: "+ event.time);
        lastFolderTimeSpan = folder1;
        lastTimeSpan= new TimeSpan();
        lastTimeSpan.setBegin(get_Date_to_TimeSpan(event));
        for (int i=0; i<sched.tasks_queues.size();i++){
            Folder folder = folder1.createAndAddFolder().withName("Mandadero "+ i);
            MandaderoTaskQueue mtq= sched.tasks_queues.get(i);
            Placemark placemark =  folder.createAndAddPlacemark();
            placemark.createAndAddStyle().createAndSetLineStyle().setColor(Integer.toHexString(get_Color()));
            LineString linestring = placemark.createAndSetLineString();
            Place place;
            for (Place mtq1 : mtq) {
                place = mtq1;
                linestring.addToCoordinates(place.coord.longit,place.coord.latit);
            }
            for (Place mtq1 : mtq) {
                place = mtq1;
                folder.createAndAddPlacemark().createAndSetPoint().addToCoordinates(place.coord.longit,place.coord.latit);
            }
        }
    }
    
    public void write_kml(String sched_id){
        try {
            if (lastFolderTimeSpan != null){
                lastFolderTimeSpan.setTimePrimitive(lastTimeSpan);
            }
            kml.marshal(new File(sched_id));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
