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
