package com.example.bryan.tipeaze.Presets;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Database schema is
 * table - currTipValue / currTaxValue / currSplitValue
 *         maxTipValue / maxTaxValue / maxSplitValue
 */

public class PresetContract implements BaseColumns {

    public static final String DATABASE_NAME = "presets.db";

    public static final int VERSION = 1;

    public static final String MIME_NO_TYPE = "NO_TYPE";




    public static final class TABLE_PRESET_INFO implements BaseColumns{

        public static final String TABLE_NAME = "preset_info";

        public static final String COL_CURR_TIP = "curr_tip";
        public static final String COL_CURR_TAX = "curr_tax";
        public static final String COL_CURR_SPLIT = "curr_split";

        public static final String COL_MAX_TIP = "max_tip";
        public static final String COL_MAX_TAX = "max_tax";
        public static final String COL_MAX_SPLIT = "max_split";

        public static final String COL_PRESET_NAME = "preset_name";


        public static final String TABLE_PRESET_INFO_WHERE_CLAUSE = "where " + COL_PRESET_NAME + " =?";


        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME +
                        " ( " + _ID + " integer primary key autoincrement, "
                        + COL_PRESET_NAME + " text not null, "
                        + COL_CURR_TIP + " integer not null, "
                        + COL_CURR_TAX + " integer not null, "
                        + COL_CURR_SPLIT + " integer not null, "
                        + COL_MAX_TIP + " integer not null, "
                        + COL_MAX_TAX + " integer not null, "
                        + COL_MAX_SPLIT + " integer not null); ";


        public static final String MIME_TYPE_DIR = "vnd.com.example.bryan/dir/presets_info";
        public static final String MIME_TYPE_ITEM = "vnd.com.example.bryan/item/presets_info";


        public static final String MIME_PRESET_DIR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE_DIR;

        public static final String MIME_PRESET_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE_ITEM;


        public static Uri presetUri = Uri.withAppendedPath(PresetProvider.CONTENT_URI_MAIN, TABLE_NAME);

        public static final int RESULT_PRESET_INFO_TABLE_ITEM = 2;
        public static final int RESULT_PRESET_INFO_TABLE = 1;



        public static String getTypeFromResult(final int result){
            if(result == RESULT_PRESET_INFO_TABLE)
                return MIME_PRESET_DIR;

            else if(result == RESULT_PRESET_INFO_TABLE_ITEM)
                return MIME_PRESET_ITEM;

        return MIME_NO_TYPE;
        }




        public static boolean doesUriMatch(int result){
            if(result == RESULT_PRESET_INFO_TABLE | result == RESULT_PRESET_INFO_TABLE_ITEM)
                return true;

            return false;
        }


    }


    public static final class TABLE_PRESET_NAMES implements BaseColumns {

        public static final String TABLE_PRESET_NAMES_SORT_ORDER = "ASC";

        public static final String TABLE_NAME = "preset_names";

        public static final String COL_NAME = "name";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME +
                " ( " + _ID + " integer primary key autoincrement, "
                +  COL_NAME + " text not null);";




        public static Uri presetUri = Uri.withAppendedPath(PresetProvider.CONTENT_URI_MAIN, TABLE_NAME);


        public static final String MIME_TYPE_DIR = "vnd.com.example.bryan/dir/preset_name";
        public static final String MIME_TYPE_ITEM = "vnd.com.example.bryan/item/preset_name";


        public static final String MIME_PRESET_DIR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MIME_TYPE_DIR;

        public static final String MIME_PRESET_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MIME_TYPE_ITEM;


        public static final int RESULT_PRESET_NAME_TABLE = 3;
        public static final int RESULT_PRESET_NAME_TABLE_ITEM = 4;



        public static String getTypeFromResult(final int result){
            if(result == RESULT_PRESET_NAME_TABLE)
                return MIME_PRESET_DIR;

            else if(result == RESULT_PRESET_NAME_TABLE_ITEM)
                return MIME_PRESET_ITEM;

            return MIME_NO_TYPE;
        }


        public static boolean doesUriMatch(int result){
            if(result == RESULT_PRESET_NAME_TABLE | result == RESULT_PRESET_NAME_TABLE_ITEM)
                return true;

        return false;
        }


    }


}
