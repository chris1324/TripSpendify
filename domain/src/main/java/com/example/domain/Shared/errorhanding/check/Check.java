package com.example.domain.Shared.errorhanding.check;

import java.util.List;
import java.util.Map;

public class Check {

    public static boolean isDefault(long l){
        return l == 0L;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmptyString(String s){
        return s.length() == 0;
    }

    public static boolean isEmptyList(List list) {
        return list.size() == 0;
    }

    public static boolean isEmptyMap(Map map){
        return map.size() == 0;
    }
}
