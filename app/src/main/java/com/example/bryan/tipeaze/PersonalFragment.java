package com.example.bryan.tipeaze;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bryan.tipeaze.Abstractions.OnUpdateListener;
import com.example.bryan.tipeaze.Abstractions.SplitChangeListener;
import com.example.bryan.tipeaze.BillingData.Bill;
import com.example.bryan.tipeaze.BillingData.BillDataCache;
import com.example.bryan.tipeaze.Abstractions.BillDataObserver;
import com.example.bryan.tipeaze.BillingData.CurrencyFormatter;
import com.example.bryan.tipeaze.CustomViews.RobotoBasedTextview;
import com.example.bryan.tipeaze.animations.AnimatorUtility;

/**
 * Created by bryan on 10/6/2017.
 */

public class PersonalFragment extends android.support.v4.app.Fragment implements SplitChangeListener, BillDataObserver {

    private static final int LAYOUT_DEF = R.layout.personal_layout;

    private static final int TOTAL_REF = R.string.total;

    public static final String TAG = "PersonalFragment";


    private RobotoBasedTextview productTotalTV;
    private RobotoBasedTextview grandTotalTV;

    private RobotoBasedTextview tipDisplayTV;
    private RobotoBasedTextview taxDisplayTV;

    private LinearLayout splitDisplayLayout;
    private RobotoBasedTextview splitTV;


    private BillDataCache cache;

    public PersonalFragment(){
        //wires this object to BillDataCache via "this" reference.
        cache = BillDataCache.getInstance(this);
    }

    public static PersonalFragment newInstance(@Nullable Bundle bundle){
        PersonalFragment frag = new PersonalFragment();
        frag.setArguments(bundle);

    return frag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View layout = inflater.inflate(LAYOUT_DEF, container, false); //Should be true?

        findViews(layout);
        //TODO getArguments()#Bundle to initialize states.

    return layout;
    }

    private void findViews(final View layout){
        productTotalTV = (RobotoBasedTextview) layout.findViewById(R.id.productTotal);
        grandTotalTV = (RobotoBasedTextview) layout.findViewById(R.id.grandTotal);
        tipDisplayTV = (RobotoBasedTextview) layout.findViewById(R.id.tipDisplay);
        taxDisplayTV = (RobotoBasedTextview) layout.findViewById(R.id.taxDisplay);
        splitDisplayLayout = (LinearLayout) layout.findViewById(R.id.splitDisplayLayout);
        splitTV = (RobotoBasedTextview) layout.findViewById(R.id.splitDisplay);

    }


    @Override
    public void changeSplit(int num) {
        changeSplitLayout(num);
    }

    private void changeSplitLayout(int num){
        //8 is max for split by default, so we only go up to 8.

        final int A = splitDisplayLayout.getChildCount() - 1; //Last usable index. Starting point for loop

        if(num <= 8){

            for(int i = 1; i <= num; i++)
                splitDisplayLayout.getChildAt(A-i).setVisibility(View.VISIBLE);

            for(int b = (A - num)-1; b >= 0; b--)
                splitDisplayLayout.getChildAt(b).setVisibility(View.INVISIBLE);

        }else{
            //do something special if user set max split to like 10, and we've surpassed 8.
            //I.E. split is 9 so make the eighth person icon glow or something fancy like that.
        }
        splitTV.setText(String.valueOf(num));
    }


    @Override
    public void onBillChanged(Bill bill) {
        /**
         * Called as a general "Something about the bill changed!" notification
         * It's up to this method to define rules for how to treat the bill.
         **/



        final long product = bill.getProduct();
        final long grand = bill.getGrand();

        productTotalTV.setText(toCash(product));
        grandTotalTV.setText(toCash(grand));

        tipDisplayTV.setText(toCash(bill.getTip()));
        taxDisplayTV.setText(toCash(bill.getTax()));


        if(product != grand & productTotalTV.getVisibility() == View.INVISIBLE)
            productTotalTV.setVisibility(View.VISIBLE);    // showProduct();
        else if(product == grand & productTotalTV.getVisibility() == View.VISIBLE)
            hideProduct();

    }

    private void showProduct(){
        /**
         * Animates from some starting value, to some ending value.
         * I need to animate, size, position, and color (last though)
         *
         *
         * VAL sV: The starting Y point for the translation
         * VAL eV: The ending Y-point for the translation
         *
         */

        final float sV = grandTotalTV.getY();
        final float eV = productTotalTV.getY();

        final int endingTextColor = productTotalTV.getCurrentTextColor();

        //should find a way to change this. I want to dynamically fetch the text size, not manually.
        final float startingTextSize = getResources().getDimension(R.dimen.textXXLarge);
        final float endingTextSize = getResources().getDimension(R.dimen.textLarge);

        final float sizeTrackLength = startingTextSize - endingTextSize;


        AnimatorUtility.track(sV, eV, 250L, null, new OnUpdateListener() {
            /**
             * @param value The converted value from sV to eV
             */
            @Override
            public void onUpdated(float value) {
                setY(productTotalTV, value);
            }

            @Override
            public void onFractionChanged(float fraction) {

                final float scale = sizeTrackLength * fraction;
                final float delta = startingTextSize - scale;
                setTextSize(productTotalTV, delta);
                Log.i(TAG, "onFractionChanged");
            }


        }, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                productTotalTV.setVisibility(View.VISIBLE);
                Log.i(TAG, "SETTING VIEW VISIBILITY");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setTextColor(productTotalTV, endingTextColor);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //reverse any changes
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //UnsupportedATM
            }
        });


    }

    private void hideProduct(){
        //animate product back into GrandTotal
    }

    private void setTextColor(TextView tv, int color){
        tv.setTextColor(color);
    }

    private void setTextSize(TextView tv, float textSize){
        //We set values in SP units, and the method itself does the conversions...
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    private void setY(TextView tv, float yPos){
        tv.setY(yPos);
    }

    private String toCash(long pennies){
        final double cash = CurrencyFormatter.getInstance().cashToPennies(pennies);
        return CurrencyFormatter.getInstance().format(cash);
    }


}






