package com.example.bryan.tipeaze.BillingData;

/**
 * Created by bryan on 10/7/2017.
 */

public class Bill {

    private long product, grand, tip, tax, grandSplit, tipSplit, taxSplit;


    public Bill(){
        init();
    }

    private void init(){
        product = grand = tip = tax = 0L;
        grandSplit = tipSplit = taxSplit = 0L;
    }



    public long getGrand() {
        return grand;
    }

    public void setGrand(long grand) {
        this.grand = grand;
    }


    public long getGrandSplit() {
        return grandSplit;
    }

    public void setGrandSplit(long grandSplit) {
        this.grandSplit = grandSplit;
    }

    public long getProduct() {
        return product;
    }

    public void setProduct(long product) {
        this.product = product;
    }

    public long getTax() {
        return tax;
    }

    public void setTax(long tax) {
        this.tax = tax;
    }

    public long getTaxSplit() {
        return taxSplit;
    }

    public void setTaxSplit(long taxSplit) {
        this.taxSplit = taxSplit;
    }

    public long getTip() {
        return tip;
    }

    public void setTip(long tip) {
        this.tip = tip;
    }


    public long getTipSplit() {
        return tipSplit;
    }

    public void setTipSplit(long tipSplit) {
        this.tipSplit = tipSplit;
    }

    public void clear(){
        init();
    }



}
