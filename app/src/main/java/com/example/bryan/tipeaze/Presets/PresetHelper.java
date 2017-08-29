package com.example.bryan.tipeaze.Presets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * TODO make this entity into a singleton
 */

public class PresetHelper extends SQLiteOpenHelper {


    private static PresetHelper instance;

    public static PresetHelper getInstance(Context c){

        if(instance == null)
            instance = new PresetHelper(c.getApplicationContext(), PresetContract.DATABASE_NAME, null, PresetContract.VERSION);

    return instance;
    }


    private PresetHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PresetContract.TABLE_PRESET_INFO.CREATE_TABLE);
        db.execSQL(PresetContract.TABLE_PRESET_NAMES.CREATE_TABLE);


        if(shouldInitDefaultData(db))
            initDefaultData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO check if we should upgrade based on oldVersion/newVersion
        db.execSQL("DROP TABLE IF EXISTS " + PresetContract.TABLE_PRESET_INFO.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PresetContract.TABLE_PRESET_NAMES.TABLE_NAME);
        onCreate(db);
    }


    private boolean shouldInitDefaultData(SQLiteDatabase database){

        final String[] projection = {PresetContract.TABLE_PRESET_NAMES._ID};
        final String[] args = {Presets.DEFAULT_NAME};

        final Cursor cursor = database.query(PresetContract.TABLE_PRESET_NAMES.TABLE_NAME,
                                             projection,
                                             PresetContract.TABLE_PRESET_NAMES.COL_NAME + " =? ",
                                             args,
                                             null, null, null);


        if(cursor.getCount() > 0){
            cursor.close();

            return false;
        }


        return true;
    }


    private void initDefaultData(SQLiteDatabase db){

        final Presets defaultPresets = new Presets.Builder().apply();

        final ContentValues nameValues = new ContentValues();
        nameValues.put(PresetContract.TABLE_PRESET_NAMES.COL_NAME,
                       Presets.DEFAULT_NAME);

        final String insertNameCol = valuesToString(nameValues, PresetContract.TABLE_PRESET_NAMES.TABLE_NAME);

        nameValues.remove(PresetContract.TABLE_PRESET_NAMES.COL_NAME);


        db.execSQL(insertNameCol);


        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_PRESET_NAME, Presets.DEFAULT_NAME);

        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_CURR_TIP , defaultPresets.getCurrTip());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_CURR_TAX, defaultPresets.getCurrTax());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_CURR_SPLIT, defaultPresets.getCurrSplit());

        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_MAX_TIP, defaultPresets.getMaxTip());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_MAX_TAX, defaultPresets.getMaxTip());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_MAX_SPLIT, defaultPresets.getMaxSplit());

        final String insertNameData = valuesToString(nameValues, PresetContract.TABLE_PRESET_INFO.TABLE_NAME);



        db.execSQL(insertNameData);

    }


    private String valuesToString(ContentValues values, String table){

        //Converts column-data mappings into sqlite executable statement

        final StringBuilder $sql = new StringBuilder();
        $sql.append(" INSERT INTO ")
              .append(table)
              .append(" ( ");

        Object[] bindArgs;

        final int colCount = values.size();

        if(colCount > 0) {

            bindArgs = new Object[colCount];
            int i = 0;
            for (String col : values.keySet()) {
                $sql.append((i > 0) ? "," : "")
                        .append(col);
                bindArgs[i] = values.get(col);
                i++;
            }
            $sql.append(" ) ")
                    .append(" VALUES ( ");

            for (i = 0; i < colCount; i++) {
                $sql.append((i > 0) ? "," : "");
                $sql.append(bindArgs[i]);
            }

            $sql.append(" ); ");

        } else
            throw new RuntimeException("Why in the hell is values* empty?");


    return $sql.toString();
    }



}
