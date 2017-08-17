package com.example.bryan.tipeaze.CustomViews;

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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;

import com.example.bryan.tipeaze.CustomViews.TipView;
import com.example.bryan.tipeaze.R;

/**
 * Bounds of this object should be the (tracksize + strokesize*2)
 */

public class Track extends Drawable {

   private String valueText = " ";
   private TextPaint valueTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
   private Rect valueBounds = new Rect();


   private Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

   private Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
   private float strokeSize;

   private Paint tickmarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
   private float tickmarkWidth;


   //the theta angle's of the tick marks
   public final float[] tickmarks = {TipView.tipTickMarkRad, TipView.taxTickMarkRad, TipView.splitTickMarkRad};


    Track(TypedArray array, Resources resources){

        for(int i =0; i < array.getIndexCount(); i++){

            final int attribute = array.getIndex(i);

            if (attribute ==  R.styleable.TipView_trackHighlight)
                  trackPaint.setColor(array.getColor(attribute, Color.parseColor("#000fff")));

            else if(attribute == R.styleable.TipView_trackStrokeHighlight)
                  strokePaint.setColor(array.getColor(attribute, Color.parseColor("#000fff")));

            else if(attribute == R.styleable.TipView_trackStrokeSize )
                 this.strokeSize = array.getDimensionPixelSize(attribute, 10);

            else if(attribute == R.styleable.TipView_tickmarkHighlight )
                  tickmarkPaint.setColor(array.getColor(attribute, Color.parseColor("#000fff")));

            else if(attribute == R.styleable.TipView_tickmarkWidth )
                this.tickmarkPaint.setStrokeWidth(array.getDimensionPixelSize(attribute, 10));

            else if(attribute == R.styleable.TipView_trackTextHighlight )
                  valueTextPaint.setColor(array.getColor(attribute, Color.parseColor("#000fff")));

            else if(attribute == R.styleable.TipView_trackTextSize )
                  valueTextPaint.setTextSize(array.getDimensionPixelSize(attribute, 12));
            }

        this.valueTextPaint.setTypeface(Typeface.createFromAsset(resources.getAssets(), TipView.FONT_SECONDARY));


    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        /**
         * The STROKE circle radius will be 1/2 the bounds width
         * and the track will be the stroke radius *minus* strokeWidth
         */
        final float strokeRadius = (getBounds().width()) *0.5f;
        final float trackRadius = (strokeRadius - strokeSize);

        final int cX = getBounds().centerX();
        final int cY = getBounds().centerY();

        canvas.drawCircle(cX, cY, strokeRadius, strokePaint);
        canvas.drawCircle(cX, cY, trackRadius, trackPaint);


        final float howFarStart = trackRadius;
        final float howFarEnd = strokeRadius;

        for(int i =0; i < 3; i++)
            canvas.drawLine(TipView.polarX(cX, howFarStart, tickmarks[i]),
                            TipView.polarY(cY, howFarStart, tickmarks[i]),
                            TipView.polarX(cX, howFarEnd, tickmarks[i]),
                            TipView.polarY(cY, howFarEnd, tickmarks[i]),
                            tickmarkPaint);


            final int valueTextX = cX - (valueBounds.width()/2);
            final int valueTextY = cY + (valueBounds.height()/2);

            canvas.drawText(valueText, valueTextX, valueTextY, valueTextPaint);



    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    void setValue(String value){
        this.valueText = value;
        setTextBounds();
    }

    Rect getTextBounds(){
        return valueBounds;
    }

    private void setTextBounds(){
        valueTextPaint.getTextBounds(valueText, 0 , valueText.length(), valueBounds);

    }

}
