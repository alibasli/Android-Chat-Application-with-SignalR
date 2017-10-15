package goodapp.company.kissmyass.TiujKiss;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.TimeZone;

import goodapp.company.kissmyass.Models.Chat;
import goodapp.company.kissmyass.Models.Chats;
import goodapp.company.kissmyass.Models.Feedback;
import goodapp.company.kissmyass.Models.Feedbacks;
import goodapp.company.kissmyass.Models.Message;
import goodapp.company.kissmyass.Models.Messages;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.SignalR.Connection;

/**
 * Created by mehme on 3.08.2016.
 */
public class TiujKissR extends Application {

    public static Connection HubConnection;
    public static Activity currentActivity;
    public static User currentUser;
    public static Chats chats;
    public static Feedbacks feedbacks;
    public static Messages messages;
    public static TimeZone serverTimezone;

    @Override
    public void onCreate()
    {
        super.onCreate();
        chats = new Chats();
        feedbacks = new Feedbacks();
        messages = new Messages();
        serverTimezone = TimeZone.getDefault();
    }


/*    public static Chat chatExists(int chatee)
    {
        for (Chat chat : activeChats) {
            for (int userId : chat.Participants) {
                if (userId == chatee)
                    return chat;
            }
        }

        Chat NF = new Chat();
        NF.ChatId = "NotFound";
        return NF;
    }*/
/*
    public static boolean chatExists(String chatId)
    {
        for(Chat chat : activeChats) {
            if(chat.ChatId.equals(chatId))
                return true;
        }

        return false;
    }*/

    public void setActivity(Activity activity)
    {
        currentActivity = activity;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
