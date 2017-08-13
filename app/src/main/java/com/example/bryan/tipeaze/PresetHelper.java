package com.example.bryan.tipeaze;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PresetHelper extends SQLiteOpenHelper {


    public PresetHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, PresetContract.DATABASE_NAME, factory, PresetContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PresetContract.TABLE_PRESET_INFO.CREATE_TABLE);
        db.execSQL(PresetContract.TABLE_PRESET_NAMES.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //check if we should upgrade based on oldVersion/newVersion
        onCreate(db);
    }
}
