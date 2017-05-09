package com.appstudio.mrodrigues.temperatureapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appstudio.mrodrigues.temperatureapp.db.Contrato;
import com.appstudio.mrodrigues.temperatureapp.db.DB;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HistoricalDataActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_data);
        DB mDbHelper = new DB(getApplicationContext());
        db =  mDbHelper.getWritableDatabase();
        try {
            fillListview();
        }catch (Exception e){
            Log.d("Historical Activity", e.getMessage());
        }
    }

    private void fillListview(){
        ArrayList<Registo> arrayItems = new ArrayList<>();
        arrayItems = getLocalRecords();

        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(this,arrayItems);

        ((ListView)findViewById(R.id.listHistorical)).setAdapter(itemsAdapter);
    }

    private ArrayList<Registo> getLocalRecords(){
        ArrayList<Registo> array = new ArrayList<Registo>();
        String query = "SELECT device_id,temperature,level,date,power FROM "+ Contrato.Registo.TABLE_NAME + " order by _id desc";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Registo r = new Registo(c.getString(3),c.getString(1),c.getInt(2),c.getInt(4),c.getInt(0));
                array.add(r);
            }while(c.moveToNext());
        }
        return array;
    }
}
