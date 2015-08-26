package my_utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.rmi.CORBA.Util;
import org.jcp.xml.dsig.internal.dom.Utils;

public class ArrayUtil {
    public static boolean compareArrays(Object[] arr1, Object[] arr2){
        assert(arr1.length == arr2.length);
        for(int i=0;i<arr1.length;i++){
            if (arr1[i] != arr2[i]){
                return false;
            }
        }
        return true;
    }
    public static String ArrayToString(Object[] arr){
        return Arrays.deepToString(arr);
    }
    public static String ListToString(List lista){
        return Arrays.deepToString(lista.toArray());
    }
    public static int MaxInd(List<Integer> lista){
        int val = lista.get(0);
        int ind = 0;
        for (int i=0; i<lista.size(); i++){
            if (lista.get(i) > val){
                val = lista.get(i);
                ind = i;
            }
        }
        lista.set(ind, 0);
        return ind;
    }
    public static int NthIndexOf(List<Integer> lista, Integer objecto, int nth){
        for (int i=0 ; i < lista.size() ; i++){
            if (lista.get(i) == objecto){
                nth--;
                if (nth == 0){
                    return i;
                }
            }
        }
        return -1;
    }
    public static Integer NthRemoveOf(List<Integer> lista, Integer objecto, int nth){
        for (int i=0 ; i < lista.size() ; i++){
            if (lista.get(i) == objecto){
                nth--;
                if (nth == 0){
                    return lista.remove(i);
                }
            }
        }
        return null;
    }
    public static void NthAddOf(LinkedList<Integer> lista, Integer objecto, int nth){
        for (int i=0 ; i < lista.size() ; i++){
            if (lista.get(i) == objecto){
                nth--;
                if (nth == 0){
                    lista.add(i, objecto);
                }
            }
        }
    }
//    public static void addAtIndex(LinkedList<Integer> lista, Integer objecto, int index){
//        lista.add(index, objecto);
//    }
//    public static void addAtIndex(ArrayList<Integer> lista, Integer objecto, int index){
//        lista.add(index, objecto);
//    }
//    public static Integer NthAddOf(ArrayList<Integer> lista, Integer objecto, int nth){
//        for (int i=0 ; i < lista.size() ; i++){
//            if (lista.get(i) == objecto){
//                nth--;
//                if (nth == 0){
//                }
//            }
//        }
//    }
}
