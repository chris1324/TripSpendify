package com.example.domain.Common.errorhanding.guard;


import com.example.domain.Common.errorhanding.exception.NullArgumentException;

public class Guard {

    public static void NotNull(Object object) throws NullArgumentException {
        if (object == null) throw new NullArgumentException();
    }



}
