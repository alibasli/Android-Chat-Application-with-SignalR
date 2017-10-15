package com.mehme.menuexample.Model;

/**
 * Created by mehme on 22.06.2016.
 */
public class ModelFeedback {
    private int FeedId;
    private String FeedBody;
    private int OwnerId;
    private String OwnerNameSurname;
    private byte[] OwnerPhoto;
    private  int PostId;
    private String PostTitle;
    private String FeedDateTime;
    private Boolean FeedRead;

    public ModelFeedback(int FeedId, String FeedBody,
                         int OwnerId,String OwnerNameSurname,byte[] OwnerPhoto,
                         int PostId,String PostTitle,String FeedDateTime,Boolean FeedRead){
        this.FeedId = FeedId;
        this.FeedBody = FeedBody;
        this.OwnerId = OwnerId;
        this.OwnerNameSurname = OwnerNameSurname;
        this.OwnerPhoto = OwnerPhoto;
        this.PostId = PostId;
        this.PostTitle = PostTitle;
        this.FeedDateTime = FeedDateTime;
        this.FeedRead = FeedRead;

    }
    public int getFeedId() {
        return FeedId;
    }
    public String getFeedBody() {
        return FeedBody;
    }
    public int getOwnerId() {
        return OwnerId;
    }
    public String getOwnerNameSurname() {
        return OwnerNameSurname;
    }
    public byte[] getOwnerPhoto(){
        return OwnerPhoto;
    }
    public int getPostId() {
        return PostId;
    }
    public String gePostTitle() {
        return PostTitle;
    }
    public String getFeedDateTime() {
        return FeedDateTime;
    }
    public Boolean getFeedRead() {
        return FeedRead;
    }
}