package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 11/18/2017.
 */

public class Reservation {
    private String reservedTo, reservedQty, reservedDate;

    public Reservation(String reservedTo, String reservedQty, String reservedDate) {
        this.reservedTo = reservedTo;
        this.reservedQty = reservedQty;
        this.reservedDate = reservedDate;
    }

    public String getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(String reservedTo) {
        this.reservedTo = reservedTo;
    }

    public String getReservedQty() {
        return reservedQty;
    }

    public void setReservedQty(String reservedQty) {
        this.reservedQty = reservedQty;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }
}
