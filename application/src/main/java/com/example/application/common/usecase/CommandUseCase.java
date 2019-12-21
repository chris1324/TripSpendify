package com.example.application.common.usecase;

import com.example.domain.Common.errorhanding.outcome.Outcome;

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
