package com.mobisocial.tyranttitan.lab5;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private List<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void getTweet(View view) {
        String user = "Metallica";
        EditText TweetID = findViewById(R.id.editText);
        if (TweetID.getText().length()!=0)
        {
            user = TweetID.getText().toString();
        }
        ListView lv = (ListView) findViewById(R.id.listview);

        new getTweet(user,this).execute();
        Log.d("Titan Main",String.valueOf(list.size()));


    }
}




