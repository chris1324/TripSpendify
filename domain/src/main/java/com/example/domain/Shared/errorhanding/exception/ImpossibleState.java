package com.example.domain.Shared.errorhanding.exception;

public class ImpossibleState extends RuntimeException {



    public ImpossibleState() {
    }

    public ImpossibleState(String s) {
        super(s);
    }
}
