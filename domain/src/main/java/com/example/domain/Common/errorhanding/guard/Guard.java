package com.example.domain.Common.errorhanding.guard;


import java.util.List;

public class Guard {

    public static void NotNull(Object object) throws NullArgumentException {
        if (object == null) throw new NullArgumentException();
    }



}
