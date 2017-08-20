package com.example.bryan.tipeaze.Presets;

import android.content.Context;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.database.Cursor;
import android.os.Bundle;

/**
 * TODO change the thrown RuntimeException to something more appropriate
 * TODO if onCreateLoader is defined in a client class, then who is the loader returned to? IDK it this matters.
 * TODO if above matters, remove onCreateLoader from the interface ...queryResponse.
 */



public class PresetLoader implements LoaderManager.LoaderCallbacks<Cursor> {


    private PresetLoaderQueryResponse client;

    private LoaderManager manager;

    public PresetLoader(Context context, PresetLoaderQueryResponse client){
        if(client == null) throw new RuntimeException("PresetLoaderQueryResponse is null");
        else
            this.client = client;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return client.onCreateLoader(id, bundle);
    }

    @Override  //Where the actual data will Go to
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        client.onLoadFinished(loader, cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        client.onLoaderReset(loader);
    }

    public interface PresetLoaderQueryResponse{

        public Loader<Cursor> onCreateLoader(int id, Bundle args);

        public void onLoadFinished(Loader<Cursor> loader, Cursor data);

        public void onLoaderReset(Loader<Cursor> loader);

    }



}
