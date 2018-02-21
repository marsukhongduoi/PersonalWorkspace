package com.mobisocial.tyranttitan.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }
    public void newPayment(View view) {
        Intent intent = new Intent(this, payment.class);
        startActivity(intent);
    }
}
