package com.example.bryan.tipeaze;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by bryan on 7/27/2017.
 */

public class RobotoBasedTextview extends AppCompatTextView {

    public enum FONTS{BOLD, LIGHT, MEDIUM, REGULAR, THIN };

    private FONTS fonts;

    public RobotoBasedTextview(Context context) {
        super(context);
    }

    public RobotoBasedTextview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        int value = 1;

        final TypedArray fontArr = context.obtainStyledAttributes(attrs, R.styleable.RobotoBasedTextview);

        for(int i = 0; i < fontArr.getIndexCount(); i++){
            final int attr = fontArr.getIndex(i);

            if(attr == R.styleable.RobotoBasedTextview_font)
                 value = fontArr.getInt(attr, 1);
        }
        fontArr.recycle();

        this.fonts = fontTypeFromValue(value);

        setTypeface(typefaceFromFontType(fonts));

    }


    private FONTS fontTypeFromValue(int index){

        switch(index){
            case 0: return FONTS.BOLD;
            case 1: return FONTS.LIGHT;
            case 2: return FONTS.MEDIUM;
            case 3: return FONTS.REGULAR;
            case 4: return FONTS.THIN;
        }
        return FONTS.LIGHT;
    }

    private Typeface typefaceFromFontType(FONTS fonts){

        final AssetManager manager = getResources().getAssets();

        switch(fonts){

            case BOLD:
                return Typeface.createFromAsset(manager, "Roboto-Bold.ttf");

            case LIGHT:
                return Typeface.createFromAsset(manager, "Roboto-Light.ttf");

            case MEDIUM:
                return Typeface.createFromAsset(manager, "Roboto-Medium.ttf");

            case REGULAR:
                return Typeface.createFromAsset(manager, "Roboto-Regular.ttf");

            case THIN:
                return Typeface.createFromAsset(manager, "Roboto-Thin_0.ttf");

        }
        return Typeface.createFromAsset(manager, "Roboto-Light.ttf");
    }


    public void setFont(FONTS fonts){
        setTypeface(this.typefaceFromFontType(fonts));
    }

}
