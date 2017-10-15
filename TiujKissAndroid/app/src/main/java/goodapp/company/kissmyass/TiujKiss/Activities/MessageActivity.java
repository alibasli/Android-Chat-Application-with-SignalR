package goodapp.company.kissmyass.TiujKiss.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import goodapp.company.kissmyass.Adapters.AdapterMessage;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.Models.Chat;
import goodapp.company.kissmyass.Models.Message;
import goodapp.company.kissmyass.Models.Messages;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.R;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.messages;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.chats;
/**
 * Created by mehme on 17.07.2016.
 */
public class MessageActivity extends AppCompatActivity {

    private ClassSession.SessionUserInfo UserSession = null;
    SharedPreferences sharedpreferences;

    final List<Message> messageList = new ArrayList<Message>();
    private Button mSend;
    private String ToUserID, UserId,ChatId;
    private Chat chat;
    private AdapterMessage adaptorumuz;
    private final Handler handler = new Handler();
    private ListView msg_list;
    private boolean setListUnder = true;
    private Messages activeMessageList;
    private TextView msgData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        currentActivity = this;
        msgData = (TextView) findViewById(R.id.enter_message);
        msg_list = (ListView) findViewById(R.id.list_msg);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                ToUserID = null;
            } else {
                ToUserID = extras.getString("SenderId");
            }
        } else {
            ToUserID = (String) savedInstanceState.getSerializable("SenderId");
        }
        chat = chats.getChatByUserId(ToUserID);
        if(chat != null) {
            ChatId = chat.ChatId;
            drawPartner(chat.PartnerId,chat.PartnerName,chat.PartnerLastname,chat.PartnerPhoto);
        }else{
            HubConnection.getUserById(ToUserID)
                    .done(new Action<User>() {
                        @Override
                        public void run(final User user) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    drawPartner(user.UserID,user.Name,user.Lastname,user.Photo);
                                }
                            });

                        }
                    });
        }
        mSend = (Button) findViewById(R.id.send_button);
        mSend.setOnClickListener(SendMessage);

        //requesterToServer();
        doStuff();
    }
    @Override
    @SuppressWarnings("unchecked")
    protected void onResume(){
        super.onResume();

        try {
            currentActivity = this;
            if(ChatId == null)
                ChatId  = chats.getChatByUserId(ToUserID).ChatId;
            activeMessageList = messages.getMessagesbyChatId(ChatId);
            if(!activeMessageList.isEmpty() ) {
                if(adaptorumuz == null) {
                    adaptorumuz = new AdapterMessage(this, activeMessageList);
                    msg_list.setAdapter(adaptorumuz);
                    msg_list.setSelection(adaptorumuz.getCount() - 1);
                   // msg_list.setOnItemClickListener(messageItemClick);
                    msg_list.setOnItemLongClickListener(messageItemLongClick);
                }else if(adaptorumuz.getCount() != activeMessageList.size()){
                    adaptorumuz = new AdapterMessage(this, activeMessageList);
                    msg_list.setAdapter(adaptorumuz);
                    msg_list.setSelection(adaptorumuz.getCount() - 1);
                   // msg_list.setOnItemClickListener(messageItemClick);
                    msg_list.setOnItemLongClickListener(messageItemLongClick);
                }
            }else{
                msg_list.setAdapter(null);
            }
        }catch (Exception ex){}
        handler.postDelayed(this.myRunnable, 3000);
    }
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
           onResume();
        }
    };

    @Override
    public void onPause() {
        handler.removeCallbacks(myRunnable);
        super.onPause();
    }
      @Override
    public void onBackPressed() {
          handler.removeCallbacks(myRunnable);
          super.onBackPressed();
    }
    private void doStuff(){

        AdView adView = (AdView) MessageActivity.this.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    public void goBack(View view){
        onBackPressed();
    }

    private void drawPartner(String PartnerId, String PartnerName, String PartnerLastname, String PartnerPhoto){
        ImageView partner_photo = (ImageView) findViewById(R.id.img_msguser);
        if (PartnerPhoto != "") {
            ImageLoader imgLoad = new ImageLoader(currentActivity);
            imgLoad.DisplayImage(ClassSession.SOCKET_URL + PartnerPhoto , partner_photo);
        }
        partner_photo.setTag(R.id.SENDER_ID, PartnerId);
        partner_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newInt = new Intent(getApplicationContext(), ProfileActivity.class);
                newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
                startActivity(newInt);
            }
        });
        TextView name = (TextView) findViewById(R.id.name_for_msguser);
        name.setText(PartnerName + " " + PartnerLastname);
    }

    AdapterView.OnItemClickListener messageItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    AdapterView.OnItemLongClickListener messageItemLongClick= new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView,final View view,final  int position, long l) {
            CharSequence OptionMenu[] = new CharSequence[] {/*currentActivity.getString(R.string.message_detail),*/
                    currentActivity.getString(R.string.delete_message), currentActivity.getString(R.string.copy_text),
                    currentActivity.getString(R.string.cancel)};

            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            builder.setTitle(currentActivity.getString(R.string.choose_event));
            builder.setItems(OptionMenu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        /*
                        case 0:
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(currentActivity);
                            builder2.setTitle(currentActivity.getString(R.string.message_detail));
                            builder2.setItems(MessageDetail, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder2.show();
                            break;
                            */
                        case 0:
                            HubConnection.deleteMessage(view.getTag(R.id.MESSAGE_ID).toString())
                                    .done(new Action<Boolean>() {
                                        @Override
                                        public void run(final Boolean status) {
                                            currentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (status) {
                                                        messages.removeByMessageId(view.getTag(R.id.MESSAGE_ID).toString());
                                                        onResume();
                                                        Toast.makeText(currentActivity, "Deleted.", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(currentActivity, "Delete Error!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
                            deleteMessage(view.getTag(R.id.MESSAGE_ID).toString() , position);
                            break;
                        case 1:
                            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) currentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText(view.getTag(R.id.MESSAGE_CONTENT).toString());
                            } else {
                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) currentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", view.getTag(R.id.MESSAGE_CONTENT).toString());
                                clipboard.setPrimaryClip(clip);
                            }
                            break;
                        default:
                            dialog.dismiss();
                            break;
                    }
                }
            });
            builder.show();
            return false;
        }
    };

    View.OnClickListener SendMessage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                HubConnection.sendMessage(msgData.getText().toString(),ToUserID,ChatId);
                msgData.setText("");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public void deleteMessage(String messageId, final int position) {
        try {

            /*
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/DeleteMessage/" + messageId, HttpRequest.GET, null, ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                messageList.remove(position);
                                adaptorumuz.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MessageActivity.this, "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute();
                    */
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
/*
    private void requesterToServer() {
        JSONObject login = new JSONObject();
        try {
          //  if (UserId.equals(null))
                //return;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            login.put("DateTimeNow",format.format(new Date())+".601");
            login.put("UserID", currentUser.getUserID());
            login.put("ToUserID", ToUserID);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/GetMessageList", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                drawMessages(response.body);
                            } else {
                                Toast.makeText(MessageActivity.this, "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawMessages(String data) {
        messageList.clear();
        try {
            if (data.equals(null) || data.equals("[]") || data.equals("")) {
                return;
            }
            JSONObject response = new JSONObject(data);
            ImageView owner_photo = (ImageView) findViewById(R.id.img_msguser);
            if (!response.getString("ProfilePhoto").equals("null")) {
                byte[] byteArray = Base64.decode(response.getString("ProfilePhoto"), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                owner_photo.setImageBitmap(bm);
            }
            owner_photo.setTag(R.id.SENDER_ID, response.getString("UserID"));
            owner_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newInt = new Intent(getApplicationContext(), ProfileActivity.class);
                    newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
                    startActivity(newInt);
                }
            });
            TextView name = (TextView) findViewById(R.id.name_for_msguser);
            name.setText(response.getString("Name") + " " + response.getString("LastName"));

            if (!response.getString("MessageList").equals(null) && !response.getString("MessageList").equals("[]") && !response.getString("MessageList").equals("")) {
                JSONArray jsonArray = new JSONArray(response.getString("MessageList"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    messageList.add(new Message(
                            jsonObject.getString("MessageID"),
                            jsonObject.getString("SenderID"),
                            jsonObject.getString("Content"),
                            jsonObject.getString("SentDate"),
                            jsonObject.getString("SeenDate"),
                            jsonObject.getBoolean("SeenStatus"),
                            jsonObject.getBoolean("DeliveredStatus"),
                            jsonObject.getString("DeliveredDate")
                    ));
                }
                adaptorumuz.notifyDataSetChanged();
                if(setListUnder) {
                    msg_list.setSelection(adaptorumuz.getCount() - 1);
                    setListUnder=false;
                }
            }
            handler.postDelayed(this.myRunnable, 3000);
        } catch (Exception ex) {
            Toast.makeText(MessageActivity.this, "Json Error! Please try later again.", Toast.LENGTH_LONG).show();
        }
    }
*/

}
