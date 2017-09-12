package com.example.bryan.tipeaze.CustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SpinnerAdapter;

import com.example.bryan.tipeaze.CurrencyFormatter;

import java.text.DecimalFormat;
import java.util.Locale;

public class CurrencyEditText extends AppCompatEditText {

    public static final String TAG = "CurrencyEditText";

    private final CurrencyFormatter formatter = CurrencyFormatter.getInstance();

    public CurrencyEditText(Context context) {
        super(context);
    }

    public CurrencyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        addTextChangedListener(watcher);
    }

    public CurrencyEditText(Context c, @Nullable AttributeSet set, int defStyleAttr){
        super(c, set, defStyleAttr);
    }

    private void setCursor(final int textLength){
        setSelection(textLength);
    }

    public void onBackSpace(){ //a is the string representation of this views Editable object; Named 'a' for simplicity
        //'r' is the result after the substring operation :)
        final String a = getText().toString();
        final String r = a.substring(0, a.length()-1);
        setText(r);
    }

    public void onDelete(){
        resetText();
    }

    public void resetText(){
        setText("0");
    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            removeTextChangedListener(this);

            final String fuck = s.toString();

            if(fuck.length() == 0)
                return;

            final String cleanedText = formatter.cleanText(fuck);

            final double cookedText = formatter.cookText(cleanedText);

            final String formattedValue = formatter.format(cookedText);

            setText(formattedValue);

            setCursor(formattedValue.length());

            addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



}



