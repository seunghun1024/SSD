package com.example.ssd;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class Mainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("야너두");

    }
}
