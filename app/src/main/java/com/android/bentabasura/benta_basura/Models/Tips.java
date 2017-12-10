package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 12/10/2017.
 */

public class Tips {
    String tipsTitle,tipsDetail,tipsDate,tipsImagepath;

    public Tips(String tipsTitle, String tipsDetail, String tipsDate, String tipsImagepath) {
        this.tipsTitle = tipsTitle;
        this.tipsDetail = tipsDetail;
        this.tipsDate = tipsDate;
        this.tipsImagepath = tipsImagepath;
    }

    public String getTipsTitle() {
        return tipsTitle;
    }

    public void setTipsTitle(String tipsTitle) {
        this.tipsTitle = tipsTitle;
    }

    public String getTipsDetail() {
        return tipsDetail;
    }

    public void setTipsDetail(String tipsDetail) {
        this.tipsDetail = tipsDetail;
    }

    public String getTipsDate() {
        return tipsDate;
    }

    public void setTipsDate(String tipsDate) {
        this.tipsDate = tipsDate;
    }

    public String getTipsImagepath() {
        return tipsImagepath;
    }

    public void setTipsImagepath(String tipsImagepath) {
        this.tipsImagepath = tipsImagepath;
    }
}
