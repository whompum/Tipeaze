package com.example.bryan.tipeaze;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.example.bryan.tipeaze.Abstractions.SplitChangeListener;
import com.example.bryan.tipeaze.BillingData.AddTotalDialog;
import com.example.bryan.tipeaze.BillingData.BillingManipulator;
import com.example.bryan.tipeaze.BillingData.CurrencyFormatter;
import com.example.bryan.tipeaze.CustomViews.RobotoBasedTextview;
import com.example.bryan.tipeaze.CustomViews.TipView;
import com.example.bryan.tipeaze.Abstractions.TipViewChangeListener;
import com.example.bryan.tipeaze.Presets.PresetAdapter;
import com.example.bryan.tipeaze.Presets.PresetQueryManager;

public class MainScreen extends AppCompatActivity implements AddTotalDialog.Result<String>{

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

    private AddTotalDialog addTotalDialog;

    private CurrencyFormatter formatter = CurrencyFormatter.getInstance();

    /**
     * BillingManipulator takes and manipulates penny values. E.G. returns a tip @ 10 percent of a $100 bill
     * This should be used in conjunction with CurrencyFormatter t o convert said values into
       Locale-formatted values.
     */
    BillingManipulator billingManipulator = new BillingManipulator();

    private SplitChangeListener splitChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipview_card_layout);

        this.tipView = (TipView) findViewById(R.id.tipView);
             tipView.setChangeListener(this.tipViewChangeListener);

        this.presetSelector = (AppCompatSpinner) findViewById(R.id.presetSelector);
        this.extras = (AppCompatImageButton) findViewById(R.id.extras);

        this.tipCardTipTextView = (RobotoBasedTextview) findViewById(R.id.tipCardTipDisplay);
        this.tipCardTaxTextView = (RobotoBasedTextview) findViewById(R.id.tipCardTaxDisplay);

        this.personalTextView = (RobotoBasedTextview) findViewById(R.id.personalTextView);
        this.partyTextView = (RobotoBasedTextview) findViewById(R.id.partyTextView);

        this.addFab = (FloatingActionButton) findViewById(R.id.addFab);
             addFab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     addTotalDialog = new AddTotalDialog();
                     addTotalDialog.setResultClient(MainScreen.this);
                     addTotalDialog.show(getSupportFragmentManager(), AddTotalDialog.TAG);
                 }
             });




        this.splitFab = (FloatingActionButton) findViewById(R.id.splitFab);
             splitFab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                   //Swap The Total FRAG for the PER PERSON frag.
                   //SHOW split > 1 ELSE, HIDE.
                 }
             });


        initPresets();
        addBillTotal();
    }

    /**
     *
     * @param result The resulting value from the addTotalDialog. Should probably refractor the name
     *               to something else more specific.
     */
    @Override
    public void sendResult(String result) {

        final long pennies = Long.parseLong(formatter.cashToPennies(result));

        billingManipulator.resetWithBillTotal(pennies);

        addTotalDialog.dismiss();

        tipView.reset();
    }

    private final TipViewChangeListener tipViewChangeListener = new TipViewChangeListener() {
        @Override
        public void onTipChange(int tip) {
            //Set the tip value in the card
            Log.i("path", "OnTipChange");
            tipCardTipTextView.setText(TipView.utilToPercent(String.valueOf(tip)));
            billingManipulator.changeTip(tip, true);
        }

        @Override
        public void onTaxChange(int tax) {
            //set the tax value in the card.
            Log.i("path", "OnTaxChange");
            tipCardTaxTextView.setText(TipView.utilToPercent(String.valueOf(tax)));
            billingManipulator.changeTax(tax, true);
        }

        @Override
        public void onSplitChange(int split) {

            if(doesPersonalFragmentExist()) //if personalFragment
            splitChangeListener.changeSplit(split);

            //Only keep false, IF the current fragment is Personal, not party. Else it's true
            if(!doesPersonalFragmentExist())
            billingManipulator.setSplit(split, false);
        }
    };


    private void initPresets(){
        Spinner spinner = (Spinner) findViewById(R.id.presetSelector);
        PresetQueryManager queryManager = new PresetQueryManager(getSupportLoaderManager(), this);
        PresetAdapter adapter = queryManager.getAdapter();

        spinner.setAdapter(adapter);
    }
    private void addBillTotal(){


        PersonalFragment billTotal = PersonalFragment.newInstance(null);

        splitChangeListener = billTotal;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, billTotal, PersonalFragment.TAG)
                .commit();

    }

    /**
     *
     * @returns whether or not i should notify the fragment of split changes.
     */
    private boolean doesPersonalFragmentExist(){
        return true;
    }

}
