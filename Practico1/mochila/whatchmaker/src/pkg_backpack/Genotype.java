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
}
