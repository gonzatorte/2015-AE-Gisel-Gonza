package pkg_backpack;

//abstract class Fenotype implements List<Integer>{}

import java.util.ArrayList;
import java.util.List;

public class Fenotype extends ArrayList<Integer>{
    public Fenotype(Genotype g){
        super((ArrayList<Integer>) g);
    }
    public Fenotype(List<Integer> l){
        super(l);
    }
}
