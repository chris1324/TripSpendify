package com.example.domain.Shared.errorhanding.guard;


import com.example.domain.Shared.errorhanding.exception.NullArgumentException;

public class Guard {

    public static void NotNull(Object object) throws NullArgumentException {
        if (object == null) throw new NullArgumentException();
    }



}
