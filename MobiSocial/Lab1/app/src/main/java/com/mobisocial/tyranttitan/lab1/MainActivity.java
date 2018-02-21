package com.mobisocial.tyranttitan.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openAccount(View view) {
        Intent intent = new Intent(this, Account.class);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        if (username.getText().toString().equals("username") && password.getText().toString().equals("password"))
        {
            startActivity(intent);
        }
    }
}
