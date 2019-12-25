package com.example.application.Shared.baseUseCase.queryUseCase;


import com.example.application.Shared.baseUseCase.useCase.UseCase;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class QueryUseCase<Q extends UseCase.RequestDTO, R extends UseCase.ResponseDTO> extends UseCase<Q, R>  {

    private Disposable mDisposable;

    abstract protected Observable<R> execute(Q requestModel);

    public void dispose() {
        if (!(mDisposable.isDisposed())) mDisposable.dispose();
    }

    void startSubscription(Observable<R> observable, DisposableObserver<R> observer){
     mDisposable = observable.subscribeWith(observer);
    }
}
