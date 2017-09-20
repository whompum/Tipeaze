package com.example.bryan.tipeaze;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.bryan.tipeaze.CustomViews.CurrencyEditText;
import com.example.bryan.tipeaze.CustomViews.OnTotalChanged;

public class AddTotalDialog extends DialogFragment implements View.OnClickListener, OnTotalChanged {

    public static final String TAG = "AddTotalDialog";

    public static final int contentLayout = R.layout.add_total_dialog_layout;

    public static final String TOTAL_KEY = "TheTotal";

    private CurrencyEditText totalDisplay;
    private FloatingActionButton doneFab;

    private Animation hideAnim = null;
    private Animation showAnim = null;

    private boolean isShowing = true;

    private Result resultClient;


    @Override //Recieves notifications when CurrencyEditText's value changes
    public void onTotalChanged(String total) {
        //Parse to a raw string with numbers only, then check if there is any number but a zero
        final long pennies = Long.valueOf(total); //Should be guarenteed to be a digit-only value

        if(pennies > 0 & !isShowing)
            showView(doneFab);
        else if (pennies == 0 & isShowing)
            hideView(doneFab);
    }


    public static AddTotalDialog newInstance(Result<String> resultClient){
        final AddTotalDialog dialog = new AddTotalDialog();
        dialog.resultClient = resultClient;
        return dialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog theD = new Dialog(this.getContext());
        theD.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return theD;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View contentView = inflater.inflate(contentLayout, container, false);

        //Do work to do a circular reveal on the view


        initCalc(contentView);

        this.totalDisplay = (CurrencyEditText) contentView.findViewById(R.id.totalDisplay);
        this.doneFab = (FloatingActionButton) contentView.findViewById(R.id.doneFab);
        doneFab.setOnClickListener(this);

        String total = "0";


        if (savedInstanceState != null) {
            total = savedInstanceState.getString(TOTAL_KEY);
        }

        totalDisplay.setOnTotalChangedListener(this);

        totalDisplay.setText(total);
          return contentView;
    }


    private void initCalc(final View contentView){
        final GridLayout calculator = (GridLayout) contentView.findViewById(R.id.calcGridLayout);

        final int numChildren = calculator.getChildCount();

        for (int i = 0; i < numChildren; i++) {

            final View child = calculator.getChildAt(i);

            child.setOnClickListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TOTAL_KEY, totalDisplay.getText().toString());
    }

    public void setResultClient(Result<String> result) {
        this.resultClient = result; //Used in case i switch from constructor to newInstance() creation pattern
    }

    @Override
    public void onClick(View v) {

        Button castView = null;

        final int id = v.getId();

        if (v instanceof Button)
            castView = (Button) v;

        if (id == R.id.backspaceCalc)
            totalDisplay.onBackSpace();

         else if (id == R.id.deleteCalc)
            totalDisplay.onDelete();


        else if (id == R.id.doneFab /** & resultClient != null**/)
            resultClient.<String>sendResult(totalDisplay.getText().toString());

        else if (castView != null & id == -1)
             totalDisplay.append(castView.getText());

    }

    public void hideView(final View view) {

        if(hideAnim == null)
            hideAnim = AnimationUtils.loadAnimation(getContext(), R.anim.hide_animation);

        hideAnim.setFillAfter(false);

        view.startAnimation(hideAnim);

        toggleIsShowing(false);

        view.setVisibility(View.GONE);
    }


    public void showView(final View view){

        if(showAnim == null)
            showAnim = AnimationUtils.loadAnimation(getContext(), R.anim.show_animation);

        showAnim.setFillAfter(false);

        view.startAnimation(showAnim);

        toggleIsShowing(true);

        view.setVisibility(View.VISIBLE);
    }

    private void toggleIsShowing(boolean b){
        isShowing = b;
    }

    private void registerContentReveal(){
        //TODO add material circular reveal animation
    }



    public interface Result<String> {
        void sendResult(String result);
    }



}
