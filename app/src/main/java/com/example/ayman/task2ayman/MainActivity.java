package com.example.ayman.task2ayman;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    Button data, loc;
    String lat, lon;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = (Button) findViewById(R.id.btn1id);
        loc = (Button) findViewById(R.id.btn2id);
        data.setOnClickListener(this);
        loc.setOnClickListener(this);
        lat = "a";
        lon = "a";
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                        //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        Log.e("onCreate", "onCreate");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
        Log.e("onResume", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

        Log.e("onPause", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart", "onStart");
    }

    /**
     * If connected get lat and long
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());

            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v == data) {
            Intent gotodata = new Intent(this, dataActivity.class);
            startActivity(gotodata);
        } else if (v == loc) {
            if (lat == "a" && lon == "a") {
                Toast.makeText(this, "Can't get location, try open GPS", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+lat+","+lon+"?q="+lat+","+lon+"(U r here)"));
                if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d("MAP->", "Couldn't call " + lat + ", no receiving apps installed!");
                }
            }
        }
    }
}
