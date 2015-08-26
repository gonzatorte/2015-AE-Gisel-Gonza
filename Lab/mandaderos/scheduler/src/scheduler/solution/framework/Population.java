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
        if (mandaderos_count == 0){
            mandaderos_count++;
            return;
        }
        mandaderos_count++;
        IntStream ints = this.rng.ints(this.size(), 0, this.get(0).size()+1);
        int[] sorteos = ints.toArray();
        int index_sorteo = 0;
        for (Genotype individuo : this){
            individuo.addAtIndex(0, sorteos[index_sorteo++]);
        }
    }

    public void removeMandadero(){
        IntStream ints = this.rng.ints(this.size(), 1, mandaderos_count);
        int[] sorteos = ints.toArray();
        int index_sorteo = 0;
        for (Genotype individuo : this){
            individuo.removeNthOf(0, sorteos[index_sorteo++]);
        }
        mandaderos_count--;
    }

    public void addPedido(Integer p){
        IntStream ints = this.rng.ints(this.size(), 0, this.get(0).size()+1);
        int[] sorteos = ints.toArray();
        int index_sorteo = 0;
        for (Genotype individuo : this){
            individuo.addAtIndex(p, sorteos[index_sorteo++]);
        }
        places_count++;
    }

    public void removePedido(Integer p){
        for (Genotype individuo : this){
            individuo.remove(p);
        }
        places_count--;
    }
}
