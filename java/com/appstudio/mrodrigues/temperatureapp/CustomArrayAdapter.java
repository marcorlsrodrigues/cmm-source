package com.appstudio.mrodrigues.temperatureapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mrodrigues on 23/03/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<Registo> {
    public CustomArrayAdapter(Context context, ArrayList<Registo> registo){
        super(context,0,registo);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Registo r = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha,parent,false);
        }
        ((TextView)convertView.findViewById(R.id.date)).setText(r.getDate());
        ((TextView)convertView.findViewById(R.id.degree)).setText(r.getDegree());
        ((TextView)convertView.findViewById(R.id.level)).setText(Integer.toString(r.getLevel()));
        ((TextView)convertView.findViewById(R.id.power)).setText(Integer.toString(r.getPower()));

        return convertView;
    }
}
