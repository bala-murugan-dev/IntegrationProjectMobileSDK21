package com.example.integrationprojectapi21;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.mysdklib.Excal;

/**
 * Application Class of this IntegrationProjectApi21
 * <br>
 * application class will be registered under application tag in manifest file in the android:name property
 * <br>
 * eg:  < application android:name = .ClientApplication >
 */
public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        //(STEP 2)  Excal SDK will be loaded and execute its business logic in separate thread
        Excal.getInstance(this,"9a2bd97760ccd4ff2bc6719ed9fe338b");

    }

}
