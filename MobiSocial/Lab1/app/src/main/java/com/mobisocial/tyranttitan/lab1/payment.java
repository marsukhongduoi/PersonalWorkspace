package com.mobisocial.tyranttitan.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }
    public void sendPayment (View view) {
        finish();

    }
}
