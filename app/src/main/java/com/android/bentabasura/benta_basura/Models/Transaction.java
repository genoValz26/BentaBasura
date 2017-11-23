package com.android.bentabasura.benta_basura.Models;

/**
 * Created by reymond on 25/10/2017.
 */

public class Transaction {
    private String soldTo, soldQty,sellerId,transaction_Date, itemId;

    public Transaction(String soldTo, String sellerId, String transaction_Date, String itemId) {
        this.soldTo = soldTo;
        this.sellerId = sellerId;
        this.transaction_Date = transaction_Date;
        this.itemId = itemId;
    }

    public void setItemId(String itemId) {

        this.itemId = itemId;
    }
    public String getItemId() {
        return itemId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTransaction_Date() {
        return transaction_Date;
    }

    public void setTransaction_Date(String transaction_Date) {
        this.transaction_Date = transaction_Date;
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

}
