package com.example.android.demomaps;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {
    String TAG = "MapsActivity";
    ArrayList<String> allname = new ArrayList<String>();
    ArrayList<String> alllat = new ArrayList<String>();
    ArrayList<String> alllong = new ArrayList<String>();
    int len;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent in = getIntent();
        len = in.getIntExtra("length", 0);
        allname = in.getStringArrayListExtra("name_list");
        alllat = in.getStringArrayListExtra("lat_list");
        alllong = in.getStringArrayListExtra("long_list");
        Log.d(TAG, "-----------" + len);
        Log.d(TAG, "-----------" + allname);
        Log.d(TAG, "-----------" + alllat);
        Log.d(TAG, "-----------" + alllong);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     *
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (mMap != null) {
                onLocationChanged(location);
            }
            // Check if we were successful in obtaining the map.
        }
    }

    public void onLocationChanged(Location location) {
        double latitude = Double.parseDouble(alllat.get(0));
        double longitude = Double.parseDouble(alllong.get(0));
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        for(int i=0;i<len;i++){
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(alllat.get(i)), Double.parseDouble(alllong.get(i)))).title(allname.get(i)));
    }
}
}

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

