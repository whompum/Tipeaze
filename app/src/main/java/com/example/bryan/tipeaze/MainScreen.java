package com.example.bryan.tipeaze;

import android.app.Dialog;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bryan.tipeaze.CustomViews.RobotoBasedTextview;
import com.example.bryan.tipeaze.CustomViews.TipView;
import com.example.bryan.tipeaze.Presets.PresetAdapter;
import com.example.bryan.tipeaze.Presets.PresetQueryManager;

public class MainScreen extends AppCompatActivity {


    private TipView tipView;

    private AppCompatSpinner presetSelector;
    private AppCompatImageButton extras;

    private RobotoBasedTextview tipCardTipTextView;
    private RobotoBasedTextview tipCardTaxTextView;

    private RobotoBasedTextview personalTextView;
    private RobotoBasedTextview partyTextView;

    private FloatingActionButton addFab;
    private FloatingActionButton splitFab;
    //^Content of top Cards

    //Content of bill data
    private RobotoBasedTextview totalTextView;
    private int totalTextRef = R.string.total; //"Total" text of the totalTextView
    private int perPersonTextRef = R.string.per_person; //"Per Person" text of the totalTextView

    private RobotoBasedTextview secondaryProductTotalTextView;

    private RobotoBasedTextview productTotalTextView;
    private RobotoBasedTextview grandTotalTextView;

    private RobotoBasedTextview tipDisplayTextView;
    private RobotoBasedTextview taxDisplayTextView;

    private LinearLayout splitDisplayLayout;
    private RobotoBasedTextview splitDisplay;

    private String currTotal;


    private AddTotalDialog addTotalDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipview_card_layout);


        initCardRes();

        initBillData();

        tempSetupSpinner();
    }


    private void tempSetupSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.presetSelector);
        PresetQueryManager queryManager = new PresetQueryManager(getSupportLoaderManager(), this);
        PresetAdapter adapter = queryManager.getAdapter();

        spinner.setAdapter(adapter);
    }


    private void initCardRes(){
        this.tipView = (TipView) findViewById(R.id.tipView);
        tipView.splitListener = new TipView.tempSplit() {
            @Override
            public void blah(int num) {
                changeSplitLayout(num);
            }
        };
        this.presetSelector = (AppCompatSpinner) findViewById(R.id.presetSelector);
        this.extras = (AppCompatImageButton) findViewById(R.id.extras);

        this.tipCardTipTextView = (RobotoBasedTextview) findViewById(R.id.tipCardTipDisplay);
        this.tipCardTaxTextView = (RobotoBasedTextview) findViewById(R.id.tipCardTaxDisplay);

        this.personalTextView = (RobotoBasedTextview) findViewById(R.id.personalTextView);
        this.partyTextView = (RobotoBasedTextview) findViewById(R.id.partyTextView);

        this.addFab = (FloatingActionButton) findViewById(R.id.addFab);
        addFab.setOnClickListener(addFabListener);

        this.splitFab = (FloatingActionButton) findViewById(R.id.splitFab);
             splitFab.setOnClickListener(splitFabListener );
    }

    private void initBillData(){
        this.totalTextView = (RobotoBasedTextview) findViewById(R.id.totalPerPersonTextView);
        this.secondaryProductTotalTextView = (RobotoBasedTextview) findViewById(R.id.productTotalSecondary);
        this.productTotalTextView = (RobotoBasedTextview) findViewById(R.id.productTotal);
        this.grandTotalTextView = (RobotoBasedTextview) findViewById(R.id.grandTotal);
        this.taxDisplayTextView = (RobotoBasedTextview) findViewById(R.id.taxDisplay);
        this.tipDisplayTextView = (RobotoBasedTextview) findViewById(R.id.tipDisplay);

        this.splitDisplayLayout = (LinearLayout) findViewById(R.id.splitDisplayLayout);
        this.splitDisplay = (RobotoBasedTextview) findViewById(R.id.splitDisplay);
    }


    //Called when the user changes the total of the Bill.
    private void onTotalChanged(){
        if(productTotalTextView.getVisibility() == View.INVISIBLE){
            //Animate productTotalTextView
        }




    }

    final View.OnClickListener addFabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainScreen.this.addTotalDialog = AddTotalDialog.newInstance(null);
            addTotalDialog.registerResultListener(newTotalListener);
            addTotalDialog.show(getSupportFragmentManager(), "bluh");
        }
    };

    final View.OnClickListener splitFabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Animate the shit outta errything
        }
    };


    final AddTotalDialog.Result newTotalListener = new AddTotalDialog.Result<String>() {
        @Override
        public void sendResult(String result) {
            //Keep the toast as it ads to the UX
            Toast.makeText(MainScreen.this, result, Toast.LENGTH_SHORT).show();
            addTotalDialog.dismiss();
            currTotal = result;
            onTotalChanged();
        }
    };


    private void changeSplitLayout(int num){
        //8 is max for split by default, so we only go up to 8.

        final int A = splitDisplayLayout.getChildCount() - 1; //Last usable index. Starting point for loop

        if(num <= 8){

            for(int i = num; i > 0; i--) //Makes the ImageViews VISIBLE in sequential order :D
                splitDisplayLayout.getChildAt(A-i).setVisibility(View.VISIBLE);

        }else{
            //do something special if user set max split to like 10, and we've surpassed 8.
            //I.E. split is 9 so make the eighth person icon glow or something fancy like that.
        }
        splitDisplay.setText(String.valueOf(num));
    }

}
