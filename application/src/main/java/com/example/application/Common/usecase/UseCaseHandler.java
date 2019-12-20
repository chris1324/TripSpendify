package com.example.application.Common.usecase;

import com.example.application.Common.executor.MainThread;
import com.example.application.Common.executor.WorkerThread;
import com.example.domain.Common.errorhanding.outcome.Outcome;

public class UseCaseHandler {

    // region Variables and Constructor ------------------------------------------------------------
    private final MainThread mMainThread;
    private final WorkerThread mWorkerThread;

    public UseCaseHandler(MainThread mainThread, WorkerThread workerThread) {
        mMainThread = mainThread;
        mWorkerThread = workerThread;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    public <Q, R> void execute(CommandUseCase<Q, R> useCase,
                               Q requestModel,
                               CommandUseCase.Listener<R> listener) {
        mWorkerThread.post(() -> useCase.execute(requestModel, listener));
    }

    // region CommandListenerWrapper  --------------------------------------------------------------
    private class CommandListenerWrapper<R> implements CommandUseCase.Listener<R> {
        private final CommandUseCase.Listener<R> mListener;

        public CommandListenerWrapper(CommandUseCase.Listener<R> listener) {
            mListener = listener;
        }

        @Override
        public void onComplete(Outcome<R> responseModel) {
            mWorkerThread.post(() -> mListener.onComplete(responseModel));
        }
    }
    // endregion CommandListenerWrapper  -----------------------------------------------------------
}
