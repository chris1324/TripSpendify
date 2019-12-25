package com.example.application.Shared.baseUseCase.commandUseCase;

import com.example.application.Shared.executor.MainThread;
import com.example.application.Shared.executor.WorkerThread;
import com.example.application.Shared.baseUseCase.useCase.UseCase;
import com.example.domain.Shared.errorhanding.outcome.Outcome;

public class CommandUseCaseHandler {

    // region Variables and Constructor ------------------------------------------------------------
    private final MainThread mMainThread;
    private final WorkerThread mWorkerThread;

    public CommandUseCaseHandler(MainThread mainThread, WorkerThread workerThread) {
        mMainThread = mainThread;
        mWorkerThread = workerThread;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    public <Q extends UseCase.RequestDTO,
            R extends UseCase.ResponseDTO> void execute(CommandUseCase<Q, R> useCase,
                               Q requestModel,
                               CommandUseCase.Listener<R> listener) {
        mWorkerThread.execute(() -> useCase.execute(
                requestModel,
                new CommandListenerWrapper<>(listener)));
    }


    // region CommandListenerWrapper  --------------------------------------------------------------
    private class CommandListenerWrapper<R> implements CommandUseCase.Listener<R> {
        private final CommandUseCase.Listener<R> mListener;

        private CommandListenerWrapper(CommandUseCase.Listener<R> listener) {
            mListener = listener;
        }

        @Override
        public void onComplete(Outcome<R> responseModel) {
            mMainThread.execute(() -> mListener.onComplete(responseModel));
        }
    }
    // endregion CommandListenerWrapper  -----------------------------------------------------------
}
