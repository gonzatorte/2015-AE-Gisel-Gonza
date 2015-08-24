package scheduler.problem;

import Map.Mapa;
import Map.MapaGenerator;
import Map.Place;
import java.util.LinkedList;
import scheduler.events.Event;

public class ProblemInstance {
    public int step = 0;
    public Mapa mapa;
    public LinkedList<Place> origin_mandaderos;

    public ProblemInstance(Mapa mapa){
        this.mapa = mapa;
        this.origin_mandaderos = new LinkedList<Place>();
    }
    
    public ProblemInstance(Mapa mapa, LinkedList<Place> origin_mandaderos){
        this.mapa = mapa;
        this.origin_mandaderos = origin_mandaderos;
    }

    public void applyEvent(Event event) {
        if ("addMandadero".equals(event.tipo)){
            String place_id = event.data;
            Place origin = this.mapa.findPlaceById(place_id);
            this.origin_mandaderos.add(origin);
        } else if ("removeMandadero".equals(event.tipo)){
            String place_id = event.data;
            Place origin = this.mapa.findPlaceById(place_id);
            this.origin_mandaderos.remove(origin);
        } else if ("addPlace".equals(event.tipo)){
            String place_id = event.data;
            //ToDo: Obtengo bajo demanda el lugar desde google y/o la distancia
        } else if ("removePlace".equals(event.tipo)){
            String place_id = event.data;
            //Elimino de los origenes al place, puede ser que tambien del mapa
            //Esto tiene que tener la misma semantica que su contraparte en 
            //el solver
        } else {
            throw new Error("No hay este tipo de evento");
        }
    }
    
    public static ProblemInstance test_case(){
        Mapa mapa = MapaGenerator.test_data();
        LinkedList<Place> origins = new LinkedList<Place>();
        origins.add(mapa.places.get(0));
        return new ProblemInstance(mapa, origins);
    }
}
