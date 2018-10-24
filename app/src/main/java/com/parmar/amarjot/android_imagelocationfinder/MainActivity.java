package com.parmar.amarjot.android_imagelocationfinder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity{

    // Used to display recipes stored in local db.
    ListView list;

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    String [] landmarkNames;
    Integer [] imageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isServicesOK();
        setupActionbar();
        setupList();
    }

    private void setupList() {
        // load data from resources into arrays
        loadArrays();

        // Make list using array

        //goToMaps(0);
        list= findViewById(R.id.listView);
        CustomListview customListview = new CustomListview(this, landmarkNames, imageID);
        list.setAdapter(customListview);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMaps(i);
            }
        });
    }

    private void loadArrays() {
        // Read data from string array
        // Load into array
        landmarkNames = getResources().getStringArray(R.array.name);

    }


    public void goToMaps(int landmarkIndex){
        Log.d(TAG, "goToMaps: called");
        Intent intent = new Intent(this, MapActivity.class);

        Bundle landmarkDetails = new Bundle();
        landmarkDetails.putInt("landmarkIndex", landmarkIndex);
        intent.putExtras(landmarkDetails);
        startActivity(intent);
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right);
    }

    // Makes sure app can communicate with GoogleMaps
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // Sets up custom action bar with filter spinner (all, vegan, vege)
    private void setupActionbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

    }
}
