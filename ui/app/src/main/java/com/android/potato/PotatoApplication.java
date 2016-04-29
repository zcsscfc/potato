package com.android.potato;

import android.app.Application;

public class PotatoApplication extends Application {

    private static PotatoApplication instance;

    public static PotatoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
    }
}
