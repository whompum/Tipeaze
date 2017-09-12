package com.example.bryan.tipeaze.CustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;


import com.example.bryan.tipeaze.CurrencyFormatter;


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

        if(isStringEmpty(a))
            return;

        final String r = a.substring(0, a.length()-1);
        setText(r);
    }

    public void onDelete(){
        resetText();
    }

    public void resetText(){
        setText("0");
    }

    public boolean isStringEmpty(String text){
        return (text.length() == 0);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            removeTextChangedListener(this);

            final String unCharredText = s.toString();

            if(isStringEmpty(unCharredText))
                return;

            final String cleanedText = formatter.cleanText(unCharredText);

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



