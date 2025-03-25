package com.jeesite.modules.algorithm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CloneUtil {

    public static <T> Map<String, List<T>> cloneListMap(Map<String, List<T>> listMap, Function<T,Object> function){
        Map<String, List<T>> cloneMap = new HashMap<>();
        for (Map.Entry<String, List<T>> stringListEntry : listMap.entrySet()) {
            String key = stringListEntry.getKey();
            List<T> list = stringListEntry.getValue();
            cloneMap.put(key,list.stream().map(t->(T)function.apply(t)).collect(Collectors.toList()));
        }
        return cloneMap;
    }

    public static <K, V> Map<K, V> cloneMap(Map<K, V> listMap, Function<V,Object> function){
        Map<K, V> cloneMap = new HashMap<>();
        for (Map.Entry<K, V> kvEntry : listMap.entrySet()) {
            V value = kvEntry.getValue();
            if (value==null){
                cloneMap.put(kvEntry.getKey(), null);
            }else{
                cloneMap.put(kvEntry.getKey(), (V)function.apply(value));
            }
        }
        return cloneMap;
    }


    public static <T> List<T> cloneList(List<T> list, Function<T,Object> function){
        return list.stream().map(t -> (T) function.apply(t)).collect(Collectors.toList());
    }

}
