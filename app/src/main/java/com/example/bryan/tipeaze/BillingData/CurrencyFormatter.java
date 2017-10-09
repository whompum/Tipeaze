package com.example.bryan.tipeaze.BillingData;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.Locale;

//TODO register listeners to be notified whenever the locale changes or something similar happens

public class CurrencyFormatter {


    private static CurrencyFormatter instance;

    private DecimalFormat formatter;

    private Locale locale = Locale.getDefault();

    private int defaultFractionDigits = 0;

    private CurrencyFormatter() {
        if (locale == null)
            locale = Locale.US;

        formatter = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);

        defaultFractionDigits = formatter.getCurrency().getDefaultFractionDigits();



    }

    static{
        instance = new CurrencyFormatter();
    }


    public static CurrencyFormatter getInstance(/* Any init parameters needed? */){
        return instance;
    }

    public String format(String text){
        return formatter.format(text);
    }

    public String format(double text){
        return formatter.format(text);
    }

    public String getCurrencySymbol(){
        return formatter.getCurrency().getSymbol();
    }

    private String cleanText(String text){

        final String newText = text.replaceAll("[^\\d]", "");

        return newText;
    }

    public double penniesToCash(String dirtyPennies){

        final long cleanPennies = Long.valueOf(cleanText(dirtyPennies));
        final double cash = (cleanPennies / getFractionDivisor());

    return cash;
    }



    public String cashToPennies(String pennies){
        return cleanText(pennies);
    }

    private double getFractionDivisor(){
        return Math.pow(10, defaultFractionDigits);
    }


    public double cashToPennies(long pennies){
        return pennies / getFractionDivisor();
    }








}
