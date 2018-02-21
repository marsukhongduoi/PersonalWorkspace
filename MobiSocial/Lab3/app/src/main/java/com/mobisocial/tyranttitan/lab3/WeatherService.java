package com.mobisocial.tyranttitan.lab3;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mobisocial.tyranttitan.lab3.model.Location;
import com.mobisocial.tyranttitan.lab3.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WeatherService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    private static String API_URL = "69ad5032d6e12ff504ca0a3aa6a8fed0";
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.mobisocial.tyranttitan.lab3.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.mobisocial.tyranttitan.lab3.extra.PARAM2";

    public WeatherService() {
        super("WeatherService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String getLocation = "Oulu";
            while (true)
            {
            String wData = getWeatherData(getLocation);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( this);
            if (preferences.contains("weatherData")) {
                preferences.edit().putString("previousData", preferences.getString("weatherData","")).apply();
                preferences.edit().putString("weatherData", wData).apply();
            }
            else
            {
                preferences.edit().putString("weatherData", wData).apply();
            }
            try {
                Weather completeWeather = getWeather(preferences.getString("weatherData","")); //get the value of var1);
                Weather lastWeather = getWeather(preferences.getString("previousData","")); //get the value of var1);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // build notification
                // the addAction re-use the same intent to keep the example short
                String message = Math.round((completeWeather.temperature.getTemp() - 273.15)) +
                        "\u2103\nWind speed: " + Float.toString(completeWeather.wind.getSpeed()) + "m/s \n" +
                        "Previous temp: " + Math.round((lastWeather.temperature.getTemp() - 273.15)) +"\u2103";
                Notification notification = new Notification.Builder(this)
                        .setStyle(new Notification.BigTextStyle().bigText(message))
                        .setSmallIcon(getResources().getIdentifier("weather_" + completeWeather.currentCondition.getIcon(), "drawable", getPackageName()))
                        .setContentTitle(getLocation + " weather now:")
                        .setContentIntent(pIntent)
                        .setAutoCancel(true) //clear automatically when clicked
                        .build();
                if (mNotificationManager != null) {
                    mNotificationManager.notify(1, notification);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    public String getWeatherData(String location) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(BASE_URL + location + "&appid=" + API_URL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }
        return null;
    }

    public byte[] getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(IMG_URL + code + ".png")).openConnection();
            Log.wtf("AAA", IMG_URL+code+".png");
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();
        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        Location loc = new Location();

        JSONObject coordObj = jObj.getJSONObject("coord");
        loc.setLatitude(BigDecimal.valueOf(coordObj.getDouble("lat")).floatValue());
        loc.setLongitude(BigDecimal.valueOf(coordObj.getDouble("lon")).floatValue());

        JSONObject sysObj = jObj.getJSONObject("sys");
        loc.setCountry(sysObj.getString("country"));
        loc.setSunrise(sysObj.getInt("sunrise"));
        loc.setSunset(sysObj.getInt("sunset"));
        loc.setCity(jObj.getString("name"));
        weather.location = loc;

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(JSONWeather.getInt("id"));
        weather.currentCondition.setDescr(JSONWeather.getString("description"));
        weather.currentCondition.setCondition(JSONWeather.getString("main" ));
        weather.currentCondition.setIcon(JSONWeather.getString("icon"));

        JSONObject mainObj = jObj.getJSONObject("main");
        weather.currentCondition.setHumidity(mainObj.getInt("humidity"));
        weather.currentCondition.setPressure(mainObj.getInt("pressure"));
        weather.temperature.setMaxTemp(BigDecimal.valueOf(mainObj.getDouble("temp_max")).floatValue());
        weather.temperature.setMinTemp(BigDecimal.valueOf(mainObj.getDouble("temp_min")).floatValue());
        weather.temperature.setTemp(BigDecimal.valueOf(mainObj.getDouble("temp")).floatValue());

        // Wind
        JSONObject wObj = jObj.getJSONObject("wind");
        weather.wind.setSpeed(BigDecimal.valueOf(wObj.getDouble("speed")).floatValue());
        weather.wind.setDeg(BigDecimal.valueOf(wObj.getDouble("deg")).floatValue());

        // Clouds
        JSONObject cObj = jObj.getJSONObject("clouds");
        weather.clouds.setPerc(cObj.getInt("all"));

        // We download the icon to show


        return weather;
    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */

}

