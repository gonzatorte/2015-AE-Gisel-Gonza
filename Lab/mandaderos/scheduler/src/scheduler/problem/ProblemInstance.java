package scheduler.problem;

import Map.Mapa;
import Map.Place;
import java.util.LinkedList;
import scheduler.events.Event;

public abstract class ProblemInstance {
    public Mapa mapa;
    public LinkedList<Place> origin_mandaderos;
    
    public ProblemInstance(Mapa mapa, LinkedList<Place> origin_mandaderos){
        this.mapa = mapa;
        this.origin_mandaderos = origin_mandaderos;
    }

    public ProblemInstance(Mapa mapa){
        this.mapa = mapa;
        this.origin_mandaderos = new LinkedList<Place>();
    }
    
    public abstract void applyEvent(Event event);
}
