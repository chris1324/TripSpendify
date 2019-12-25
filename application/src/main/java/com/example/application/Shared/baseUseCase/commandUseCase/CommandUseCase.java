package com.example.application.Shared.baseUseCase.commandUseCase;

import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Shared.errorhanding.outcome.Outcome;

public abstract class CommandUseCase<Q extends UseCase.RequestDTO, R extends UseCase.ResponseDTO> extends UseCase<Q, R> {

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
