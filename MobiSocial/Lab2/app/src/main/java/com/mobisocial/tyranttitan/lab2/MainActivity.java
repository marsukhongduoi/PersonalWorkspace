package com.mobisocial.tyranttitan.lab2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager senManager;
    private float X, Y, Z;
    public static DecimalFormat DECIMAL_FORMATTER;
    double sensorValue;
    TextView textView, mode;
    ToggleButton toggleButton;
    boolean magnetic = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);
        senManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        textView= (TextView) findViewById(R.id.textView);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mode = (TextView) findViewById(R.id.textViewMode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (magnetic) {
            senManager.registerListener(this, senManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), senManager.SENSOR_DELAY_NORMAL);
        }
        else
        {
            senManager.registerListener(this, senManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), senManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        senManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        senManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType()== Sensor.TYPE_MAGNETIC_FIELD)
        {
            X = sensorEvent.values[0];
            Y = sensorEvent.values[1];
            Z = sensorEvent.values[2];
            sensorValue = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2)+ Math.pow(Z,2));
            textView.setText(DECIMAL_FORMATTER.format(sensorValue));
            if (sensorValue > 1000)
            {
                playSound(this);
            }
        }
        if (sensorEvent.sensor.getType()== Sensor.TYPE_ACCELEROMETER)
        {
            X = sensorEvent.values[0];
            Y = sensorEvent.values[1];
            Z = sensorEvent.values[2];
            sensorValue = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2)+ Math.pow(Z,2));
            textView.setText(DECIMAL_FORMATTER.format(sensorValue));
            if (sensorValue>11)
            {
                playSound(this);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void playSound(MainActivity view) {
        MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.beep);
        player.start();
    }

    public void onToggleClicked(View view) {
        senManager.unregisterListener(this);
        if (toggleButton.isChecked())
        {
            magnetic=true;
            mode.setText("Magnetometer Value (Threshold 1000)");
            senManager.registerListener(this, senManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), senManager.SENSOR_DELAY_NORMAL);

        }
        else
        {
            magnetic=false;
            mode.setText("Accelerometer Value (Threshold 11)");
            senManager.registerListener(this, senManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), senManager.SENSOR_DELAY_NORMAL);
        }
    }
}
