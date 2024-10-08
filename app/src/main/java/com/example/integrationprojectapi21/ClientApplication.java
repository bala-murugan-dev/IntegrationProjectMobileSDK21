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


        /**
         * (STEP 2)   SDK will be loaded and execute its business logic in separate thread
         * parameters1 - SDK needs App context to perform some operations
         * parameter2 - md5 digest certificate of the app
         *
         * to get this md5 digest , run this command:
         *  $apksigner verify --print-certs -v <apk-name>.apk
         *
         *  if there are more than one signers, mention the first signers md5 digest in this parameter
        */
        //Use Crc.java file with release apk file's path
        Excal.createInstance(this,"9bd449776aa583a8ce5dbe65f55caa15",R.string.dexCRC);
        this.registerActivityLifecycleCallbacks(Excal.getInstance().preventRecording());

    }

}
