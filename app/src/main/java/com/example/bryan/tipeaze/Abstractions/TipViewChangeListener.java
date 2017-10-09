package com.example.bryan.tipeaze.Abstractions;

/**
 * Created by bryan on 10/3/2017.
 */

public interface TipViewChangeListener {
    void onTipChange(int tip);

    void onTaxChange(int tax);

    void onSplitChange(int split);
}
