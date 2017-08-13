package com.example.bryan.tipeaze;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

public class Thumb extends Drawable {


    private TipView.TYPE type;

    private String thumbTypeText;

    private int touchHighlight;
    private int thumbHighlight;

    private int touchTextHighlight;
    private int textHighlight;
    private int thumbTextPadding;


    private int thumbSize;
    private Rect paddingRect = new Rect();

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private StaticLayout textLayout;


    private TipView.TOUCHSTATES touchState = TipView.TOUCHSTATES.DEFAULT;


    private int currValue; //Percentage that thumb has gone in the track

    private float floor;
    private float cieling;

    private float currTheta = 0f;




    Thumb(TypedArray array, TipView.TYPE type, String text, Resources resources){

        for(int i = 0; i < array.getIndexCount(); i++){
            final int attribute = array.getIndex(i);

            if(attribute == R.styleable.TipView_thumbHighlight)
                this.thumbPaint.setColor(  (thumbHighlight = array.getColor(attribute, Color.BLUE))  );

            else if(attribute == R.styleable.TipView_thumbTouchHighlight)
                this.touchHighlight = array.getColor(attribute, Color.parseColor("#fff000"));

            else if(attribute == R.styleable.TipView_thumbTouchTextHighlight)
                this.touchTextHighlight = array.getColor(attribute, Color.parseColor("#fff000") );

            else if(attribute == R.styleable.TipView_thumbTextSize)
                //give default sizing of about 12 DP units.
                textPaint.setTextSize(array.getDimensionPixelSize(attribute, 12));

            else if(attribute == R.styleable.TipView_thumbTextHighlight)
                this.textPaint.setColor( (this.textHighlight = array.getColor(attribute, Color.GRAY)) );

            else if(attribute == R.styleable.TipView_tipThumbPadding)
                this.thumbTextPadding = array.getDimensionPixelSize(attribute, 0);

            else if(attribute == R.styleable.TipView_thumbSize)
                this.thumbSize = array.getDimensionPixelSize(attribute, 0);

            this.textPaint.setTypeface(Typeface.createFromAsset(resources.getAssets(), TipView.FONT_PRIMARY));



        }


        this.type = type;

        this.thumbTypeText = text;


        this.paddingRect.set(thumbTextPadding,
                             thumbTextPadding,
                             thumbSize-thumbTextPadding,
                             thumbSize-thumbTextPadding);





        textLayout = new StaticLayout(thumbTypeText, textPaint, (int)Layout.getDesiredWidth(thumbTypeText, textPaint),
                Layout.Alignment.ALIGN_CENTER, 1.0f, 0f, true);



        if(Build.VERSION.SDK_INT < 23)
          shadowPaint.setColor(resources.getColor(R.color.fadingGray));
        else if (Build.VERSION.SDK_INT >= 23)
          shadowPaint.setColor(resources.getColor(R.color.fadingGray, null));


    }

    @Override
    public void draw(@NonNull Canvas canvas) {


        canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), getBounds().width()*0.5f, shadowPaint);

        canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), (getBounds().width()*0.5f)-3, thumbPaint);

        canvas.save();
        canvas.translate(getBounds().centerX() - (textLayout.getWidth()*0.5f) ,
                         getBounds().centerY() - (textLayout.getHeight()*0.5f) );

        textLayout.draw(canvas);
        canvas.restore();

    }

    private void onTouchStateChange(){

        if(touchState == TipView.TOUCHSTATES.FOCUSED) {
            thumbPaint.setColor(touchHighlight);
            textPaint.setColor(touchTextHighlight);
        }



        else if(touchState == TipView.TOUCHSTATES.DEFAULT) {
            thumbPaint.setColor(thumbHighlight);
            textPaint.setColor(textHighlight);
        }


    }

    void setTouchState(TipView.TOUCHSTATES touchState){
        this.touchState = touchState;
        onTouchStateChange();
    }

    void setFloorAndCiel(float floor, float ciel){
        this.floor = floor;
        this.cieling = ciel;

        if(floor > ciel) //Used for cases where the thumbs cieling is past 2PI, and floor is behind 2PI (physically)
            cieling +=(float)(Math.PI*2);
    }

    void updateCurrValue(float theta){
        this.currValue = (int)(100 / ((cieling-floor)/(theta-floor)) ); //Needs fixing...

        if(type == TipView.TYPE.TIP)
            Log.i("test", String.valueOf(currValue));

    }

    float getFloor(){
        return this.floor;
    }

    float getCiel(){
        return cieling;
    }

    float getValue(){
        return currValue;
    }

    void setCurrTheta(final float theta){
        this.currTheta = theta;
        updateCurrValue(theta);
    }

    float getCurrTheta(){
        return currTheta;
    }


    TipView.TYPE getType(){
        return type;
    }


    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        //do nothing
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        //do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }






}


