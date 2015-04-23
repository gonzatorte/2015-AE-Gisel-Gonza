package pkg_backpack;

import java.util.ArrayList;
import java.util.List;

//abstract class Fenotype implements List<Integer>{}
public class Fenotype extends ArrayList<Integer>{
    public Fenotype(Genotype g){
        super((ArrayList<Integer>) g);
    }
    public Fenotype(List<Integer> l){
        super(l);
    }
}
