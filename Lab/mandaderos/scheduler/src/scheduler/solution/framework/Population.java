package scheduler.solution.framework;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;
import my_utils.ArrayUtil;

public class Population extends LinkedList<Genotype> {
    private int mandaderos_count;
    private int places_count;
    private Random rng;
    
    public Population(int size, Random rng){
        this.rng = rng;
        mandaderos_count = 0;
        places_count = 0;
        for (int i=0;i<size;i++){
            super.add(new Genotype());
        }
    }
    
    public Population(List<Integer> elements, int size, Random rng){
        this.rng = rng;
        mandaderos_count = 0;
        places_count = 0;
        for (int i=0;i<size;i++){
            super.add(new Genotype(elements));
        }
    }

    public void addMandadero(){
        IntStream ints = this.rng.ints(this.size(), 0, this.size());
        if (mandaderos_count == 0){
            mandaderos_count++;
            return;
        }
        mandaderos_count++;
        for (Genotype individuo : this){
            OptionalInt sorteo = ints.findFirst();
            int index_sorteo = sorteo.getAsInt();
            individuo.addAtIndex(0, index_sorteo);
        }
    }

    public void removeMandadero(){
        IntStream ints = this.rng.ints(this.size(), 0, this.size());
        for (Genotype individuo : this){
            OptionalInt sorteo = ints.findFirst();
            int index_sorteo = sorteo.getAsInt();
            individuo.removeNthOf(0, index_sorteo);
        }
        mandaderos_count--;
    }

    public void addPlace(Integer p){
        IntStream ints = this.rng.ints(this.size(), 0, this.size());
        for (Genotype individuo : this){
            OptionalInt sorteo = ints.findFirst();
            int index_sorteo = sorteo.getAsInt();
            individuo.addAtIndex(p, index_sorteo);
        }
        places_count++;
    }

    public void removePlace(Integer p){
        IntStream ints = this.rng.ints(this.size(), 0, this.size());
        for (Genotype individuo : this){
            OptionalInt sorteo = ints.findFirst();
            int index_sorteo = sorteo.getAsInt();
            int index = individuo.indexOf(p);
            individuo.removeNthOf(p, index_sorteo);
        }
        places_count--;
    }
}
