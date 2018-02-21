package com.mobisocial.tyranttitan.lab4;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends Service implements LocationListener{
    public static final int PERMISSION_CODE = 99;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private final LocationListener locationListener = this;

    private static final String TAG = "HelloService";

    private boolean isRunning  = false;
    private Double longitude, latitude;
    @Override
    public void onCreate() {
        super.onCreate();
        Context ctx = getApplication();
        SharedPreferences currentLocation = PreferenceManager.getDefaultSharedPreferences(ctx);
        longitude = Double.longBitsToDouble(currentLocation.getLong("lastLongitude",0));
        latitude = Double.longBitsToDouble(currentLocation.getLong("lastLatitude",0));
        Log.d("TITAN","Long/Lat " + Double.toString(longitude) +" " + Double.toString(latitude));

        isRunning = true;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");


                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                try {

                    Log.d("TITAN","HERE in Thread SERVICE");


                    if (ContextCompat.checkSelfPermission(
                            getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("TITAN","HERE in Thread before call location");
                        try{
                            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                        }
                        catch(Exception ex){}
                        try{
                            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                        }
                        catch(Exception ex){}
                        if (gps_enabled) {
                            Log.d("TITAN","gps_enabled " + String.valueOf(gps_enabled));

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
                        }
                        if (network_enabled) {
                            Log.d("TITAN","network_enabled " + String.valueOf(network_enabled));

                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
                        }
                    }
                } catch (AssertionError e)
                {
                }

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
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

    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences currentLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Location lastLoc = new Location("");
        lastLoc.setLatitude(latitude);
        lastLoc.setLongitude(longitude);
        float distance = lastLoc.distanceTo(location);
        //Double distance = Math.sqrt(Math.pow(longitude-location.getLongitude(),2)+Math.pow(latitude-location.getLatitude(),2));
        try {
            Intent i = new Intent();
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            // build notification
            // the addAction re-use the same intent to keep the example short
            String message =  String.valueOf(distance);
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Distance: " + message )
                    .setContentText("Your location\n" + getLocation(location))
                    .setContentIntent(pIntent)
                    .setOngoing(true)
                    .build();
            if (mNotificationManager != null) {
                if (distance<15) {
                    Log.d("TITAN","Try to create new notification with " + String.valueOf(distance));

                    mNotificationManager.notify(1, notification);
                }
                else {
                    mNotificationManager.cancel(1);
                }
            }

        } catch (Exception e) {
        }
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
}