package com.sfc.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sfc.myapplication.databinding.ActivityFlagMapsBinding;

import java.util.List;

public class FlagMaps extends FragmentActivity implements OnMapReadyCallback {
    Marker mCurrLocationMarker;
    Location mLastLocation;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityFlagMapsBinding binding = ActivityFlagMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
//        Log.d("missmap", "地图权限1");
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
//        if (mFusedLocationClient != null) {
//            Log.d("missmap","地图权限2");
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//        }
    }
//    LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            List<Location> locationList = locationResult.getLocations();
//            if (locationList.size() > 0) {
//                //The last location in the list is the newest
//                Location location = locationList.get(locationList.size() - 1);
//                Log.d("missmap","地图权限3");
//                Log.d("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
//                mLastLocation = location;
//                if (mCurrLocationMarker != null) {
//                    mCurrLocationMarker.remove();
//                }
//
//                //Place current location marker
//                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title("Current Position");
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
//
//                //move map camera
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
//            }
//        }
//    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private static final LatLng Mubuliding = new LatLng(35.38816829902916, 139.42723399013175);
    private static final LatLng Tennis = new LatLng(35.38696386572878, 139.42914485995647);
    private static final LatLng Kamoike = new LatLng(35.38699229459216, 139.42786035980842);
    private static final LatLng Delta = new LatLng(35.38824956525928, 139.42541544170638);
    private static final LatLng yukichi = new LatLng(35.38848308563938, 139.42698890503902);


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap = googleMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000); // two minute interval
//        mLocationRequest.setFastestInterval(120000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                //Location Permission already granted
//                Log.d("missmap","地图权限4");
//                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//                mGoogleMap.setMyLocationEnabled(true);
//            } else {
//                //Request Location Permission
//                Log.d("missmap","地图权限5");
//                //checkLocationPermission();
//            }
//        }
//        else {
//            Log.d("missmap","else");
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            mGoogleMap.setMyLocationEnabled(true);
//        }
//        // Add a marker in SFC and move the camera,
        // Define a BitmapDescriptor object for the marker's icon
        BitmapDescriptor duckicon = BitmapDescriptorFactory.fromResource(R.drawable.duck);
        BitmapDescriptor monitoricon = BitmapDescriptorFactory.fromResource(R.drawable.monitor);
        BitmapDescriptor statueicon = BitmapDescriptorFactory.fromResource(R.drawable.yukichi);
        BitmapDescriptor tennis_ballicon = BitmapDescriptorFactory.fromResource(R.drawable.tennis);
        BitmapDescriptor libraryicon = BitmapDescriptorFactory.fromResource(R.drawable.books);
        // Add the marker to the map
        googleMap.addMarker(new MarkerOptions().icon(duckicon).position(Kamoike).title("Kamoike"));
        googleMap.addMarker(new MarkerOptions().icon(libraryicon).position(Mubuliding).title("Mubuliding"));
        googleMap.addMarker(new MarkerOptions().icon(tennis_ballicon).position(Tennis).title("Tennis"));
        googleMap.addMarker(new MarkerOptions().icon(monitoricon).position(Delta).title("Delta"));
        googleMap.addMarker(new MarkerOptions().icon(statueicon).position(yukichi).title("yukichi"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(SFC));

        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(
                Mubuliding, 16);
        googleMap.moveCamera(cUpdate);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            return;
        }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }
    };
}