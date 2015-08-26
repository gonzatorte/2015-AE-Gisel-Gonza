package scheduler.events;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Event {
    public int time;
    public String tipo;
    public Object[] data;
    public Event(String tipo){
        this(tipo, null);
    }
    public Event(String tipo, Object... datas){
        this.tipo = tipo;
        this.data = datas;
    }
}
