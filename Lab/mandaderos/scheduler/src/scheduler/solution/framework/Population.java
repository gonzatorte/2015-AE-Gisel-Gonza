package scheduler.solution.framework;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Population extends LinkedList<Genotype> {
    public Population(int size){
        for (int i=0;i<size;i++){
            super.add(new Genotype());
        }
    }
    
    public Population(List<Integer> elements, int size){
        for (int i=0;i<size;i++){
            super.add(new Genotype(elements));
        }
    }

    public void RandomizePopultion(Random rng){
        for (Genotype g : this){
            Collections.shuffle(g, rng);
        }
    }

    public void addMandadero(){
        this.addMandadero(1);
    }
    
    public void addMandadero(int quantity){
        for (Genotype individuo : this){
            for (int i = 0 ; i < quantity ; i++){
                individuo.add(0);
            }
        }
    }
    
    public void removeMandadero(){
        this.removeMandadero(1);
    }
    
    public void removeMandadero(int quantity){
        for (Genotype individuo : this){
            for (int i = 0 ; i < quantity ; i++){
                individuo.remove(individuo.indexOf(0));
            }
        }
    }

    public void addPlace(Integer place){
        List<Integer> wrap = new LinkedList<Integer>();
        wrap.add(place);
        this.addPlace(wrap);
    }
    
    public void addPlace(List<Integer> places){
        for (Genotype individuo : this){
            for (Integer p : places)
                individuo.add(p);
        }
    }

    public void removePlace(Integer place){
        List<Integer> wrap = new LinkedList<Integer>();
        wrap.add(place);
        this.removePlace(wrap);
    }
    
    public void removePlace(List<Integer> places){
        for (Genotype individuo : this){
            for (Integer p : places){
                int index = individuo.indexOf(p);
                if (index != -1){
                    individuo.remove(index);
                }
            }
        }
    }
}
