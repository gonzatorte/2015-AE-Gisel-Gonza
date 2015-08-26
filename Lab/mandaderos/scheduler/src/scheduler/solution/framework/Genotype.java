package scheduler.solution.framework;

import java.util.ArrayList;
import java.util.List;
import my_utils.ArrayUtil;

public class Genotype extends ArrayList<Integer> {

    Genotype(Genotype parent1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Genotype() {
        super();
    }
    
    Genotype(List<Integer> candidate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void normalize(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void inplaceNormalize(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    void removeNthOf(Integer i, int index_sorteo) {
        ArrayUtil.NthRemoveOf(this, i, index_sorteo);
    }

    void addAtIndex(Integer p, int index_sorteo) {
        this.add(p, index_sorteo);
    }
}
