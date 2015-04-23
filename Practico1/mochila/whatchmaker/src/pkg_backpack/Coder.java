package pkg_backpack;

import java.util.ArrayList;
import java.util.List;

public class Coder {
    public static Fenotype decode(Genotype genotipo)
    {
        return new Fenotype(genotipo);
    }

    public static Genotype encode(Fenotype fenotipo)
    {
        return new Genotype(fenotipo);
    }
}

//abstract class Fenotype implements List<Integer>{}
class Fenotype extends ArrayList<Integer>{
    public Fenotype(Genotype g){
        super((ArrayList<Integer>) g);
    }
    public Fenotype(List<Integer> l){
        super(l);
    }
}

//abstract class Genotype implements List<Integer>{}
class Genotype extends ArrayList<Integer>{
    public Genotype(Fenotype f){
        super((ArrayList<Integer>) f);
    }
    public Genotype(List<Integer> l){
        super(l);
    }
}
