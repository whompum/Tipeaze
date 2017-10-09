package com.example.bryan.tipeaze.animations;

import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.example.bryan.tipeaze.Abstractions.OnUpdateListener;

/**
 * Created by bryan on 10/5/2017.
 */

public class AnimatorUtility {


    /**
     *
      * @param sV starting value of the engine. E.G. 100
     * @param eV Ending value of the timing engine. E.G. 10
     * @param dur How long to run the timing engine for
     * @param interpolator R.O.C for the timing Engine
     * @param listner  Receiver of value/fraction changes
     * @param stateListener Listeners to the timing state (started / paused / cancel / ended )
     */


    /**
     * Track is a timing engine that computes values between sV, and eV
     * and returns a value for them
     */
   public static void track(final float sV,
                        final float eV,
                        long dur,
                        @Nullable Interpolator interpolator,
                        @Nullable final OnUpdateListener listener,
                        @Nullable final android.animation.Animator.AnimatorListener stateListener){


        ValueAnimator animator = new ValueAnimator();


            if(interpolator == null)
                  interpolator = new DecelerateInterpolator();
            if(dur == 0L)
                  dur = 250L;
            if(stateListener!=null)
                  animator.addListener(stateListener);


        animator.setFloatValues(0f, 1f);
        animator.setDuration(dur);
        animator.setInterpolator(interpolator);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float fraction = valueAnimator.getAnimatedFraction();

                if(fraction <= 1.0f){

                    if(listener != null)
                    listener.onFractionChanged(fraction);

                    if(listener != null) {
                        final float delta = computeTrackLength(sV, eV) * fraction;
                        listener.onUpdated(sV - delta);
                    }

                }
            }
        });

        animator.start();
    }

    private static int computeTrackLength(float sV, float eV){
        return (int)((sV > eV) ? sV - eV : eV - sV);
    }



}
