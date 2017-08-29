package com.example.bryan.tipeaze.Presets;

/**
 * Created by bryan on 8/15/2017.
 */

public class Presets {

    public static final String DEFAULT_NAME = "'Custom'";

    private int currTip;
    private int currTax;
    private int currSplit;

    private int maxTip;
    private int maxTax;
    private int maxSplit;

    private Presets(){
        currTip = currTax = currSplit = 0;
        maxTip = maxTax = 30;
        maxSplit = 8;
    }


    public int getCurrTip(){ return  currTip; }
    public int getCurrTax(){ return  currTax; }
    public int getCurrSplit(){ return currSplit; }
    public int getMaxTip(){ return maxTip; }
    public int getMaxTax(){ return maxTax; }
    public int getMaxSplit(){ return maxSplit; }

    public static class Builder{
        private Presets presets;

        public Builder(){
            presets = new Presets();
        }

        public Builder setCurrTip(int currTip){
            presets.currSplit = currTip;
            return this;
        }
        public Builder setCurrTax(int currTax){
            presets.currTax = currTax;
        return  this;
        }

        public Builder setCurrSplit(int currSplit){
            presets.currSplit = currSplit;
            return this;
        }

        public Builder setMaxTip(int maxTip){
            presets.maxTip = maxTip;
            return this;
        }

        public Builder setMaxTax(int maxTax){
            presets.maxTax = maxTax;
            return this;
        }

        public Builder setMaxSplit(int maxSplit){
            presets.maxSplit = maxSplit;
            return this;
        }

        public Presets apply(){
            return presets;
        }


    }


}
