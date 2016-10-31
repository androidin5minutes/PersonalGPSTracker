package com.sow.personalgpstracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class GpsService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        GpsManager gpsManager = new GpsManager(getApplicationContext());
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setGpsManager(gpsManager);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
