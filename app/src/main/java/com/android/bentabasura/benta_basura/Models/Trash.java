package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 10/1/2017.
 */

public class Trash {
    private String trashId;
    private String trashName;
    private String trashQuantity;
    private String trashPrice;
    private String trashDescription;
    private String trashCategory;
    private String meetupLocation;
    private String uploadedBy;
    private String uploadedDate;
    private String imageUrl;
    private String flag;
    private String flagTo;

    private long   reverseDate;

    public Trash()
    {

    }
    public Trash(String trashName,String trashQuantity,String trashPrice,String trashDescription,String trashCategory, String meetupLocation, String uploadedBy,String uploadedDate,String imageUrl, String flag, String flagTo, long reverseDate)
    {
        this.trashName = trashName;
        this.trashQuantity = trashQuantity;
        this.trashPrice = trashPrice;
        this.trashDescription = trashDescription;
        this.trashCategory = trashCategory;
        this.meetupLocation = meetupLocation;
        this.uploadedBy = uploadedBy;
        this.uploadedDate = uploadedDate;
        this.imageUrl = imageUrl;
        this.flag = flag;
        this.flagTo = flagTo;
        this.reverseDate = reverseDate;
    }


    public Trash(String trashName, String trashPrice, String trashDescription, String uploadedDate, String imageUrl) {
        this.trashName = trashName;
        this.trashPrice = trashPrice;
        this.trashDescription = trashDescription;
        this.uploadedDate = uploadedDate;
        this.imageUrl = imageUrl;
    }

    public String getTrashName() {
        return trashName;
    }

    public void setTrashName(String trashName) {
        this.trashName = trashName;
    }

    public String getTrashQuantity() {
        return trashQuantity;
    }

    public void setTrashQuantity(String trashQuantity) {
        this.trashQuantity = trashQuantity;
    }

    public String getTrashPrice() {
        return trashPrice;
    }

    public void setTrashPrice(String trashPrice) {
        this.trashPrice = trashPrice;
    }

    public String getTrashDescription() {
        return trashDescription;
    }

    public void setTrashDescription(String trashDescription) {
        this.trashDescription = trashDescription;
    }

    public String getTrashCategory() {
        return trashCategory;
    }

    public void setTrashCategory(String trashCategory) {
        this.trashCategory = trashCategory;
    }

    public String getmeetupLocation() {
        return meetupLocation;
    }

    public void setmeetupLocation(String meetupLocation) {
        this.meetupLocation = meetupLocation;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getflag() {
        return flag;
    }

    public void setflag(String flag) {
        this.flag = flag;
    }

    public String getTrashId() {
        return trashId;
    }

    public void setTrashId(String trashId) {
        this.trashId = trashId;
    }

    public String getflagTo() {
        return flagTo;
    }

    public void setflagTo(String flagTo) {
        this.flagTo = flagTo;
    }

    public long getReverseDate() {
        return reverseDate;
    }

    public void setReverseDate(long reverseDate) {
        this.reverseDate = reverseDate;
    }


}
