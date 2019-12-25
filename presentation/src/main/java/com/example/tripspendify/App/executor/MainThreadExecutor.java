package com.example.tripspendify.App.executor;

import android.os.Looper;

import com.example.application.Shared.executor.MainThread;

import android.os.Handler;



public class MainThreadExecutor implements MainThread {

    private final Handler mUiHandler;

    public MainThreadExecutor() {
        mUiHandler = getMainHandler();
    }

    @Override
    public void execute(Runnable runnable) {
        mUiHandler.post(runnable);
    }

    protected Handler getMainHandler() {
        return new Handler(Looper.getMainLooper());
    }
}
