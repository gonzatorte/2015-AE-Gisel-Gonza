package scheduler.problem;

import Map.Place;
import java.util.LinkedList;

public class MandaderoTaskQueue extends LinkedList<Place> implements Comparable<MandaderoTaskQueue>{
    public int compareTo(MandaderoTaskQueue q2){
        int comp1 = new Integer(this.size()).compareTo(q2.size());
        if (comp1 == 0){
            for (Place e1 : this){
                for (Place e2 : q2){
                    Integer id1 = System.identityHashCode(e1);
                    int comp2 = id1.compareTo(System.identityHashCode(e2));
                    if (comp2 != 0){
                        return comp2;
                    }
                }
            }
            return 0;
        } else {
            return comp1;
        }
    }
}
