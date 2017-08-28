package com.example.bryan.tipeaze.Presets;

/**
 * Created by bryan on 8/15/2017.
 */

public class Presets {

    public static final String DEFAULT_NAME = "'Custom'";

    private int currTip = 0;
    private int currTax = 0;
    private int currSplit = 0;

    private int maxTip = 30;
    private int maxTax = 30;
    private int maxSplit = 8;

    public Presets(){

    }


    public int getCurrTip(){ return  currTip; }
    public int getCurrTax(){ return  currTax; }
    public int getCurrSplit(){ return currSplit; }
    public int getMaxTip(){ return maxTip; }
    public int getMaxTax(){ return maxTax; }
    public int getMaxSplit(){ return maxSplit; }





    public String getDefaultName(){
        return DEFAULT_NAME;
    }


}
