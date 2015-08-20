package my_utils;

import Map.Place;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class MapUtils {
    public static void merge(TreeMap<String, Place> dest, TreeMap<String, Place> news){
        dest.putAll(news);
    }

    public static TreeMap<String, Place> toMap(List<Place> lista){
        TreeMap<String, Place> tree = new TreeMap<String, Place>();
        for (Place e: lista){
            tree.put(e.place_id, e);
        }
        return tree;
    }

    public static List<Place> toList(TreeMap<String, Place> tree){
        return new LinkedList<Place>(tree.values());
    }
}
