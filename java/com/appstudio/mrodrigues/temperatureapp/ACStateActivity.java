package com.appstudio.mrodrigues.temperatureapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appstudio.mrodrigues.temperatureapp.db.AndroidDatabaseManager;
import com.appstudio.mrodrigues.temperatureapp.db.Contrato;
import com.appstudio.mrodrigues.temperatureapp.db.DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import static com.appstudio.mrodrigues.temperatureapp.R.styleable.CompoundButton;

public class ACStateActivity extends AppCompatActivity {


    // Gets the data repository in write mode
    SQLiteDatabase db;
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acstate);
        DB mDbHelper = new DB(getApplicationContext());
        db =  mDbHelper.getWritableDatabase();
        sw = (Switch)findViewById(R.id.switch1);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                slideSwitch();
            }
        });
    }

    // -- display do AC
    public void displayAC(double newvalue) {
        TextView ac = (TextView) findViewById(R.id.tempNr);
        String acText = ac.getText().toString();
        double currentValue = Double.parseDouble(acText);
        currentValue = currentValue + newvalue;
        ac.setText(String.valueOf(currentValue));
    }

    // -- adição de 0.5 graus
    public void adicionaMeioGrau(View v) {
        displayAC(0.5);
    }

    // -- subtração de 0.5 graus
    public void subtraiMeioGrau(View v) {
        displayAC(-0.5);
    }

    public void save(View v){
        String url = "https://marcorodrigues191.000webhostapp.com/ws_insert.php";
        JSONObject jsonBody = new JSONObject();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Log.d("temperatureApp", obj.getString(Utils.param_status));

                    if(obj.getString(Utils.param_status).equals("OK")){
                        saveLocalDb();
                    }else{
                        getLastLocalDb();
                    }
                } catch(Exception ex) {
                    Log.d("tag", ex.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
                String level_str = mySpinner.getSelectedItem().toString();

                TextView myText = (TextView)findViewById(R.id.tempNr);
                String degree_str = myText.getText().toString();

                Switch s = (Switch) findViewById(R.id.switch1);
                String power_str  = "";
                if(s.isChecked()){
                    power_str = "1";
                }else{
                    power_str = "0";
                }

                Map<String,String> params = new HashMap<String, String>();
                params.put("level", level_str);
                params.put("degree",degree_str);
                params.put("power",power_str);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getlast(View v){
        String url = "https://marcorodrigues191.000webhostapp.com/ws_get.php";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray(Utils.param_dados);
                            String level = "";
                            String power = "";
                            String temperature = "";
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                level = obj.getString("level");
                                temperature = obj.getString("temperature");
                                power = obj.getString("power");
                            }
                            ((TextView) findViewById(R.id.tempNr)).setText(temperature);
                            ((Spinner)findViewById(R.id.spinner)).setSelection(Integer.parseInt(level)-1);
                            sw.setOnCheckedChangeListener(null);
                            if(power.equals("1")){
                                ((Switch)findViewById(R.id.switch1)).setChecked(true);
                            }else{
                                ((Switch)findViewById(R.id.switch1)).setChecked(false);
                            }
                            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    slideSwitch();
                                }
                            });
                        } catch(JSONException ex){}
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.textTemp)).setText(error.getMessage());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void savelocal(View v){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
        String level_str = mySpinner.getSelectedItem().toString();
        TextView myText = (TextView)findViewById(R.id.tempNr);
        String degree_str = myText.getText().toString();
        Switch s = (Switch) findViewById(R.id.switch1);
        String power_str  = "";
        if(s.isChecked()){
            power_str = "1";
        }else{
            power_str = "0";
        }

        ContentValues cv = new ContentValues();
        cv.put(Contrato.Registo.COLUMN_DEVICEID,1);
        cv.put(Contrato.Registo.COLUMN_DATE,strDate);
        cv.put(Contrato.Registo.COLUMN_LEVEL,level_str);
        cv.put(Contrato.Registo.COLUMN_TEMPERATURE,degree_str);
        cv.put(Contrato.Registo.COLUMN_POWER,power_str);

        db.insert(Contrato.Registo.TABLE_NAME,null,cv);

        //inicia AndroidDatabaseManager
        Intent dbmanager = new Intent(this,AndroidDatabaseManager.class);
        startActivity(dbmanager);
    }

    public void saveLocalDb(){
        try{
            Log.d("tag", "saveLocalDb");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(c.getTime());
            Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
            String level_str = mySpinner.getSelectedItem().toString();
            TextView myText = (TextView)findViewById(R.id.tempNr);
            String degree_str = myText.getText().toString();
            Switch s = (Switch) findViewById(R.id.switch1);
            String power_str  = "";
            if(s.isChecked()){
                power_str = "1";
            }else{
                power_str = "0";
            }

            ContentValues cv = new ContentValues();
            cv.put(Contrato.Registo.COLUMN_DEVICEID,1);
            cv.put(Contrato.Registo.COLUMN_DATE,strDate);
            cv.put(Contrato.Registo.COLUMN_LEVEL,level_str);
            cv.put(Contrato.Registo.COLUMN_TEMPERATURE,degree_str);
            cv.put(Contrato.Registo.COLUMN_POWER,power_str);

            db.insert(Contrato.Registo.TABLE_NAME,null,cv);
            Log.d("tag", "db insert");
            //inicia AndroidDatabaseManager
            Intent dbmanager = new Intent(this,AndroidDatabaseManager.class);
            startActivity(dbmanager);
            Log.d("tag", "start activity");
        }catch (Exception ex){
            Log.d("tag", ex.toString());
        }
    }

    public void getLastLocalDb(){
        String url = "https://marcorodrigues191.000webhostapp.com/ws_get.php";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray(Utils.param_dados);
                            String level = "";
                            String power = "";
                            String temperature = "";
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                level = obj.getString("level");
                                power = obj.getString("power");
                                temperature = obj.getString("temperature");
                            }
                            ((TextView) findViewById(R.id.tempNr)).setText(temperature);
                            ((Spinner)findViewById(R.id.spinner)).setSelection(Integer.parseInt(level)-1);
                            if(power.equals("1")){
                                ((Switch)findViewById(R.id.switch1)).setChecked(true);
                            }else{
                                ((Switch)findViewById(R.id.switch1)).setChecked(false);
                            }
                        } catch(JSONException ex){}
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.textTemp)).setText(error.getMessage());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void slideSwitch(){
        String url = "https://marcorodrigues191.000webhostapp.com/ws_insert.php";
        JSONObject jsonBody = new JSONObject();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Log.d("temperatureApp", obj.getString(Utils.param_status));

                    if(obj.getString(Utils.param_status).equals("OK")){
                        Log.d("tag", "if equals");
                        saveLocalDb();
                    }else{
                        getLastLocalDb();
                    }
                } catch(Exception ex) {
                    Log.d("tag", ex.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
                String level_str = mySpinner.getSelectedItem().toString();

                TextView myText = (TextView)findViewById(R.id.tempNr);
                String degree_str = myText.getText().toString();

                Switch s = (Switch) findViewById(R.id.switch1);
                String power_str  = "";
                if(s.isChecked()){
                    power_str = "1";
                }else{
                    power_str = "0";
                }

                Map<String,String> params = new HashMap<String, String>();
                params.put("level", level_str);
                params.put("degree",degree_str);
                params.put("power",power_str);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
