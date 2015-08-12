package my_utils;

import java.util.List;

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
        String s = "";
        for(int i=0;i<arr.length;i++){
            s += arr[i] + ",";
        }
        return s;
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
}
