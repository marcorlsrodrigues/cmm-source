package com.appstudio.mrodrigues.temperatureapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnAC(View v){
        Intent i = new Intent(MainActivity.this,ACStateActivity.class);
        startActivity(i);
    }

    public void btnHist(View v){
        Intent i = new Intent(MainActivity.this,HistoricalDataActivity.class);
        startActivity(i);
    }
}
