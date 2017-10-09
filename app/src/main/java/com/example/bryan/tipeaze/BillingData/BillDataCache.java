package com.example.bryan.tipeaze.BillingData;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bryan.tipeaze.Abstractions.BillDataObserver;

import java.util.ArrayList;

/**
 * Created by bryan on 10/7/2017.
 */

public class BillDataCache {

    private static BillDataCache instance;

    private ArrayList<BillDataObserver> observers; //An array list so i don't have to fetch iterators constantly.

    private Bill bill;

    private BillDataCache(){
        observers = new ArrayList<>();
        bill = new Bill();
    }



    public static BillDataCache getInstance(@Nullable BillDataObserver observer){

        if(instance == null){
            instance = new BillDataCache();
        }

        instance.registerObserver(observer);

        return instance;
    }

    public boolean registerObserver(BillDataObserver observer){
        if(observer == null)
            return false;

    return observers.add(observer);
    }

    public boolean removeObserver(BillDataObserver observer){
        if(observer!=null)
            return false;

    return observers.remove(observer);
    }






    void onProductTotalChange(long pennies, boolean notify){

        bill.setProduct(pennies);

        Log.i("PATH", "onProductTotalChange");

        if(notify)
        notifyObservers();

    }


    void onGrandTotalChange(long pennies, boolean notify){

        bill.setGrand(pennies);

        Log.i("PATH", "onGrandTotalChange");

        if(notify)
            notifyObservers();

    }
    void onTipTotalChange(long pennies, boolean notify){

        bill.setTip(pennies);

        if(notify)
            notifyObservers();
    }
    void onTaxTotalChange(long pennies, boolean notify){

        bill.setTax(pennies);

        if(notify)
            notifyObservers();
    }

    void onGrandSplitChange(long pennies, boolean notify){

        bill.setGrandSplit(pennies);

        if(notify)
            notifyObservers();
    }
    void onTipSplitChange(long pennies, boolean notify){

        bill.setTipSplit(pennies);

        if(notify)
            notifyObservers();
    }
    void onTaxSplitChange(long pennies, boolean notify){

        bill.setTaxSplit(pennies);

        if(notify)
            notifyObservers();
    }

    void clearData(boolean notify) {
        bill.clear();

        Log.i("PATH", "clearData");

        if(notify)
            notifyObservers();
    }


    private void notifyObservers(){

        for(int i =0; i < observers.size(); i++){
            observers.get(i).onBillChanged(bill);
        }

    }









}
