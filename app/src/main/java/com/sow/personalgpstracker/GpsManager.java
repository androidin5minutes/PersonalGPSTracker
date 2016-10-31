package com.sow.personalgpstracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GpsManager implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Fuse";

    private static int LOCATION_INTERVAL =  2000;
    private static int FASTEST_LOCATION_INTERVAL = 5000;
    private final MyApplication myApplication;

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Firebase mRefRoot;

    public GpsManager(Context context) {
        this.context = context;

        myApplication = (MyApplication) context.getApplicationContext();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
        mGoogleApiClient.connect();

    }

    protected void createLocationRequest() {
        Log.i(TAG, "GpsManager: createLocationRequest()");
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(LOCATION_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        } catch (Exception e) {
            Log.e(TAG, "GpsManager: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "GpsManager: onLocationChanged: Lat " + location.getLatitude() + ", Lon: " + location.getLongitude() + ", Accuracy: " + location.getAccuracy());

        try {
            Firebase user = mRefRoot.child(myApplication.getUser().getUid());
            user.setValue(location.getLatitude()+"#"+location.getLongitude()+"#"+location.getAccuracy());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GpsManager: onConnected");
        mRefRoot = new Firebase("https://personal-gps-tracker.firebaseio.com/");
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "GpsManager: startLocationUpdates()");
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "GpsManager: Permission failed, will return.");
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "GpsManager: Location update is started.");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
