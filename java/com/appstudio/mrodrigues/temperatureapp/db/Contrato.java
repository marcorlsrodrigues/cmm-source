package com.appstudio.mrodrigues.temperatureapp.db;

import android.provider.BaseColumns;

/**
 * Created by mrodrigues on 29/03/2017.
 */

public class Contrato {
    private static String TEXT_TYPE = "TEXT";
    private static String INT_TYPE = "INTEGER";

    public Contrato(){}

    public static abstract class Registo implements BaseColumns {
        public static final String TABLE_NAME = "cmm";
        public static final String COLUMN_DEVICEID = "device_id";
        public static final String COLUMN_TEMPERATURE = "temperature";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_DATE = "date";

        public static final String[] PROJECTION = {Registo._ID,Registo.COLUMN_DEVICEID,Registo.COLUMN_TEMPERATURE,Registo.COLUMN_LEVEL,Registo.COLUMN_DATE};

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Registo.TABLE_NAME + "(" + Registo._ID + " " + INT_TYPE + " PRIMARY KEY, " +
                        Registo.COLUMN_DEVICEID+ " "  + INT_TYPE + ","+
                        Registo.COLUMN_TEMPERATURE+ " "  + TEXT_TYPE + ","+
                        Registo.COLUMN_LEVEL+ " "   + INT_TYPE + ","+
                        Registo.COLUMN_DATE + " "    + TEXT_TYPE + ");";

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Registo.TABLE_NAME + ";";
    }
}
