package com.example.bryan.tipeaze.Presets;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.bryan.tipeaze.R;

/**
 * TODO basically the problem is i need to always have some content displayed. So if the user never saved any presets
 * a default one is given. The question is how do i take that responsibility out of any class and have it be default functionality?
 * This class is responsible for simply one thing: To query the database and return data. It shouldn't care about how much
 * data was found or whatever, just query, give to adapter, and give to client.
 */

public class PresetQueryManager implements LoaderTemplate.LoaderResponseClient {

    private static final int LOADER_ID = 0b1100100;

    private LoaderTemplate presetLoader;

    private PresetAdapter adapter;

    private CursorLoader loader;

    private String[] columns =  { PresetContract.TABLE_PRESET_NAMES.COL_NAME };

    private boolean isEmpty;

    public PresetQueryManager(@NonNull LoaderManager manager, Context context){
        presetLoader = new LoaderTemplate(manager, this, LOADER_ID);

        loader = new CursorLoader(context);
        loader.setUri(PresetContract.TABLE_PRESET_NAMES.presetUri);
        loader.setProjection(columns);

        adapter = new PresetAdapter(context, R.layout.presect_selector_hint_appearance, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id != LOADER_ID)
             return null;

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swapCursorAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public PresetAdapter getAdapter(){
        return adapter;
    }

    public void swapCursorAdapter(Cursor cursor){
        adapter.swapCursor(cursor);
    }

    public interface OnEmptyLoad{
        void onEmptyLoad();
    }




}
