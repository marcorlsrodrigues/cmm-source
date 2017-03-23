package com.appstudio.mrodrigues.temperatureapp;

import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoricalDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data);
        fillListview();
    }

    private void fillListview(){
        //ArrayList<String> arrayItems = new ArrayList<>();

        /*arrayItems.add("2017-03-23 10:00:00");
        arrayItems.add("2017-03-22 10:00:00");
        arrayItems.add("2017-03-21 10:00:00");
        arrayItems.add("2017-03-20 10:00:00");
        arrayItems.add("2017-03-19 10:00:00");
        arrayItems.add("2017-03-18 10:00:00");
        arrayItems.add("2017-03-17 10:00:00");*/

        ArrayList<Registo> arrayItems = new ArrayList<>();
        arrayItems.add(new Registo("2017-03-23 10:00:00","26.5",3));
        arrayItems.add(new Registo("2017-03-22 10:00:00","27.5",2));
        arrayItems.add(new Registo("2017-03-21 10:00:00","23.0",1));
        arrayItems.add(new Registo("2017-03-20 10:00:00","24.5",3));
        arrayItems.add(new Registo("2017-03-19 10:00:00","21.0",2));

        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(this,arrayItems);

        ((ListView)findViewById(R.id.listHistorical)).setAdapter(itemsAdapter);
    }
}
