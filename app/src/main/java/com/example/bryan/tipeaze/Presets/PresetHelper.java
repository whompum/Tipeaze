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


        if(!checkIfHasDefaults(db))
            initDefaultData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //check if we should upgrade based on oldVersion/newVersion
        onCreate(db);
    }


    private boolean checkIfHasDefaults(SQLiteDatabase database){

        final String[] projection = {PresetContract.TABLE_PRESET_NAMES._ID};
        final String[] args = {PresetContract.TABLE_PRESET_NAMES.DEFAULT_PRESET_NAME};

        final Cursor cursor = database.query(PresetContract.TABLE_PRESET_NAMES.TABLE_NAME,
                                             projection,
                                             PresetContract.TABLE_PRESET_NAMES.COL_NAME + " =? ",
                                             args,
                                             null, null, null);


        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        }

        return false;
    }


    private void initDefaultData(SQLiteDatabase db){

        final Presets presets = new Presets();

        final ContentValues nameValues = new ContentValues();
        nameValues.put(PresetContract.TABLE_PRESET_NAMES.COL_NAME,
                       Presets.DEFAULT_NAME);

        final String insertNameCol = insertWithValues(nameValues, PresetContract.TABLE_PRESET_NAMES.TABLE_NAME);

        nameValues.remove(PresetContract.TABLE_PRESET_NAMES.COL_NAME);

        Log.i("test", " INSERTING NAME: " + insertNameCol);

        db.execSQL(insertNameCol);


        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_PRESET_NAME, Presets.DEFAULT_NAME);

        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_CURR_TIP ,presets.getCurrTip());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_CURR_TAX, presets.getCurrTax());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_CURR_SPLIT, presets.getCurrSplit());

        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_MAX_TIP, presets.getMaxTip());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_MAX_TAX, presets.getMaxTip());
        nameValues.put(PresetContract.TABLE_PRESET_INFO.COL_MAX_SPLIT, presets.getMaxSplit());

        final String insertNameData = insertWithValues(nameValues, PresetContract.TABLE_PRESET_INFO.TABLE_NAME);

        Log.i("test", "INSERTING NAME DATA: " + insertNameData);

        db.execSQL(insertNameData);

    }


    private String insertWithValues(ContentValues values, String table){

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
