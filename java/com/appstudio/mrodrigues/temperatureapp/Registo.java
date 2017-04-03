package com.appstudio.mrodrigues.temperatureapp;

import java.util.Date;

/**
 * Created by mrodrigues on 23/03/2017.
 */

public class Registo {
    public String date;
    public String degree;
    public int level;
    public int power;

    public Registo(String date,String degree, int level,int power){
        this.date = date;
        this.degree = degree;
        this.level = level;
        this.power = power;
    }

    public String getDate(){
        return date;
    }

    public String getDegree() {
        return degree;
    }

    public int getLevel() {
        return level;
    }

    public int getPower() {
        return power;
    }
}
