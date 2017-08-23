package com.example.bryan.tipeaze.Presets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.database.Cursor;
import android.os.Bundle;

public class LoaderTemplate implements LoaderCallbacks<Cursor> {

    private LoaderResponseClient client;

    public LoaderTemplate(@NonNull LoaderManager manager, @NonNull LoaderResponseClient client, int loaderId){
        this.client = client;
        manager.initLoader(loaderId, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return client.onCreateLoader(id, bundle);
    }

    @Override  //Where the actual data will go to
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        client.onLoadFinished(loader, cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        client.onLoaderReset(loader);
    }



    public interface LoaderResponseClient{

        Loader<Cursor> onCreateLoader(int id, Bundle args);

        void onLoadFinished(Loader<Cursor> loader, Cursor data);

        void onLoaderReset(Loader<Cursor> loader);

    }

}
