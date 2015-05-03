package pkg_backpack;

import java.util.ArrayList;

public class Fenotype extends ArrayList<Integer>{
    public Genotype genotipo = null;
    public Fenotype(int g){
        super(g);
    }
    public boolean factible(){
        return this.PesoTotal() <= Backpack.w;
    }
    public Integer PesoTotal(){
        int peso = 0;
        for (int i = 0; i < Backpack.pesos.size(); i++) {
            if (this.get(i) == 1) {
                peso += Backpack.pesos.get(i);
            }
        }
        return peso;
    }
    public Integer GananciaTotal(){
        int peso = 0;
        for (int i = 0; i < Backpack.ganancias.size(); i++) {
            if (this.get(i) == 1) {
                peso += Backpack.ganancias.get(i);
            }
        }
        return peso;
    }
    @Override
    public String toString(){
        String S = Coder.ArrayToString(this.toArray());
        int id = System.identityHashCode(this);
        return "[" + id + "]" + S;
    }
}
