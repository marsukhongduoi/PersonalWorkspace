package com.mobisocial.tyranttitan.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendNotification(View view) {
        Intent weatherIntent = new Intent(this, WeatherService.class);
        startService(weatherIntent);
        Button thisButton = (Button) findViewById(R.id.button);
        TextView thisText = (TextView) findViewById(R.id.textView);
        thisButton.setEnabled(false);
        thisText.setText("OpenWeatherMap API INTENT SERVICE activated!!!\n\n\n\nPlease wait for 5 seconds to get new Notification\n");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, WeatherService.class));
    }
}

