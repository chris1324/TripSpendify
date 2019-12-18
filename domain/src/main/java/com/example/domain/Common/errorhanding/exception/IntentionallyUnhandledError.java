package com.example.domain.Common.errorhanding.exception;

public class IntentionallyUnhandledError extends RuntimeException{

    public static IntentionallyUnhandledError sure(String s){
        return new IntentionallyUnhandledError("This Error is due to the following reason bu is unhanded intentionally: " + s);
    }

    public static IntentionallyUnhandledError maybe(String s){
        return new IntentionallyUnhandledError("This Error might be due to the following reason but is unhanded intentionally: " + s);
    }

    private IntentionallyUnhandledError(String s) {
        super(s);
    }
}
