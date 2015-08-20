package scheduler.problem;

import Map.Mapa;
import scheduler.events.Event;

public class OfflineProblemInstance extends ProblemInstance {
    public int step = 0;

    public OfflineProblemInstance(Mapa mapa){
        super(mapa);
    }

    @Override
    public void applyEvent(Event event) {
        if ("addMandadero".equals(event.tipo)){
            count_mandaderos++;
        } else if ("removeMandadero".equals(event.tipo)){
            count_mandaderos--;
        } else {
            throw new Error("No hay este tipo de evento");
        }
    }
}
