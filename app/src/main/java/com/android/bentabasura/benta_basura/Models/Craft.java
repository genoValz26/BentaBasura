package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 10/1/2017.
 */

public class Craft {
    private String craftID,craftName,craftQuantity,craftPrice,craftDescription,craftCategory,sellerContact,uploadedBy,uploadedDate,resourcesFrom,imageUrl;
    private String sold;
    private String soldTo;
    private long   reverseDate;

    public Craft()
    {

    }
    public Craft(String craftName,String craftQuantity,String craftPrice,String craftDescription,String craftCategory, String sellerContact, String uploadedBy,String uploadedDate,String resourcesFrom,String imageUrl,String sold, String soldTo, Long reverseDate){
        this.craftName = craftName;
        this.craftQuantity = craftQuantity;
        this.craftPrice = craftPrice;
        this.craftDescription = craftDescription;
        this.craftCategory = craftCategory;
        this.sellerContact = sellerContact;
        this.uploadedBy = uploadedBy;
        this.uploadedDate = uploadedDate;
        this.resourcesFrom = resourcesFrom;
        this.imageUrl = imageUrl;
        this.sold = sold;
        this.soldTo = soldTo;
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

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
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

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getSoldTo() {
        return soldTo;
    }

    public void setSoldTo(String soldTo) {
        this.soldTo = soldTo;
    }

    public long getReverseDate() {
        return reverseDate;
    }

    public void setReverseDate(long reverseDate) {
        this.reverseDate = reverseDate;
    }
}
