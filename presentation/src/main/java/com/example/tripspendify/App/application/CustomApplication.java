package com.example.tripspendify.App.application;

import android.app.Application;

import com.example.tripspendify.App.di.CompositionRoot;

public class CustomApplication extends Application {
    private CompositionRoot mCompositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositionRoot = new CompositionRoot(this);
    }

    public CompositionRoot getCompositionRoot() {
        return mCompositionRoot;
    }
}