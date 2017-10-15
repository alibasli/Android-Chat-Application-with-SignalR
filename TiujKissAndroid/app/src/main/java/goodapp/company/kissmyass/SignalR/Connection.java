package goodapp.company.kissmyass.SignalR;

import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import goodapp.company.kissmyass.Models.Chat;
import goodapp.company.kissmyass.Models.Feedback;
import goodapp.company.kissmyass.Models.Message;
import goodapp.company.kissmyass.Models.Messages;
import goodapp.company.kissmyass.Models.Users;
import goodapp.company.kissmyass.TiujKiss.Activities.NLoginActivity;
import goodapp.company.kissmyass.Models.Chats;
import goodapp.company.kissmyass.Models.Feedbacks;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.TiujKiss.TiujKissR;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.feedbacks;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.chats;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.messages;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.serverTimezone;

/**
 * Created by mehme on 3.08.2016.
 */
public class Connection {
    public HubConnection connection;
    public HubProxy distributionHub;

    public Connection(String hubURL, NetworkInfo Wifi, NetworkInfo MobileData) {

        Logger logger = new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {
                Log.w("SignalR", s);
            }
        };

        this.connection = new HubConnection(hubURL, "", true, logger);

        distributionHub = this.connection.createHubProxy("SignalRHub");

        this.connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                InitListeners();
            }
        }).onError(new ErrorCallback() {
            public void onError(Throwable error) {
                connection.start().done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        InitListeners();
                    }
                });
            }
        });

        this.connection.closed(new Runnable() {
            @Override
            public void run() {
                connection.start().done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        InitListeners();
                    }
                }).onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable error) {
                        currentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(currentActivity, "Unable to establish connection! Please check your internet connection.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                 distributionHub.invoke("JoinRealTime", currentUser);
            }
        });
    }


    public void InitListeners() {
        Runnable listener = new Runnable() {
            @Override
            public void run() {
                distributionHub.on("ConnectionSuccessful",
                        new SubscriptionHandler() {
                            @Override
                            public void run() {
                                Log.w("CallBack from Hub", "Successful Connection");
                            }
                        });
                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void loginSuccess(String userId, String Name, String LastName, String Email, String Gender, String BirthDate, String Photo, String About) {
                        User user = new User(userId, Name, LastName, Photo, Email, BirthDate, Gender, About);
                        ((NLoginActivity) currentActivity).loginSuccess(user);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void loginFailure(String status) {
                        ((NLoginActivity) currentActivity).loginFailure(status);
                    }
                });
                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void registerSuccess(String userId, String Name, String LastName, String Email, String Gender, String BirthDate) {
                        User user = new User(userId, Name, LastName, null, Email, BirthDate, Gender, null);
                        ((NLoginActivity) currentActivity).registerSuccess(user);
                    }
                });

                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void registerFailure(String reason) {
                        ((NLoginActivity) currentActivity).registerFailure(reason);
                    }
                });
                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void newMessage(Message message) {
                        messages.add(message);
                        chats.updateChat(message);
                    }
                });
                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void newChat(Chat chat) {
                        chats.addChat(chat);
                    }
                });
                distributionHub.subscribe(new Object() {
                    @SuppressWarnings("unused")
                    public void newFeedback(Feedback feed) {
                        addFeedback(feed);
                    }
                });

            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }

    private void addFeedback(Feedback feed){
        Feedbacks tempFeeds = new Feedbacks();
        tempFeeds.add(feed);
        for (Feedback c: feedbacks ) {
            tempFeeds.add(c);
        }
        feedbacks.clear();
        feedbacks = tempFeeds;
    }

    public void login(String username, String password) {
        distributionHub.invoke("Login", username, password).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void register(String mEmail, String mPassword, String name, String surname, String birthDay, String gender) {
        Object[] TaskParams = {mEmail, mPassword, name, surname, birthDay, gender};
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                distributionHub.invoke("Register", params[0], params[1], params[2], params[3], params[4], params[5]);
                return null;
            }
        }.execute(TaskParams);
    }

    public SignalRFuture<User> getUserById(String userId) {
        return distributionHub.invoke(User.class, "GetUserByID", userId);
    }

    public SignalRFuture<User> getUserByDefault(String userId) {
        return distributionHub.invoke(User.class, "GetUserByDefault", userId);
    }

    public SignalRFuture<Boolean> createKiss(String fromUserId, String toUserId) {
        return distributionHub.invoke(Boolean.class, "CreateKiss", fromUserId, toUserId);
    }
    public SignalRFuture<Boolean> deleteChat(String chatId) {
        return distributionHub.invoke(Boolean.class, "DeleteChat", currentUser.UserID, chatId);
    }
    public SignalRFuture<Boolean> deleteMessage(String messageId) {
        return distributionHub.invoke(Boolean.class, "DeleteMessage", messageId);
    }
    public SignalRFuture<Boolean> setFeedbackSeenStatus(String FeedbackId) {
        return distributionHub.invoke(Boolean.class, "SetFeedbackSeenStatus", FeedbackId);
    }

    public void getChats() {
        distributionHub.invoke(Chats.class, "GetUserChats", currentUser.UserID).done(new Action<Chats>() {
            public void run(Chats _chats) {
                chats = _chats;
            }
        });
        //getServerTimezone();
    }
    public void getAllUserMessages() {
        distributionHub.invoke(Messages.class, "GetAllUserMessages", currentUser.UserID).done(new Action<Messages>() {
            public void run(Messages _messages) {
                messages = _messages;
            }
        });
    }
    public void getFeedbacks() {
        distributionHub.invoke(Feedbacks.class, "GetUserFeedbacks", currentUser.UserID).done(new Action<Feedbacks>() {
            public void run(Feedbacks feeds) {
                feedbacks = feeds;
            }
        });
    }
    public SignalRFuture<Users> getSearchUsers(String UserId, String key){
        return distributionHub.invoke(Users.class, "GetSearchUsers", UserId,key);
    }
    /*
    public SignalRFuture<Boolean> createChat(String fromUserId, String toUserId) {
        return distributionHub.invoke(Boolean.class, "CreateChat", fromUserId, toUserId);
    }
    */
    public void sendMessage(String content,String PartnerId,String ChatId){
        distributionHub.invoke("SendMessage", currentUser.UserID,PartnerId,ChatId,content).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(currentActivity,"send Message Error",Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }
        });
    }


    public void getServerTimezone() {
        distributionHub.invoke(TimeZone.class, "GetTimezone").done(new Action<TimeZone>() {
            public void run(TimeZone timeZone) {
                if(timeZone != null)
                    serverTimezone = timeZone;
            }
        });
    }

}
