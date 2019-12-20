package com.example.application.Common.usecase;

import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Trip.category.Category;

import java.util.List;

public abstract class CommandUseCase<Q, R> {

    public interface Listener<R> {
        void onComplete(Outcome<R> responseModel);
    }

    // ---------------------------------------------------------------------------------------------
    void execute(Q requestModel, Listener<R> listener) {
        Outcome<R> outcome = execute(requestModel);
        listener.onComplete(outcome);
    }

    abstract protected Outcome<R> execute(Q requestModel);



}
