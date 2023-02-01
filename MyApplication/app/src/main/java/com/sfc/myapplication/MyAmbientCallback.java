package com.sfc.myapplication;

import android.os.Bundle;

import androidx.wear.ambient.AmbientModeSupport;

public class MyAmbientCallback extends AmbientModeSupport.AmbientCallback {
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        // Handle entering ambient mode
    }

    @Override
    public void onExitAmbient() {
        // Handle exiting ambient mode
    }

    @Override
    public void onUpdateAmbient() {
        // Update the content
    }
}