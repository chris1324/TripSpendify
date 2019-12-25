package com.example.application.Shared.baseUseCase.queryUseCase;

import com.example.application.Shared.executor.MainThread;
import com.example.application.Shared.executor.WorkerThread;
import com.example.application.Shared.baseUseCase.useCase.UseCase;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class QueryUseCaseHandler {
    // region Variables and Constructor ------------------------------------------------------------
    private final MainThread mMainThread;
    private final WorkerThread mWorkerThread;

    public QueryUseCaseHandler(MainThread mainThread, WorkerThread workerThread) {
        mMainThread = mainThread;
        mWorkerThread = workerThread;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    public <Q extends UseCase.RequestDTO,
            R extends UseCase.ResponseDTO> void execute(QueryUseCase<Q, R> useCase,
                                                        Q requestModel,
                                                        DisposableObserver<R> observer) {
        Observable<R> observable = useCase
                .execute(requestModel)
                .subscribeOn(Schedulers.from(mWorkerThread))
                .observeOn(Schedulers.from(mMainThread));
        useCase.startSubscription(observable,observer);
    }
}
