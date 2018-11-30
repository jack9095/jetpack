package com.drouter.api.utils;

import com.drouter.api.interceptor.ActionInterceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description:
 */
public class MapUtils {

    public static List<ActionInterceptor> getInterceptorClasses(Map<Integer, ActionInterceptor> map) {
        List<ActionInterceptor> list = new ArrayList<>();

        for (Object key : map.keySet()) {
            list.add(map.get(key));
        }

        return list;
    }
}
