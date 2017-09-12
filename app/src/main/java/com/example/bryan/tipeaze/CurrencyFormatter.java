package com.example.bryan.tipeaze;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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


    public String cleanText(String text){
        return text.replaceAll("[^\\d]", "");
    }

    public double cookText(CharSequence text){
        return cookText((String)text);
    }

    public double cookText(String text){
        //parses text into a double, then shifts the values right by *defaultFractionDigits*
        return (Double.parseDouble(text)) / Math.pow(10, defaultFractionDigits);
    }













}
