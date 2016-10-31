package com.sow.personalgpstracker;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by renato on 30/10/2016.
 */
public class MyApplication extends Application {

    private GoogleApiClient mGoogleApiClient;
    private GpsManager gpsManager;
    FirebaseUser user;
    private String TAG = "Fuse";

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        Log.d(TAG, "MyApplication: set to Application Class: user");
        this.user = user;
    }

    public GpsManager getGpsManager() {
        return gpsManager;
    }

    public void setGpsManager(GpsManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        Log.d(TAG, "MyApplication: set to Application Class: mGoogleApiClient");
        this.mGoogleApiClient = mGoogleApiClient;
    }
}
