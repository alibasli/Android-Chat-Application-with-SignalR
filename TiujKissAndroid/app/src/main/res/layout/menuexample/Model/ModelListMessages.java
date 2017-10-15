package com.mehme.menuexample.Model;

/**
 * Created by mehme on 24.06.2016.
 */
public class ModelListMessages {
    private int senderId;
    private String senderNameSurname;
    private byte[] senderPhoto;
    private Boolean messageRead;

    public ModelListMessages(int senderId, String senderNameSurname, byte[] senderPhoto, Boolean messageRead){
        this.senderId = senderId;
        this.senderNameSurname = senderNameSurname;
        this.messageRead = messageRead;
        this.senderPhoto = senderPhoto;

    }
    public int getSenderId() {
        return senderId;
    }
    public String getSenderNameSurname() {
        return senderNameSurname;
    }
    public Boolean getMessageRead() {
        return messageRead;
    }
    public byte[] getSenderPhoto(){
        return senderPhoto;
    }

}
