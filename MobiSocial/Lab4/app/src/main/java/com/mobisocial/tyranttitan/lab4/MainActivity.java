package com.mobisocial.tyranttitan.lab4;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static android.app.PendingIntent.getActivity;
import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences lastLocation = PreferenceManager.getDefaultSharedPreferences( this);
        TextView txtViewCurrent = (TextView) findViewById(R.id.textViewCurrent);
        if (lastLocation.contains("lastLocation")) {
            txtViewCurrent.setText("Saved Location:\n" + lastLocation.getString("lastLocation",""));
            Button startMonitoringButton = (Button) findViewById(R.id.buttonStartService);
            startMonitoringButton.setEnabled(true);
        }
        else {
            txtViewCurrent.setText("No saved location");
        }
        TextView serviceTextView = (TextView) findViewById(R.id.textViewServiceStatus);
        if (checkServiceRunning())
        {
            serviceTextView.setText("Service is running.");
        }
    }
    public boolean checkServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.mobisocial.tyranttitan.lab4.LocationService"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
    public void getCurrentLocation(View view) {
        TextView txtView = (TextView) findViewById(R.id.textView);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }
        try {

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_LOW);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            LocationListener myLocationListener= new LocationListener()  {
                @Override
                public void onLocationChanged(Location location) {
                    SharedPreferences currentLocation = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    currentLocation.edit().putString("currentLocation", getLocation(location)).apply();
                    currentLocation.edit().putLong("currentLatitude", Double.doubleToRawLongBits(location.getLatitude())).apply();
                    currentLocation.edit().putLong("currentLongitude", Double.doubleToRawLongBits(location.getLongitude())).apply();
                    Log.d("TITAN","HERE: " + currentLocation.getString("currentLocation",""));
                    Log.d("TITAN", "Long/Lat " + Double.toString(location.getLongitude()) + "-" + Double.longBitsToDouble(currentLocation.getLong("currentLongitude",0)));

                    TextView txtView = (TextView) findViewById(R.id.textView);
                    String msg = "";
                    if (currentLocation.contains("lastLocation"))
                    {
                        Location lastLocation = new Location("");
                        lastLocation.setLongitude(Double.longBitsToDouble(currentLocation.getLong("lastLongitude",0)));
                        lastLocation.setLatitude(Double.longBitsToDouble(currentLocation.getLong("lastLatitude",0)));
                        msg = Float.toString(lastLocation.distanceTo(location));
                    }
                    if (getLocation(location) =="")
                    {
                        txtView.setText("Current Latitude/Longitude:\n" + String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude())+ "\nDistance from last checkpoint: " + msg + " m");

                    }
                    else {
                        txtView.setText("Current Location:\n" + getLocation(location) + "\nDistance from last checkpoint: " + msg + " m");
                    }
                    Button getLocationButton = (Button) findViewById(R.id.button);
                    getLocationButton.setEnabled(true);
                    getLocationButton.setText("Get\ncurrent location");
                    Button saveLocationButton = (Button) findViewById(R.id.buttonSaveLocation);
                    saveLocationButton.setEnabled(true);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            boolean gps_enabled= false, network_enabled = false;
            try{
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            catch(Exception ex){}
            try{
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
            catch(Exception ex){}
            if (!gps_enabled && !network_enabled)
            {
                Toast.makeText(this, "Please turn on Location in Setting", Toast.LENGTH_SHORT).show();
            }
            else {
                if (gps_enabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, myLocationListener);
                }
                if (network_enabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, myLocationListener);
                }
                Button getLocationButton = (Button) findViewById(R.id.button);
                getLocationButton.setText("Please wait...");
                getLocationButton.setEnabled(false);
                Button saveLocationButton = (Button) findViewById(R.id.buttonSaveLocation);
                saveLocationButton.setEnabled(false);
            }


        } catch (AssertionError e)
        {
        }

    }


    private String getLocation(Location location) {
        String strAdd = "";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");

            for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
            }
            strAdd = strReturnedAddress.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public void saveCurrentLocation(View view) {
        SharedPreferences currentLocation = PreferenceManager.getDefaultSharedPreferences( this);
        if (currentLocation.contains("currentLocation"))
        {
            currentLocation.edit().putString("lastLocation", currentLocation.getString("currentLocation","")).apply();
            currentLocation.edit().putLong("lastLatitude", currentLocation.getLong("currentLatitude",0)).apply();
            currentLocation.edit().putLong("lastLongitude", currentLocation.getLong("currentLongitude",0)).apply();
        }
        TextView txtViewCurrent = (TextView) findViewById(R.id.textViewCurrent);
        txtViewCurrent.setText("Updated Location:\n" + currentLocation.getString("lastLocation",""));
        Button startMonitoringButton = (Button) findViewById(R.id.buttonStartService);
        if (!startMonitoringButton.isEnabled())
        {
            startMonitoringButton.setEnabled(true);
        }
    }

    public void StartMonitor(View view) {
        Intent i= new Intent(this, LocationService.class);
        this.startService(i);
        Button startMonitorButton = (Button) findViewById(R.id.buttonStartService);
        startMonitorButton.setEnabled(false);
        startMonitorButton.setText("Service started.");
        TextView hintText = (TextView) findViewById(R.id.textViewHint);
        hintText.setText("This is a background service, you can destroy the UI\nRun away until the notification disappear!!!\n");

    }
}
