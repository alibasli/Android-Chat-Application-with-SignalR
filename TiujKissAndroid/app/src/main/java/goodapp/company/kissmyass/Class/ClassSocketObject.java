package goodapp.company.kissmyass.Class;

import android.app.Application;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by mehme on 22.06.2016.
 */
public class ClassSocketObject extends Application {
    String url ="";
    public ClassSocketObject(String url){
        this.url = url;
    }
    private static URI uri;
    {
        try {
            uri = new URI( ClassSession.SOCKET_URL + url );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public static URI getNewSocket() {
        return uri;
    }
}
