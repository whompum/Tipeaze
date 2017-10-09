package com.example.bryan.tipeaze.Abstractions;

import com.example.bryan.tipeaze.BillingData.Bill;

/**
 * Created by bryan on 10/7/2017.
 */

public interface BillDataObserver {


    void onBillChanged(Bill bill);



}
