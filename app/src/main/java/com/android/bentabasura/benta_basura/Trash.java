package com.android.bentabasura.benta_basura;

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
    private String sellerContact;
    private String uploadedBy;
    private String uploadedDate;
    private String imageUrl;
    private long sold;

    public Trash()
    {

    }
    public Trash(String trashName,String trashQuantity,String trashPrice,String trashDescription,String trashCategory, String sellerContact, String uploadedBy,String uploadedDate,String imageUrl, long sold)
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
        this.sold = sold;
        this.trashId = "";
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

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    public String getTrashId() {
        return trashId;
    }

    public void setTrashId(String trashId) {
        this.trashId = trashId;
    }


}
