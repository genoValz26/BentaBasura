package com.android.bentabasura.benta_basura;

/**
 * Created by ccs on 10/19/17.
 */

public class Comment {

    private String profileImage;
    private String profileName;
    private String commentDate;
    private String comment;
    private String commendID;

    public  Comment()
    {

    }
    public Comment(String profileImage, String profileName, String commentDate, String comment)
    {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.commentDate = commentDate;
        this.comment = comment;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommendID() {
        return commendID;
    }

    public void setCommendID(String commendID) {
        this.commendID = commendID;
    }

}
