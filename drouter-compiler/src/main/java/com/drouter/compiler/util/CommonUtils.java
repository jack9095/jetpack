package com.drouter.compiler.util;

import java.util.Map;

public class CommonUtils {

    public static boolean isNotEmpty(Map<String, String> options) {
        return options != null && !options.isEmpty();
    }
}
