package com.example.application.Shared.baseUseCase.useCase;

import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCase;
import com.example.application.Shared.baseUseCase.commandUseCase.CommandUseCaseHandler;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCase;
import com.example.application.Shared.baseUseCase.queryUseCase.QueryUseCaseHandler;

import io.reactivex.observers.DisposableObserver;

public class UseCaseHandler {

    // region Variables and Constructor ------------------------------------------------------------
    private final CommandUseCaseHandler mCommandUseCaseHandler;
    private final QueryUseCaseHandler mQueryUseCaseHandler;

    public UseCaseHandler(CommandUseCaseHandler commandUseCaseHandler,
                          QueryUseCaseHandler queryUseCaseHandler) {
        mCommandUseCaseHandler = commandUseCaseHandler;
        mQueryUseCaseHandler = queryUseCaseHandler;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    public <Q extends UseCase.RequestDTO,
            R extends UseCase.ResponseDTO> void execute(CommandUseCase<Q, R> useCase,
                                                        Q requestModel,
                                                        CommandUseCase.Listener<R> listener) {
        mCommandUseCaseHandler.execute(useCase, requestModel, listener);
    }

    public <Q extends UseCase.RequestDTO,
            R extends UseCase.ResponseDTO> void execute(QueryUseCase<Q, R> useCase,
                                                        Q requestModel,
                                                        DisposableObserver<R> observer) {
        mQueryUseCaseHandler.execute(useCase, requestModel, observer);
    }
}
