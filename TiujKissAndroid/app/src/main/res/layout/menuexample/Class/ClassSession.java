package com.mehme.menuexample.Class;

/**
 * Created by mehme on 16.06.2016.
 */
public class ClassSession {
    public  class SessionUserInfo{
        public static final String userInfoSession = "UserInfoPREFERENCES" ;
        public static final String Id = "UserId"; // type int
        public static final String Email = "UserEmail"; //type string
        public static final String Name = "userName";  //type string
        public static final String Surname = "userSurname";  //type string
        public static final String Gender = "userGender";  //type string
        public static final String Birthday = "userBirthday";  //type string

    }

    // socket url adress
    public static final String CHAT_SERVER_URL = "http://192.168.0.12:3000/";
    public static final String SERVER_URL = "http://192.168.0.12:2255/";
    public static final String CHAT_URL = "ws://192.168.0.12:2255/Api/Socket/Ali";//"http://54.86.103.211:8080/socket.io";
    public static final String AUTHORIZATION = "Basic cmltYWxpQGdtYWlsLmNvbTpyaW1hbGlSZXNwb25zZQ==";
}

