package com.android.bentabasura.benta_basura.Models;

/**
 * Created by ccs on 10/19/17.
 *
 */

public class Notification
{
    private String notifId;
    private String notifDbLink;
    private String notifMessage;
    private String notifOwnerId;
    private String notifBy;
    private String notifRead;
    private String notifNotify;
    private String notifByPic;
    private String notifDate;

    public Notification(){

    }
    public Notification(String notifDbLink, String notifMessage, String notifOwnerId, String notifBy, String notifRead, String notifNotify, String notifByPic, String notifDate)
    {
        this.notifDbLink = notifDbLink;
        this.notifMessage = notifMessage;
        this.notifOwnerId = notifOwnerId;
        this.notifBy = notifBy;
        this.notifRead = notifRead;
        this.notifNotify = notifNotify;
        this.notifByPic = notifByPic;
        this.notifDate = notifDate;
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }

    public String getNotifDbLink() {
        return notifDbLink;
    }

    public void setNotifDbLink(String notifDbLink) {
        this.notifDbLink = notifDbLink;
    }

    public String getNotifMessage() {
        return notifMessage;
    }

    public void setNotifMessage(String notifMessage) {
        this.notifMessage = notifMessage;
    }

    public String getNotifOwnerId() {
        return notifOwnerId;
    }

    public void setNotifOwnerId(String notifOwnerId) {
        this.notifOwnerId = notifOwnerId;
    }

    public String getNotifBy() {
        return notifBy;
    }

    public void setNotifBy(String notifBy) {
        this.notifBy = notifBy;
    }

    public String getNotifRead() {
        return notifRead;
    }

    public void setNotifRead(String notifRead) {
        this.notifRead = notifRead;
    }

    public String getNotifNotify() {
        return notifNotify;
    }

    public void setNotifNotify(String notifNotify) {
        this.notifNotify = notifNotify;
    }

    public String getNotifByPic() {
        return notifByPic;
    }

    public void setNotifByPic(String notifByPic) {
        this.notifByPic = notifByPic;
    }

    public String getNotifDate() {
        return notifDate;
    }

    public void setNotifDate(String notifDate) {
        this.notifDate = notifDate;
    }

}
