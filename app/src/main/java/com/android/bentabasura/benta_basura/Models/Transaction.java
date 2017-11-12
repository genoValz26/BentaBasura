package com.android.bentabasura.benta_basura.Models;

/**
 * Created by reymond on 25/10/2017.
 */

public class Transaction {
    private String soldTo, soldQty, craftAmount,transation_Date;
    public Transaction(String soldTo, String soldQty, String transaction_Date){
        this.soldTo =  soldTo;
        this.soldQty = soldQty;
        this.transation_Date = transaction_Date;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public String getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(String soldQty) {
        this.soldQty = soldQty;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }
    public String getTransation_Date() {
        return transation_Date;
    }

    public void setTransation_Date(String transation_Date) {
        this.transation_Date = transation_Date;
    }
}
