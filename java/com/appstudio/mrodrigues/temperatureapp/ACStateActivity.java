package com.appstudio.mrodrigues.temperatureapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ACStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acstate);
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
                    JSONObject jsonoutput = new JSONObject(response);
                    //((TextView) findViewById(R.id.textTemp)).setText(jsonoutput.getString(Utils.param_status));
                } catch(JSONException ex) {}

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
                Map<String,String> params = new HashMap<String, String>();
                params.put("level", level_str);
                params.put("degree",degree_str);
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
                            String date = "";
                            String temperature = "";
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                level = obj.getString("level");
                                date = obj.getString("date");
                                temperature = obj.getString("temperature");
                            }
                            ((TextView) findViewById(R.id.tempNr)).setText(temperature);
                            ((Spinner)findViewById(R.id.spinner)).setSelection(Integer.parseInt(level)-1);
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
}
