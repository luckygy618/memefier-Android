package com.guoyucao.memefier;

import android.app.Application;

public class MyApp extends Application {

    String[] resolution_option = {"15","30", "60"};

    public String[] getResolution_option() {
        return resolution_option;
    }

    public void setResolution_option(String[] resolution_option) {
        this.resolution_option = resolution_option;
    }
}
