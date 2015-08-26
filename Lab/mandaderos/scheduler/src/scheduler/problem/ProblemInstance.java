package scheduler.problem;

import Map.Coordinate;
import Map.Mapa;
import Map.MapaGenerator;
import Map.Place;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import scheduler.events.Event;

public class ProblemInstance {
    public Mapa mapa;
    public LinkedList<Place> origin_mandaderos;
    public LinkedList<Place> pedidos;

    public ProblemInstance(Mapa mapa){
        this.mapa = mapa;
        this.origin_mandaderos = new LinkedList<Place>();
        this.pedidos = new LinkedList<Place>();
    }
    
    public ProblemInstance(Mapa mapa, LinkedList<Place> origin_mandaderos, LinkedList<Place> pedidos){
        this.mapa = mapa;
        this.origin_mandaderos = origin_mandaderos;
        this.pedidos = pedidos;
    }

    public void applyEvent(Event event) {
        if ("addMandadero".equals(event.tipo)){
            String place_id = (String) event.data[0];
            if (event.data.length > 1){
                Coordinate origin_coord = (Coordinate) event.data[1];
                Place newplace = new Place(place_id, origin_coord);
                this.mapa.addPlace(newplace);
                this.origin_mandaderos.add(newplace);
            } else {
                Place origin = this.mapa.findPlaceById(place_id);
                this.origin_mandaderos.add(origin);
            }
        } else if ("removeMandadero".equals(event.tipo)){
            String place_id = (String) event.data[0];
            Place origin = this.mapa.findPlaceById(place_id);
            this.origin_mandaderos.remove(origin);
        } else if ("addPlace".equals(event.tipo)){
            String place_id = (String) event.data[0];
            Coordinate origin_coord = (Coordinate) event.data[1];
            Place newplace = new Place(place_id, origin_coord);
            this.mapa.addPlace(newplace);
        } else if ("addPedido".equals(event.tipo)){
            String place_id = (String) event.data[0];
            if (event.data.length > 1){
                Coordinate origin_coord = (Coordinate) event.data[1];
                Place newplace = new Place(place_id, origin_coord);
                this.mapa.addPlace(newplace);
                this.pedidos.add(newplace);
            } else {
                Place place = this.mapa.findPlaceById(place_id);
                this.pedidos.add(place);
            }
        } else if ("resolvePedido".equals(event.tipo)){
            String place_id = (String) event.data[0];
            String origin_id = (String) event.data[1];
            Place place = this.mapa.findPlaceById(place_id);
            Place origin = this.mapa.findPlaceById(origin_id);
            this.pedidos.remove(place);
            this.origin_mandaderos.set(this.origin_mandaderos.indexOf(origin), place);
        } else {
            throw new Error("No hay este tipo de evento");
        }
    }
    
    public static ProblemInstance test_case(){
        Mapa mapa = MapaGenerator.test_data();
        LinkedList<Place> origins = new LinkedList<Place>();
        origins.add(mapa.places.get(0));
        return new ProblemInstance(mapa, origins, new LinkedList<Place>(mapa.places));
    }
}
