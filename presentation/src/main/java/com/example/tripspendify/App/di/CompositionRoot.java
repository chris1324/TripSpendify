package com.example.tripspendify.App.di;

import android.app.Application;

public class CompositionRoot {

    private final Application mApplication;

    public CompositionRoot(Application application) {
        mApplication = application;
    }
}
