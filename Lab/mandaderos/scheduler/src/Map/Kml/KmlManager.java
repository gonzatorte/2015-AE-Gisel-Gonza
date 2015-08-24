package Map.Kml;

import Map.Place;
import de.micromata.opengis.kml.v_2_2_0.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
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
    
    public void load_Solution_From_KML(String filename, Schedule sched){
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
                        try {
                            sched.currentMapa.addPlace(place);
                        } catch (SAXException ex) {
                            Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParserConfigurationException ex) {
                            Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        mandadero.add(place);
                    }              
                }
                sched.tasks_queues.add(mandadero);
            }
        }
    }
    /*
    La idea es crear un kml con puntos y folders donde cada folder es un mandadero
    */
    public void load_Init_Problem_From_KML(String filename, Schedule sched){
        File file = new File(filename);
        Kml kml2 = Kml.unmarshal(file);
        Document documento = (Document) kml2.getFeature ();
        List <Feature> t = documento.getFeature (); 
        for(Object o : t){
            if (o instanceof Folder){
                sched.tasks_queues.add(new MandaderoTaskQueue());
            }
            if (o instanceof Placemark){
                Placemark placemark = (Placemark)o;
                Point point = (Point) placemark.getGeometry();
                Double latitud = point.getCoordinates().get(0).getLatitude();
                Double longitud = point.getCoordinates().get(0).getLongitude();
                //el id tome el nombre que se le pone a la etiqueta porque el que retorna google es null
                try {
                    sched.currentMapa.addPlace(new Place(placemark.getName(),latitud,longitud));
                } catch (SAXException ex) {
                    Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void start_Kml(String filename){
        kml = new Kml();
        document = kml.createAndSetDocument().withName(filename);
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
    public String get_DateNow_to_TimeSpan(){
        Calendar cal1 = Calendar.getInstance();
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
            lastTimeSpan.setEnd(get_DateNow_to_TimeSpan());
            lastFolderTimeSpan.setTimePrimitive(lastTimeSpan);
        }
        Folder folder1 = document.createAndAddFolder().withName("EventTime: "+ event.time);
        lastFolderTimeSpan = folder1;
        lastTimeSpan= new TimeSpan();
        lastTimeSpan.setBegin(get_DateNow_to_TimeSpan());
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
            kml.marshal(new File("Solution.kml"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
