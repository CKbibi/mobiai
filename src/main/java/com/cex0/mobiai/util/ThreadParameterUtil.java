package com.cex0.mobiai.util;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;

public class ThreadParameterUtil {
    private static TransmittableThreadLocal<Map<String, String>> threadLocal = new TransmittableThreadLocal();

    private ThreadParameterUtil() {

    }

    public static void set(Map<String, String> parameterMap) {
        threadLocal.set(parameterMap);
    }

    public static void set(String key, String value) {
        Map<String, String> parameterMap = (Map) threadLocal.get();
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        else {
            ((Map) parameterMap).remove(key);
        }

        ((Map)parameterMap).put(key, value);
        threadLocal.remove();
        threadLocal.set(parameterMap);
    }

    public static String get(String key) {
        Map<String, String> parameterMap = (Map)threadLocal.get();
        return parameterMap == null ? "" : parameterMap.get(key);
    }

    public static void refreshParameter(String key, String value) {
        Map<String, String> parameterMap = (Map) threadLocal.get();
        if (parameterMap != null) {
            parameterMap.remove(key);
            parameterMap.put(key, value);
            threadLocal.set(parameterMap);
        }
    }

    public static void clear() {
        threadLocal.remove();
    }
}
