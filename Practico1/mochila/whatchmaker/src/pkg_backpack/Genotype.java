package pkg_backpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype extends ArrayList<Integer>{
    public Genotype padre1 = null;
    public Genotype padre2 = null;
    public Genotype(Fenotype f){
        super(Coder.encode(f));
    }
    public Genotype(List<Integer> l){
        super(l);
    }
    public Genotype(List<Integer> l, Genotype p1, Genotype p2){
        this(l);
//        this.padre1 = p1;
//        this.padre2 = p2;
    }
    public Genotype(int l){
        super(l);
    }
    public Genotype cross(Genotype g, int crossoverIndex){
        Genotype newg = new Genotype(this, this, g);
        for (int j = 0; j < crossoverIndex; j++)
        {
            newg.set(j, g.get(j));
        }
        return newg;
    }
    public void correct(Random rng){
        int i = this.size();
        while (!Coder.decode(this).factible()){
            i--;
            if (this.get(i) == 1){
                this.set(i, 0);
            }
        }
    }
    @Override
    public String toString(){
        String S = Coder.ArrayToString(this.toArray());
        int id = System.identityHashCode(this);
        return "[" + id + "]" + S;
    }
}
