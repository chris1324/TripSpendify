package com.example.domain.Common.errorhanding.check;

import java.util.List;

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

    public static boolean isEmptyString(List list) {
        return list.size() == 0;
    }
}
