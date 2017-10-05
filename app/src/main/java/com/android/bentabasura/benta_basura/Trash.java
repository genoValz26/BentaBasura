package com.android.bentabasura.benta_basura;

/**
 * Created by Geno on 10/1/2017.
 */

public class Trash {
    private String trashName,trashQuantity,trashPrice,trashDescription,trashCategory,sellerContact,uploadedBy,uploadedDate,imageUrl;
    public Trash()
    {

    }
    public Trash(String trashName,String trashQuantity,String trashPrice,String trashDescription,String trashCategory, String sellerContact, String uploadedBy,String uploadedDate,String imageUrl)
    {
        this.trashName = trashName;
        this.trashQuantity = trashQuantity;
        this.trashPrice = trashPrice;
        this.trashDescription = trashDescription;
        this.trashCategory = trashCategory;
        this.sellerContact = sellerContact;
        this.uploadedBy = uploadedBy;
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

    public String getSellerContact() {
        return sellerContact;
    }

    public void setSellerContact(String sellerContact) {
        this.sellerContact = sellerContact;
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
}
