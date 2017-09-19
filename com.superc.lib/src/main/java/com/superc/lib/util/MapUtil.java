package com.superc.lib.util;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by owner on 2017/8/23.
 */

public class MapUtil {

    public static <T> Map<Integer, Integer> getValueMap(@NonNull Map<Integer, Set<Integer>> map) {
        Map<Integer, Integer> newMap = new Hashtable<>();
        for (Map.Entry<Integer, Set<Integer>> e : map.entrySet()) {
            if (e.getValue() != null) {
                for (Integer i : e.getValue()) {
                    newMap.put(e.getKey(), i);
                }
            }
        }
        return newMap;
    }

    public static <K, V> List<K> getKey(@NonNull Map<K, V> map) {
        List<K> result = new ArrayList<>();
        for (Map.Entry<K, V> e : map.entrySet()) {
            result.add(e.getKey());
        }
        return result;
    }

    public static <K, V> int getValueSize(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return 0;
        }
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (e != null && e.getValue() != null) {
                if (e.getValue() instanceof Collection) {
                    return ((Collection) e.getValue()).size();
                }
                if (e.getValue() instanceof Map) {
                    return ((Map) e).size();
                }
            }
        }
        return 0;
    }

    public static <K, V> List<Integer> getValueSizeList(Map<K, V> map) {
        List<Integer> returnList = new LinkedList<>();
        if (map == null || map.isEmpty()) {
            returnList.add(0);
            return returnList;
        }
        for (Map.Entry<K, V> e : map.entrySet()) {
            int size = 0;
            if (e != null && e.getValue() != null) {
                if (e.getValue() instanceof Collection) {
                    size = ((Collection) e.getValue()).size();
                }
                if (e.getValue() instanceof Map) {
                    size = ((Map) e.getValue()).size();
                }
            }
            returnList.add(size);
        }
        return returnList;
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        if (map == null || map.isEmpty()) return true;
        boolean tag = true;
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (e != null && e.getValue() != null) {
                if (e.getValue() instanceof Collection) {
                    if (!((Collection) e.getValue()).isEmpty()) {
                        tag = false;
                        break;
                    }
                } else if (e.getValue() instanceof List) {
                    if (!((List) e.getValue()).isEmpty()) {
                        tag = false;
                        break;
                    }
                } else if (e.getValue() instanceof Map) {
                    if (!((Map) e.getValue()).isEmpty()) {
                        tag = false;
                        break;
                    }
                } else {
                    if (!StringUtils.isEmpty(e.getValue().toString())) {
                        tag = false;
                        break;
                    }
                }
            }
        }
        return tag;
    }

    public static <K, V> JSONObject mapToJsonObject(@NonNull Map<K, V> map) {
        JSONObject o = new JSONObject(map);
        return o;
    }
}
