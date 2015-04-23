package pkg_backpack;

import java.util.ArrayList;
import java.util.List;


//abstract class Genotype implements List<Integer>{}
public class Genotype extends ArrayList<Integer>{
    public Genotype(Fenotype f){
        super((ArrayList<Integer>) f);
    }
    public Genotype(List<Integer> l){
        super(l);
    }
    public Genotype cross(Genotype g, int crossoverIndex){
        Genotype newg = new Genotype(g);
        for (int j = 0; j < crossoverIndex; j++)
        {
            newg.set(j, g.get(j));
        }
        return new Genotype(g);
    }
}
