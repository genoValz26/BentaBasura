package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 12/10/2017.
 */

public class News {
    String newsID,newsTitle,newsDetail,newsDate,newsImagepath;

    public News(){

    }
    public News(String newsTitle, String newsDetail, String newsDate, String newsImagepath) {
        this.newsTitle = newsTitle;
        this.newsDetail = newsDetail;
        this.newsDate = newsDate;
        this.newsImagepath = newsImagepath;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsImagepath() {
        return newsImagepath;
    }

    public void setNewsImagepath(String newsImagepath) {
        this.newsImagepath = newsImagepath;
    }
}
