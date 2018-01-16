package com.project.sabaree.vehicletracking;

import android.app.Application;

import com.firebase.client.Firebase;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
