package com.example.bryan.tipeaze;

import android.app.Dialog;
import android.icu.util.Currency;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.example.bryan.tipeaze.CustomViews.CurrencyEditText;
import com.example.bryan.tipeaze.CustomViews.RobotoBasedTextview;

/**
 * Created by bryan on 9/1/2017.
 */

public class AddTotalDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "AddTotalDialog";

    public static final int contentLayout = R.layout.add_total_dialog_layout;

    public static final String TOTAL_KEY = "TheTotal";

    private String total = "0";

    private CurrencyEditText totalDisplay;
    private FloatingActionButton doneFab;

    public interface Result<String> {
        void sendResult(String result);
    }

    private Result resultClient;


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

        /**
         * Init Views based on savedInstanceState if i can
         */

        final GridLayout calculator = (GridLayout) contentView.findViewById(R.id.calcGridLayout);

        final int numChildren = calculator.getChildCount();

        for (int i = 0; i < numChildren; i++) {

            final View child = calculator.getChildAt(i);
            Button castChild = null;

            if (child instanceof Button | child instanceof ImageButton) {
                castChild = (Button) child;
                castChild.setOnClickListener(this);
            }

        }
        this.totalDisplay = (CurrencyEditText) contentView.findViewById(R.id.totalDisplay);
        this.doneFab = (FloatingActionButton) contentView.findViewById(R.id.doneFab);
        doneFab.setOnClickListener(this);

        if (savedInstanceState != null) {
            this.total = savedInstanceState.getString(TOTAL_KEY);
        }

        totalDisplay.setText(total);
          return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TOTAL_KEY, total);
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


        else if (id == R.id.doneFab & resultClient != null)
            resultClient.<String>sendResult(totalDisplay.getText().toString());

        else if (castView != null & id == -1)
             totalDisplay.append(castView.getText());

    }





}
