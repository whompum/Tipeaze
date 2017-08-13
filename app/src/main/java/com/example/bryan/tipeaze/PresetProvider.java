package com.example.bryan.tipeaze;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class PresetProvider extends ContentProvider {


    public static final String AUTHORITY = "com.example.bryan.tipeaze.PresetProvider";
    public static final Uri CONTENT_URI_MAIN = Uri.parse("content://" + AUTHORITY);

    private static UriMatcher matcher;

    private PresetHelper helper;

    private String illagalUriMessage = "";

    static{
        matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY, PresetContract.TABLE_PRESET_INFO.TABLE_NAME, PresetContract.TABLE_PRESET_INFO.RESULT_PRESET_INFO_TABLE);
        matcher.addURI(AUTHORITY, PresetContract.TABLE_PRESET_INFO.TABLE_NAME, PresetContract.TABLE_PRESET_INFO.RESULT_PRESET_INFO_TABLE_ITEM);

        matcher.addURI(AUTHORITY, PresetContract.TABLE_PRESET_NAMES.TABLE_NAME, PresetContract.TABLE_PRESET_NAMES.RESULT_PRESET_NAME_TABLE);
        matcher.addURI(AUTHORITY, PresetContract.TABLE_PRESET_NAMES.TABLE_NAME, PresetContract.TABLE_PRESET_NAMES.RESULT_PRESET_NAME_TABLE_ITEM);

    }


    @Override
    public boolean
    onCreate() {
        helper = new PresetHelper(getContext(), PresetContract.DATABASE_NAME, null, PresetContract.VERSION);

        final Resources res = getContext().getResources();
        if(res!=null){
            illagalUriMessage = res.getString(R.string.illegalUriMessage);
        }

        return true;
    }

    @Nullable
    @Override
    public Cursor
    query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String tableName = "";
        String sortBy = sortOrder;


        if(PresetContract.TABLE_PRESET_INFO.doesUriMatch(getResultFromUri(uri)))
            tableName = PresetContract.TABLE_PRESET_INFO.TABLE_NAME;

        else if(PresetContract.TABLE_PRESET_NAMES.doesUriMatch(getResultFromUri(uri))) {
            tableName = PresetContract.TABLE_PRESET_NAMES.TABLE_NAME;

            if(sortBy == null)
                sortBy = PresetContract.TABLE_PRESET_NAMES.TABLE_PRESET_NAMES_SORT_ORDER;
        }

        else //should throw exception
            return null;


        final Cursor querResults = getReadableDb().query(
                tableName,
                projection,
                selection,
                selectionArgs,
                "",
                "",
                sortBy);

        querResults.close();

        registerObservation(querResults, uri);


        return querResults;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int result = getResultFromUri(uri);

         String mimeType = PresetContract.TABLE_PRESET_INFO.getTypeFromResult(result);

        if( !mimeType.equals(PresetContract.MIME_NO_TYPE) ) //if not mime_no_type, then it's a valid Uri so return
            return mimeType;

        else //if the result did equal NO_TYPE then its invalid, so check the other table. If still not match, then return MIME_NO_TYPE
            mimeType = PresetContract.TABLE_PRESET_NAMES.getTypeFromResult(result);


        return mimeType;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        /**
         * check which table i want to access
         * Then using that information, append
         * the rowID to the tables URI, and return. E.G.
         * return ContentUri.withAppendedId(someTableUri, rowID);
         */

        Uri newUri = uri;
        String tableName = resolveTableFromUri(getResultFromUri(uri));

        final long rowId = getWriteableDb().insert(tableName, null, values);

        newUri = ContentUris.withAppendedId(uri, rowId);

        notifyChange(uri, null);

        return newUri;
    }

    @Override
    public int
    delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int numRows = getWriteableDb().delete(resolveTableFromUri(getResultFromUri(uri)),
                selection,
                selectionArgs);

        notifyChange(uri, null);


        return numRows;
    }

    @Override
    public int
    update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

       final int row = getWriteableDb().update(resolveTableFromUri(getResultFromUri(uri)),
                                values,
                                selection,
                                selectionArgs);

        notifyChange(uri, null);

     return row;
    }


    private static int getResultFromUri(Uri uri){
        return matcher.match(uri);
    }

    private SQLiteDatabase getReadableDb(){
        return helper.getReadableDatabase();
    }

    public SQLiteDatabase getWriteableDb(){
        return helper.getWritableDatabase();
    }

    private String resolveTableFromUri(int result){
        if(PresetContract.TABLE_PRESET_INFO.doesUriMatch(result))
            return PresetContract.TABLE_PRESET_INFO.TABLE_NAME;

        else if(PresetContract.TABLE_PRESET_NAMES.doesUriMatch(result))
            return PresetContract.TABLE_PRESET_NAMES.TABLE_NAME;


    throw new IllegalArgumentException(illagalUriMessage);
    }


    private void notifyChange(Uri uri, @Nullable ContentObserver observer){

        final ContentResolver resolver = getContext().getContentResolver();

        if(resolver!=null){
            resolver.notifyChange(uri, observer);
        }

    }

    private void registerObservation(Cursor cursor, Uri uri){
        final ContentResolver resolver = getContext().getContentResolver();
        if(resolver!=null)
         cursor.setNotificationUri(resolver, uri);
    }




}
