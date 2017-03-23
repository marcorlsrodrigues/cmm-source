package com.appstudio.mrodrigues.temperatureapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
}
