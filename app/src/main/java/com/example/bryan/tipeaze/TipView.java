package com.example.bryan.tipeaze;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;



import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * TODO add logic to change thumb boundaries if this View is given a preset: Animate thumbs to positions :)
 * TODO Switch from custom state setting to using Drawable states and framework methods
 * TODO write for accessibility as well : DO July-25th/26th-2017
 */


public class TipView extends View {


    enum TYPE{  TIP,  TAX,  SPLIT  }

    //android chooses first matching state first, so order from least common to most common;
    enum TOUCHSTATES{  FOCUSED,  DEFAULT  }

    static final float tipTickMarkRad = (float)(Math.PI*3)/2; //270 Degree
    static final float taxTickMarkRad = tipTickMarkRad - ((float)(Math.PI*2)/3);  //ONE-THIRD of 2PI
    static final float splitTickMarkRad = taxTickMarkRad - ((float)(Math.PI*2)/3); //ONE-THIRD of 2PI
    private static final float THUMB_GUTTER = 0.126f; // thumb offset from the tick marks;

    static final String FONT_PRIMARY = "Roboto-Regular.ttf";
    static final String FONT_SECONDARY = "Roboto-Bold.ttf";



    private int totalTrackSize; //Entire size of view. Should be the actual trackSize dimension value set in XML. Includes the stroke.
    private int trackStrokeSize; //stroke size of the track. totalTrackSize "minus" this value, will give the track size housing the text field.
    private int thumbSize;

    private Rect paddingBounds = new Rect();

    private Track track;
    private Set<Thumb> thumbs = new HashSet<>(3);


    private int tipMax = 31;  //percent - Cieling of tip. E.G. tip thumb @ cieling would be 30.
    private int taxMax = 31;  //percent
    private int splitMax = 9; //whole number


    private int currTip = 0;
    private int currTax = 0;
    private int currSplit = 0;


    private Thumb thumbToUpdate;

    private float lastTouchTheta;

    private Rect graceTouchHitBox = new Rect(); //the hitbox the users finger must be in to continue dragging;

    private int graceTouchRadius = 0;

    Vibrator vibrator;

    public TipView(Context context, AttributeSet set){
        super(context, set);
        TypedArray array = context.obtainStyledAttributes(set, R.styleable.TipView, 0, R.style.TipViewDefStyle);

        for(int i = 0; i < array.getIndexCount(); i++){
            final int attribute = array.getIndex(i);

            if(attribute == R.styleable.TipView_totalTrackSize)
                this.totalTrackSize = array.getDimensionPixelSize(attribute, 0);

            else if(attribute == R.styleable.TipView_trackStrokeSize)
                this.trackStrokeSize = array.getDimensionPixelSize(attribute, 0);

            else if(attribute == R.styleable.TipView_thumbSize)
                this.thumbSize = array.getDimensionPixelSize(attribute, 0);
        }

        thumbs.add(new Thumb(array, TYPE.TIP, "Tip", getResources()));
        thumbs.add(new Thumb(array, TYPE.TAX, "Tax", getResources()));
        thumbs.add(new Thumb(array, TYPE.SPLIT, "Split", getResources()));

        track = new Track(array, getResources());

        array.recycle();

        graceTouchRadius = getResources().getDimensionPixelSize(R.dimen.graceTouchHitbox);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width;
        int height;

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //should i also fetch padding start/end?
        final int paddingHor = getPaddingLeft() + getPaddingRight();
        final int paddingVer = getPaddingTop() + getPaddingBottom();


        if(widthMode == MeasureSpec.EXACTLY)
            width = MeasureSpec.getSize(widthMeasureSpec);
        else
            width = totalTrackSize + thumbSize + paddingHor;


        if(heightMode == MeasureSpec.EXACTLY)
            height = MeasureSpec.getSize(heightMeasureSpec);
        else
            height = totalTrackSize + thumbSize + paddingVer;


        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //sets a new positioning Rect to be used based on the defined padding
        paddingBounds.set(getPaddingLeft(),
                          getPaddingTop(),
                          w - getPaddingRight(),
                          h - getPaddingBottom());

        track.setBounds(paddingBounds.centerX() - totalTrackSize/2,
                        paddingBounds.centerY() - totalTrackSize/2,
                        paddingBounds.centerX() + totalTrackSize/2,
                        paddingBounds.centerY() + totalTrackSize/2);


        initThumbPos();


        final Iterator<Thumb> thumbIterator = thumbs.iterator();

        while(thumbIterator.hasNext())
            initThumbBounds(thumbIterator.next());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        track.draw(canvas);

        final Iterator<Thumb> thumbIterator = thumbs.iterator();

        while(thumbIterator.hasNext()){
            thumbIterator.next().draw(canvas);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
              return onTouchDown(event);

        else if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
            return onMove(event);

        else if(event.getActionMasked() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_CANCEL)
                    onUpCancel(event);


        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private void setThumbBounds(Thumb thumb, float angle){
        /**
         * Starting at the center of paddingBounds:
         * Go out drawLengthPos far:
         * At an defined angle  ;
         * Now adjust +/- THUMB_RADIUS to find left/top/right/bottom  (thumbRadius);
         */

        final int thumbCenterX = (int)(polarX( paddingBounds.centerX(),
                                             (totalTrackSize*0.5f) - (trackStrokeSize/2), angle));

        final int thumbCenterY = (int)(polarY( paddingBounds.centerY(),
                                             (totalTrackSize*0.5f) - (trackStrokeSize/2), angle));

        thumb.setBounds(thumbCenterX - thumbSize/2,
                        thumbCenterY - thumbSize/2,
                        thumbCenterX + thumbSize/2,
                        thumbCenterY + thumbSize/2);

        thumb.setCurrTheta(angle);
    }


    private void initThumbPos(){

        //thumbOffset has the same purpose as thumbGutter; Though it adjust positioning by half of thumbs width
        final float thumbOffset = ((float)Math.atan2( thumbSize*0.5f,
                (totalTrackSize-trackStrokeSize)*0.5f + trackStrokeSize*0.5f))+THUMB_GUTTER;


        Iterator<Thumb> thumbIterator = thumbs.iterator();
            while(thumbIterator.hasNext()){

                final Thumb thumb = thumbIterator.next();

                float tickmarkFloor = 0f;
                float tickmarkCieling = 0f;

                switch(getThumbType(thumb)){

                    case TIP:
                        tickmarkFloor = tipTickMarkRad;
                        tickmarkCieling = splitTickMarkRad;
                        break;

                    case TAX:
                        tickmarkFloor = taxTickMarkRad;
                        tickmarkCieling = tipTickMarkRad;
                        break;

                    case SPLIT:
                        tickmarkFloor = splitTickMarkRad;
                        tickmarkCieling = taxTickMarkRad;
                        break;

                }

                thumb.setFloorAndCiel(tickmarkFloor + thumbOffset,
                        tickmarkCieling - thumbOffset );
            }

    }


    private boolean onTouchDown(MotionEvent event){

        if( thumbToUpdate != null ) { //@ this point we don't care where the touch actually was, we just want this behavior.
            changeThumbState(TOUCHSTATES.DEFAULT);
            //invalidate(thumbToUpdate.getBounds());

            setTrackText("");
            invalidate(track.getTextBounds());
            //Should probably check if certain conditions are meet, instead of always running this code. Works either way
            //but for a completely unnoticeable performance boost, this is our solution.
            //In fact, to all those reading this just know it took me more time to write this comment than it would've to
            //fix the solution.
        }

        if( (thumbToUpdate = getThumbForHit((int)event.getX(), (int)event.getY())) != null ){

            changeThumbState(TOUCHSTATES.FOCUSED);

            setTrackText(getThumbValueReal(thumbToUpdate));

            invalidate(thumbToUpdate.getBounds());
            invalidate(track.getTextBounds());

            this.lastTouchTheta = getTouchTheta(event.getX(), event.getY());

            setGraceTouchHitBox(thumbToUpdate.getBounds());

            return true;
        }

        return false;
    }


    private boolean onMove(MotionEvent event){

        float currentTouchTheta = getTouchTheta(event.getX(), event.getY());

        if(!isDragOutsideOurGrace((int)event.getX(), (int)event.getY()))
            return false;


        if(shouldScaleTouchTheta(currentTouchTheta)) //is the thumbs intrinsic cieling less than its intrinsic floor?
            currentTouchTheta = scaleUpTouchTheta(currentTouchTheta);

        float newThumbTheta = thumbToUpdate.getCurrTheta();

        if(currentTouchTheta > lastTouchTheta) //if currTheta is greater, than we add the differences to the thumbs curr theta
            newThumbTheta += (currentTouchTheta - lastTouchTheta);

        else if(lastTouchTheta > currentTouchTheta) //if currTheta is less than, then we subract  the difference to the thumbs curr theta
            newThumbTheta -= (lastTouchTheta - currentTouchTheta);


        this.lastTouchTheta = currentTouchTheta;


        if(!canChangeThumbTheta(newThumbTheta)) { //Asking: Has the thumb reached its cieling or floor yet?
            vibrate(15);
            return false;
        }


        thumbToUpdate.setCurrTheta(newThumbTheta);

        setThumbBounds(thumbToUpdate, newThumbTheta);
        invalidate(thumbToUpdate.getBounds());

        setGraceTouchHitBox(thumbToUpdate.getBounds()); //update our graceHitBox to match our thumbs new bounds

        setTrackText(getThumbValueReal(thumbToUpdate));
        invalidate(track.getTextBounds());

        setCurrValue(thumbToUpdate, getThumbValueReal(thumbToUpdate));


        return true;
    }

    private void
    onUpCancel(MotionEvent event){
        //do something...
    }

    private @Nullable Thumb getThumbForHit(final int hitX, final int hitY) {

        final Iterator<Thumb> thumbIterator = thumbs.iterator();

        while(thumbIterator.hasNext()) {

            final Thumb thumb = thumbIterator.next();

            if (thumb.getBounds().contains(hitX, hitY))
                return thumb;
        }

        return null;
    }


    private void vibrate(long dur){
        if(vibrator!=null)
            if(vibrator.hasVibrator())
                vibrator.vibrate(dur);
    }

   /**
    * @return whether or not the thumb can move anymore
    */
    private boolean canChangeThumbTheta(float newTheta){

        if(newTheta >= thumbToUpdate.getCiel() || newTheta <= thumbToUpdate.getFloor())
            return false;

    return true;
    }

    private void setGraceTouchHitBox(Rect rect){

        graceTouchHitBox.left = rect.left - graceTouchRadius;
        graceTouchHitBox.right = rect.right + graceTouchRadius;
        graceTouchHitBox.top = rect.top - graceTouchRadius;
        graceTouchHitBox.bottom = rect.bottom + graceTouchRadius;
    }

    private boolean isDragOutsideOurGrace(final int x, final int y){
        return (graceTouchHitBox.contains(x, y));
    }


    /**
     *
     * @param touchTheta the current radian value of the most recent touch
     * @return whether or not the thumbs cieling lie past 2PI while its floor doesn't
     */
    private boolean shouldScaleTouchTheta(float touchTheta){

        final float scaleThumbCieling = scaleDownTouchTheta(thumbToUpdate.getCiel());

        /**
         * In the thumb class, a cieling value is automatically scaled upwards by 2PI IF
         * it's less than the floor. Hence the reason we scale downwards to get the real value
         */
        if(thumbToUpdate.getFloor() > scaleThumbCieling && touchTheta >= 0 && touchTheta <= scaleThumbCieling )
                return true;

    return false;
    }

    /**
     *
     * @param touchTheta theta value to scale
     * @return a radian value scaled upwards by 2PI
     */
    private float scaleUpTouchTheta(float touchTheta){
        return touchTheta + (float)(Math.PI*2);
    }

    private float scaleDownTouchTheta(float touchTheta){
        return touchTheta - (float)(Math.PI*2);
    }

    private float getTouchTheta(float x, float y){
        x -= paddingBounds.centerX(); //forgot why this is necessary, but it is.
        y -= paddingBounds.centerY();

        float newTheta = (float)Math.atan2(y, x);
        //Scale theta up by 2PI since math.atan2(float, float) returns a value between -PI / PI
        if(newTheta <= 0) newTheta = scaleUpTouchTheta(newTheta);


        return newTheta;
    }

    private int getThumbValueReal(Thumb thumb){

        float currThumbValue = 0f;

        switch(getThumbType(thumbToUpdate)){

            case TIP:
                currThumbValue = tipMax* getThumbValuePercentage(thumb);
                break;
            case TAX:
                currThumbValue = taxMax* getThumbValuePercentage(thumb);
                break;
            case SPLIT:
                currThumbValue = splitMax* getThumbValuePercentage(thumb);
                break;
        }

        return (int)currThumbValue;
    }

    private void setTrackText(int value){

        String text = String.valueOf(value);

        switch(getThumbType(thumbToUpdate)){
            case TIP:
            case TAX: text += "%";
        }


        this.setTrackText(text);
    }

    private void setTrackText(String string){
        track.setValue(string);
    }

    private void setCurrValue(Thumb thumb, int currValue){

        switch (getThumbType(thumb)){
            case TIP: this.currTip = currValue;
                break;
            case TAX: this.currTax = currValue;
                break;
            case SPLIT: this.currSplit = currValue;
                break;
        }

    }

    private float getThumbValuePercentage(Thumb thumb){
        return (thumb.getValue()) / 100;
    }

    private void changeThumbState(TipView.TOUCHSTATES state){
        thumbToUpdate.setTouchState(state);
    }

    private TYPE getThumbType(Thumb thumb){
        return thumb.getType();
    }

    private void initThumbBounds(Thumb thumb){
        setThumbBounds(thumb, thumb.getFloor());
    }

    public void setTipMaxValue(int tipMax){
        //add one to these to account for the fact that the thumb values are 0-based
        tipMax+=1;
        this.setMaxValues(tipMax, this.taxMax, this.splitMax);
    }

    public void setTaxMaxValue(int taxMax){
        taxMax+=1;
        this.setMaxValues(this.tipMax, taxMax, this.splitMax);
    }

    public void setSplitMaxValue(int splitMax){
        splitMax+=1;
        this.setMaxValues(this.tipMax, this.taxMax, splitMax);
    }

    public void setMaxValues(int tipMax, int taxMax, int splitMax){
        this.tipMax = tipMax;
        this.taxMax = taxMax;
        this.splitMax = splitMax;
    }

    public void setCurrTip(int currTip){
        this.currTip = currTip;
    }

    public void setCurrTax(int currTax){
        this.currTax = currTax;
    }

    public void setCurrSplit(int currSplit){
        this.currSplit = currSplit;
    }

    public static float polarX(float start, float howFar, float direction){
        return (start + howFar * (float)Math.cos(direction));
    }

    public static float polarY(float start, float howFar, float direction) {
        return (start + howFar * (float) Math.sin(direction));
    }

}
