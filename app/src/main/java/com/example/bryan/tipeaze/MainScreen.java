package com.example.bryan.tipeaze;

import android.content.ContentValues;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.bryan.tipeaze.CustomViews.RobotoBasedTextview;
import com.example.bryan.tipeaze.CustomViews.TipView;
import com.example.bryan.tipeaze.Presets.PresetContract;
import com.example.bryan.tipeaze.Presets.PresetQueryManager;

public class MainScreen extends AppCompatActivity {


    private TipView tipView;

    private AppCompatSpinner presetSelector;
    private AppCompatImageButton extras;

    private RelativeLayout personalPartyTab;

    private RobotoBasedTextview personalTextView;
    private RobotoBasedTextview partyTextView;

    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeManager.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        this.tipView = (TipView) findViewById(R.id.tipView);
        this.presetSelector = (AppCompatSpinner) findViewById(R.id.presetSelector);
        this.extras = (AppCompatImageButton) findViewById(R.id.extras);

        this.personalPartyTab = (RelativeLayout) findViewById(R.id.personalPartyTabs);
        this.personalTextView = (RobotoBasedTextview) personalPartyTab.findViewById(R.id.personalTextView);
        this.partyTextView = (RobotoBasedTextview) personalPartyTab.findViewById(R.id.partyTextView);

        this.addFab = (FloatingActionButton) findViewById(R.id.addFab);




        tempSetupSpinner();
    }


    private void tempSetupSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.presetSelector);
        PresetQueryManager queryManager = new PresetQueryManager(getSupportLoaderManager(), this);
        spinner.setAdapter(queryManager.getAdapter());

    }




}
