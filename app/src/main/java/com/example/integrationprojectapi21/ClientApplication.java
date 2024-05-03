package com.example.integrationprojectapi21;

import android.app.Application;
import android.util.Log;

import com.example.mysdklib.Excal;

public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Excal.getInstance(this,"9a2bd97760ccd4ff2bc6719ed9fe338b");

    }

}
