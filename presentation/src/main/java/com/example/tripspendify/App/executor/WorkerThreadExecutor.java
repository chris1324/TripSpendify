package com.example.tripspendify.App.executor;

import com.example.application.Shared.executor.WorkerThread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerThreadExecutor implements WorkerThread {

    private static final int CORE_THREADS = 3;
    private static final long KEEP_ALIVE_SECONDS = 60L;

    private final ThreadPoolExecutor mThreadPoolExecutor;

    public WorkerThreadExecutor() {
        mThreadPoolExecutor = newThreadPoolExecutor();
    }

    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }


    protected ThreadPoolExecutor newThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                CORE_THREADS,
                Integer.MAX_VALUE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );
    }
}
