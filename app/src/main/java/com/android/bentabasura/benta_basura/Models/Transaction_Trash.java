package com.android.bentabasura.benta_basura.Models;

/**
 * Created by reymond on 25/10/2017.
 */

public class Transaction_Trash {
    private String sellerId, buyerId, trashId, trashQty,transation_Date;
    public Transaction_Trash(String sellerId, String buyerId, String trashId,String trashQty,String transaction_Date){
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.trashId = trashId;
        this.trashQty = trashQty;
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

    public String getTrashId() {
        return trashId;
    }

    public void setTrashId(String trashId) {
        this.trashId = trashId;
    }

    public String getTrashQty() {
        return trashQty;
    }

    public void setTrashQty(String trashQty) {
        this.trashQty = trashQty;
    }

    public String getTransation_Date() {
        return transation_Date;
    }

    public void setTransation_Date(String transation_Date) {
        this.transation_Date = transation_Date;
    }
}
