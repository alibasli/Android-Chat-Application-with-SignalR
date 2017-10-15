package com.mehme.menuexample.Model;

/**
 * Created by mehme on 24.06.2016.
 */
public class ModelSearch {
    private int userId;
    private String userNameSurname;
    private byte[] userPhoto;

    public ModelSearch(int userId, String userNameSurname, byte[] userPhoto){
        this.userId = userId;
        this.userNameSurname = userNameSurname;
        this.userPhoto = userPhoto;

    }
    public int getUserId() {
        return userId;
    }
    public String getUserNameSurname() {
        return userNameSurname;
    }
    public byte[] getUserPhoto(){
        return userPhoto;
    }

}
