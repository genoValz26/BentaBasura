package com.android.bentabasura.benta_basura;

/**
 * Created by Geno on 10/1/2017.
 */

public class Craft {
    private String craftName,craftQuantity,craftPrice,craftDescription,craftCategory,sellerContact,uploadedBy,uploadedDate,resourcesFrom,imageUrl;
    public Craft()
    {

    }
    public Craft(String craftName,String craftQuantity,String craftPrice,String craftDescription,String craftCategory, String sellerContact, String uploadedBy,String uploadedDate,String resourcesFrom,String imageUrl){
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
}
