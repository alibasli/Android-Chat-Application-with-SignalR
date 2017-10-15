package com.mehme.menuexample.Model;

import java.util.List;

/**
 * Created by mehme on 24.06.2016.
 */
public class ModelPost {

    private int PostId;
    private int PostOwnerId;
    private String PostOwnerNameSurname;
    private String PostDateTime;
    private String PostTitle;
    private String PostBody;
    private byte[] PostOwnerProfileImage;
    private List<byte[]> PostImages;

    public ModelPost(int PostId, int PostOwnerId, String PostOwnerNameSurname, String PostDateTime, byte[] PostOwnerProfileImage, String PostTitle , String PostBody, List<byte[]> PostImages){
        this.PostId = PostId;
        this.PostOwnerId = PostOwnerId;
        this.PostOwnerNameSurname = PostOwnerNameSurname;
        this.PostDateTime = PostDateTime;
        this.PostOwnerProfileImage = PostOwnerProfileImage;
        this.PostTitle = PostTitle;
        this.PostBody = PostBody;
        this.PostImages = PostImages;

    }
    public int getPostId() {
        return PostId;
    }
    public int getPostOwnerId() {
        return PostOwnerId;
    }
    public String getPostOwnerNameSurname(){
        return PostOwnerNameSurname;
    }
    public String getPostDateTime(){
        return PostDateTime;
    }
    public String getPostTitle() {
        return PostTitle;
    }

    public String getPostBody(){
        return PostBody;
    }

    public byte[] getPostOwnerProfileImage(){
        return PostOwnerProfileImage;
    }
    public List<byte[]> getPostImages(){
        return PostImages;
    }


}
