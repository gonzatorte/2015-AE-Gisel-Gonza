package Map.Kml;

import Map.Place;
import de.micromata.opengis.kml.v_2_2_0.AbstractObject;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
    Kml kml;
    
    public void loadSolutionFromKML(String filename, Schedule sched){
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
    public void loadInitProblemFromKML(String filename, Schedule sched){
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
    
    
    /*
    Agrega un timeSpan para todos los mandaderos involucrados
    */
    public void apply_reschedule(ProblemInstance prob, Schedule sched, Event event){
        
        
    }
    
    /*
    Agrega un layer con el recorrido de un nuevo mandadero
    */
    public void add_mandadero(Schedule sched){
        if (mandaderos_count < Math.pow(mandaderos_count, color_depth)){
            color_count += color_offset;
        } else {
            color_offset = color_offset/2;
            color_count = color_offset;
            color_depth++;
        }
        mandaderos_count++;
    }
    
    /*
    Agrega un timeSpan para todos los mandaderos involucrados
    */
    public void remove_mandadero(Schedule sched){
        if (Math.pow(mandaderos_count, color_depth) < mandaderos_count){
            color_count -= color_offset;
        } else {
            color_offset = color_offset*2;
            color_count = color_offset;
            color_depth--;
        }
        mandaderos_count--;
    }
    
    public void write_kml(String sched_id){
        try {
            kml.marshal(new File("Solution.kml"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KmlManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
