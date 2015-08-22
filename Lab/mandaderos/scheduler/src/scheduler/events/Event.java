package scheduler.events;

public class Event {
    public int time;
    public String tipo;
    public String data;
    public Event(String tipo){
        this(tipo, null);
    }
    public Event(String tipo, String data){
        this.tipo = tipo;
        this.data = data;
    }
}
