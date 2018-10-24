package com.parmar.amarjot.android_imagelocationfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import static com.parmar.amarjot.android_imagelocationfinder.R.array.name;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 573;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String TAG = "MapActivity: ";
    boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationClient;
    GoogleMap mMap;

    String landmarkName;
    Double lat;
    Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setupLandmarkDetails();
        initMap();
    }

    private void setupLandmarkDetails(){
        Log.d(TAG, "setupLandmarkDetails: called");
        int landmarkIndex = getIntent().getExtras().getInt("landmarkIndex");

        String names [] = getResources().getStringArray(R.array.name);
        String[] lats  = getResources().getStringArray(R.array.lat);
        String[] lngs  = getResources().getStringArray(R.array.lng);

        landmarkName = names[landmarkIndex];
        lat = Double.parseDouble(lats[landmarkIndex]);
        lng = Double.parseDouble(lngs[landmarkIndex]);

        Log.d(TAG, "setupLandmarkDetails: lat: "+ lat);
    }

    private void initMap() {
        Log.d(TAG, "initMap: called");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "initMap: asking for permissioin");
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, MY_LOCATION_REQUEST_CODE);
            return;
        }
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        Log.d(TAG, "initMap: getLastLocation onSuccess");
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
//                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in NZ"));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//                            Log.d(TAG, "initMap: getLastLocation lat: " + location.getLatitude() + ", long: " + location.getLongitude());
//                        }
//                    }
//                });
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: called");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: permissioin granted");
                mLocationPermissionGranted = true;
            } else {
                Log.d(TAG, "getLocationPermission: asking for permissioin");
                ActivityCompat.requestPermissions(this, permissions, MY_LOCATION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, MY_LOCATION_REQUEST_CODE);
            Log.d(TAG, "getLocationPermission: asking for permissioin");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: called");
        mMap = googleMap;

        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Log.d(TAG, "onMapReady: lat: " + lat);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            return;
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true;
                            initMap();

                            return;
                        }
                        Log.d(TAG, "onRequestPermissionsResult: Location permission granted");
                    }
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        MapActivity.this.overridePendingTransition(R.anim.slide_out_left,
                R.anim.slide_in_left);
    }
}
