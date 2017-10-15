package com.mehme.menuexample.Class;

import android.app.Application;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by mehme on 22.06.2016.
 */
public class ClassSocketObject extends Application {


    private static URI uri;
    {
        try {
            uri = new URI("ws://192.168.0.12:2255/Api/Socket/AliB");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public static URI getNewSocket() {
        return uri;
    }
}
