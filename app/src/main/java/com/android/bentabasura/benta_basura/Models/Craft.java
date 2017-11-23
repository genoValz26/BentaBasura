package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 10/1/2017.
 */

public class Craft {
    private String craftID,craftName,craftQuantity,craftPrice,craftDescription,craftCategory,meetupLocation,uploadedBy,uploadedDate,resourcesFrom,imageUrl;
    private String flag;
    private String flagTo;
    private long   reverseDate;

    public Craft()
    {

    }
    public Craft(String craftName,String craftQuantity,String craftPrice,String craftDescription,String craftCategory, String meetupLocation, String uploadedBy,String uploadedDate,String resourcesFrom,String imageUrl,String flag, String flagTo, Long reverseDate){
        this.craftName = craftName;
        this.craftQuantity = craftQuantity;
        this.craftPrice = craftPrice;
        this.craftDescription = craftDescription;
        this.craftCategory = craftCategory;
        this.meetupLocation = meetupLocation;
        this.uploadedBy = uploadedBy;
        this.uploadedDate = uploadedDate;
        this.resourcesFrom = resourcesFrom;
        this.imageUrl = imageUrl;
        this.flag = flag;
        this.flagTo = flagTo;
        this.reverseDate = reverseDate;
    }

    public String getCraftName() {
        return craftName;
    }

    public void setCraftName(String craftName) {
        this.craftName = craftName;
    }

    public String getCraftQuantity() {
        return craftQuantity;
    }

    public void setCraftQuantity(String craftQuantity) {
        this.craftQuantity = craftQuantity;
    }

    public String getCraftPrice() {
        return craftPrice;
    }

    public void setCraftPrice(String craftPrice) {
        this.craftPrice = craftPrice;
    }

    public String getCraftDescription() {
        return craftDescription;
    }

    public void setCraftDescription(String craftDescription) {
        this.craftDescription = craftDescription;
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

    public String getCraftCategory() {
        return craftCategory;
    }

    public void setCraftCategory(String craftCategory) {
        this.craftCategory = craftCategory;
    }

    public String getmeetupLocation() {
        return meetupLocation;
    }

    public void setmeetupLocation(String meetupLocation) {
        this.meetupLocation = meetupLocation;
    }

    public String getResourcesFrom() {
        return resourcesFrom;
    }

    public void setResourcesFrom(String resourcesFrom) {
        this.resourcesFrom = resourcesFrom;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCraftID() {
        return craftID;
    }

    public void setCraftID(String craftID) {
        this.craftID = craftID;
    }

    public String getflag() {
        return flag;
    }

    public void setflag(String flag) {
        this.flag = flag;
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
