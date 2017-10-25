package com.android.bentabasura.benta_basura.Models;

/**
 * Created by reymond on 25/10/2017.
 */

public class Transaction_Craft {
    private String sellerId, buyerId, craftId, craftQty, craftAmount,transation_Date;
    public Transaction_Craft(String sellerId, String buyerId, String craftId,String craftQty,String transaction_Date){
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.craftId = craftId;
        this.craftQty = craftQty;
        this.transation_Date = transaction_Date;

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCraftId() {
        return craftId;
    }

    public void setCraftId(String craftId) {
        this.craftId = craftId;
    }

    public String getCraftQty() {
        return craftQty;
    }

    public void setCraftQty(String craftQty) {
        this.craftQty = craftQty;
    }

    public String getTransation_Date() {
        return transation_Date;
    }

    public void setTransation_Date(String transation_Date) {
        this.transation_Date = transation_Date;
    }
}
