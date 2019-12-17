package com.example.domain.Common.errorhanding.exception;

public class ImpossibleState extends RuntimeException {



    public ImpossibleState() {
    }

    public ImpossibleState(String s) {
        super(s);
    }
}
