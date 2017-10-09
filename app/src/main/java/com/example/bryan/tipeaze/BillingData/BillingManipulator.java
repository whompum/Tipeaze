package com.example.bryan.tipeaze.BillingData;


import android.util.Log;

public class BillingManipulator {

    public static final String TAG = "BillingManipulator";

    private long productTotal, grandTotal, tipTotal, taxTotal = 0L;

    private long grandSplit, tipSplit, taxSplit = 0L;

    private BillDataCache billDataCache;

    public BillingManipulator(){
        billDataCache = BillDataCache.getInstance(null);
    }


    public void resetWithBillTotal(long pennies){

        productTotal = grandTotal = pennies;

        reset(false);
        billDataCache.onProductTotalChange(pennies, false);
        updateGrandTotal(true);

        Log.i("PATH", "resetWithBillTotal");

    }

    public void changeTip(int tip, boolean notify){
        tipTotal = (long)(productTotal * getPercentage(tip));

        billDataCache.onTipTotalChange(tipTotal, notify);

        updateGrandTotal(notify);
    }

    public void changeTax(int tax, boolean notify){
        taxTotal = (long)(productTotal * getPercentage(tax));

        billDataCache.onTaxTotalChange(taxTotal, notify);

        updateGrandTotal(notify);
    }

    public void setSplit(int split, boolean notify){
        Log.i(TAG, String.valueOf(grandTotal));
        grandSplit = grandTotal / split;
        Log.i(TAG, String.valueOf(grandTotal));

        tipSplit = tipTotal / split;
        taxSplit = taxTotal / split;

        billDataCache.onGrandSplitChange(grandSplit, notify);
        billDataCache.onTipSplitChange(tipSplit,notify);
        billDataCache.onTaxSplitChange(taxSplit, notify);

    }


    private float getPercentage(int num){
        return num / 100.0f;
    }

    private void updateGrandTotal(boolean notify){
        grandTotal = productTotal + tipTotal + taxTotal;

        Log.i("PATH", "updateGrandTotal");
        billDataCache.onGrandTotalChange(grandTotal, notify);
    }

    private void reset(boolean notify){
        Log.i("PATH", "reset");
        tipTotal = taxTotal = 0L;
        grandSplit = grandTotal;
        tipSplit = taxSplit = 0L;
        billDataCache.clearData(notify);
    }



}
