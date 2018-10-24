package com.parmar.amarjot.android_imagelocationfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;
import android.widget.TextView;
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


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 573;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String TAG = "MapActivity: ";
    boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationClient;
    GoogleMap mMap;
    String landmarkName;
    Double landmarkLat, landmarkLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mLocationPermissionGranted = false;

        getLocationPermission();
        setupLandmarkDetails();
        setupUserDistance();
        initMap();
        setupActionbar();
    }

    private void setupUserDistance() {

        if (mLocationPermissionGranted) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "getUserLocaiton: getLastLocation onSuccess");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null & mLocationPermissionGranted) {
                                // Logic to handle location object

                                Location loc1 = new Location("");
                                loc1.setLatitude(landmarkLat);
                                loc1.setLongitude(landmarkLng);

                                Location loc2 = new Location("");
                                loc2.setLatitude(location.getLatitude());
                                loc2.setLongitude(location.getLongitude());

                                float distanceInMeters = loc1.distanceTo(loc2);
                                TextView msgBox = findViewById(R.id.textView);
                                msgBox.setText((int) (distanceInMeters / 1000) + "KMs");

                                TextView currentLocation = findViewById(R.id.textViewcurrentLocation);
                                currentLocation.setText( loc2.getLatitude() + ", " + loc2.getLongitude());

                                Log.d(TAG, "getUserLocaiton: getLastLocation lat: " + location.getLatitude() + ", long: " + location.getLongitude());
                            }
                            else {
                                Log.d(TAG, "getUserLocaiton: getLastLocation failed to get location");


                                Location loc1 = new Location("");
                                loc1.setLatitude(landmarkLat);
                                loc1.setLongitude(landmarkLng);

                                Location loc2 = new Location("");
                                loc2.setLatitude(R.string.defaultLat);
                                loc2.setLongitude(R.string.defaultLong);

                                float distanceInMeters = loc1.distanceTo(loc2);
                                TextView msgBox = findViewById(R.id.textView);
                                msgBox.setText((int) (distanceInMeters / 1000) + "KMs");

                                TextView currentLocation = findViewById(R.id.textViewcurrentLocation);
                                currentLocation.setText( loc2.getLatitude() + ", " + loc2.getLongitude());
                            }
                        }
                    });
        }
        else
        {
            Location loc1 = new Location("");
            loc1.setLatitude(landmarkLat);
            loc1.setLongitude(landmarkLng);

            Location loc2 = new Location("");
            loc2.setLatitude(R.string.defaultLat);
            loc2.setLongitude(R.string.defaultLong);

            float distanceInMeters = loc1.distanceTo(loc2);
            TextView msgBox = findViewById(R.id.textView);
            msgBox.setText((int) (distanceInMeters/ 1000) + "KMs");
        }

    }

    private void setupLandmarkDetails(){
        Log.d(TAG, "setupLandmarkDetails: called");
        int landmarkIndex = getIntent().getExtras().getInt("landmarkIndex");

        String names [] = getResources().getStringArray(R.array.name);
        String[] lats  = getResources().getStringArray(R.array.lat);
        String[] lngs  = getResources().getStringArray(R.array.lng);

        landmarkName = names[landmarkIndex];
        landmarkLat = Double.parseDouble(lats[landmarkIndex]);
        landmarkLng = Double.parseDouble(lngs[landmarkIndex]);

        TextView textViewLandmark = findViewById(R.id.textViewLandmarkLocation);
        textViewLandmark.setText(landmarkLat + ", " + landmarkLng );
    }

    private void initMap() {
        Log.d(TAG, "initMap: called");

        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: called");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        mLocationPermissionGranted = false;
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

        LatLng sydney = new LatLng(landmarkLat, landmarkLng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, ": called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true;
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

    // Sets up custom action bar with back button & landmark title
    private void setupActionbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(landmarkName);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24px);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
