package goodapp.company.kissmyass.TiujKiss.Tabs;

/**
 * Created by mehme on 14.07.2016.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import goodapp.company.kissmyass.TiujKiss.Activities.MainActivity;
import goodapp.company.kissmyass.Adapters.AdapterChats;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Models.Chat;
import goodapp.company.kissmyass.Models.Chats;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.TiujKiss.Activities.MessageActivity;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.chats;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;


public class Tab2Chats extends Fragment {
    private AdapterChats adaptor;
    private ListView listView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab2_messages, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        populateChats();
    }

    public void populateChats() {
        try {
            if (adaptor != null)
                adaptor.notifyDataSetChanged();
            else if (!chats.isEmpty()) {
                listView = (ListView) currentActivity.findViewById(R.id.messager_list);
                adaptor = new AdapterChats(currentActivity, chats);
                listView.setAdapter(adaptor);
                listView.setOnItemClickListener(chatListClick);
                listView.setOnItemLongClickListener(chatListLongClick);
            }
        } catch (Exception NPE) {
            //  Toast.makeText(currentActivity,NPE.toString(),Toast.LENGTH_LONG).show();
        }
    }
    AdapterView.OnItemClickListener chatListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Intent newInt = new Intent(currentActivity, MessageActivity.class);
            newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
            currentActivity.startActivity(newInt);
        }
    };
    AdapterView.OnItemLongClickListener chatListLongClick  = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, final View view,final int position, long l) {
            CharSequence OptionMenu[] = new CharSequence[]{currentActivity.getString(R.string.delete_conversation) ,currentActivity.getString(R.string.cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            builder.setTitle(currentActivity.getString(R.string.choose_event));
            builder.setItems(OptionMenu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            HubConnection.deleteChat(view.getTag(R.id.CHAT_ID).toString())
                                    .done(new Action<Boolean>() {
                                        @Override
                                        public void run(final Boolean status) {
                                            currentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (status) {
                                                        chats.remove(chats.get(position));
                                                        populateChats();
                                                        Toast.makeText(currentActivity, "Deleted.", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(currentActivity, "Delete Error!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
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
    public void DeleteConversation(String ToUserID) {

        /*
        JSONObject login = new JSONObject();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            login.put("DateTimeNow", format.format(new Date()) + ".601");
            login.put("ToUserID", ToUserID);
            if(httpRequest("Api/DeleteConversation",login,"POST")){
                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    public void run() {
                        if (responsed == 1) {
                            responsed = 0;
                            reloadTab();
                        } else if (responsed == 2) {
                            responsed = 0;
                            Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            //TODO request failed
                        } else {
                            h.postDelayed(this, 1000);
                        }
                    }
                }, 1000);
            } else {
                Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        */

    }

/*    private void getChatList(){
       // progressDialog.setMessage("loading chats...");
      //  progressDialog.show();
        HubConnection.getChats(currentUser.UserID).done(new Action<Chats>()
        {
            @Override
            public void run(final Chats chats)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawChats(chats);
                    }
                });

            }
        });
    }
    private void drawChats(Chats chats){
        try {
            final ListView messager_list = (ListView) mView.findViewById(R.id.messager_list);
            AdapterChats adaptorumuz = new AdapterChats(getActivity(), chats);
            messager_list.setAdapter(adaptorumuz);
        }catch (Exception ex){
            Toast.makeText(currentActivity,"Draw chats error!",Toast.LENGTH_LONG).show();
        }
    }*/

/*    public void showFailedView(String text){
        ListView messager_list = (ListView) mView.findViewById(R.id.messager_list);
        messager_list.setVisibility(View.GONE);
        View view_failed = mView.findViewById(R.id.view_failed);
        view_failed.setVisibility(View.VISIBLE);
        TextView txt_failed = (TextView) mView.findViewById(R.id.txt_failed);
        txt_failed.setText(text);
    }*/
/*
private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            requesterToServer();

        }
    };
    private void requesterToServer() {
        try {
            String userId = sharedpreferences.getString(UserSession.Id, "");
            if (userId.equals("")) {
                mMainActivityActivity.showProgress(false);
                return;
            }
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/GetChatList/" + userId, HttpRequest.GET, null, ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                drawMessages(response.body);
                            } else {
                                showFailedView("messages/requesterToServer " + response.code + " Error!");
                                //Toast.makeText(getActivity().getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                            mMainActivityActivity.showProgress(false);
                        }
                    }).execute();
        }
        catch (Exception e){
            mMainActivityActivity.showProgress(false);
        }
    }

    private void drawMessages(String jsonString) {
        messagerList.clear();
        try {
            if(jsonString.equals(null) || jsonString.equals("[]") || jsonString.equals("")){
                showFailedView(getString(R.string.no_message_text));
                return;
            }
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                messagerList.add(new Chat(
                        jsonObject.getString("SenderID"),
                        jsonObject.getString("Name"),
                        jsonObject.getString("Surname"),
                        jsonObject.getString("SenderPhoto").equals("null") ? null : Base64.decode(jsonObject.getString("SenderPhoto"), Base64.DEFAULT),
                        jsonObject.getString("Content"),
                        jsonObject.getBoolean("MessageRead"),
                        jsonObject.getInt("UnreadMessage"),
                        jsonObject.getString("LastMessageDate")

                        ));
            }
            final ListView messager_list = (ListView) mView.findViewById(R.id.messager_list);
            AdapterChats adaptorumuz = new AdapterChats(getActivity(), messagerList);
            messager_list.setAdapter(adaptorumuz);

            handler.postDelayed(this.myRunnable, 10000);
        } catch (Exception ex) {
            showFailedView("Messages/drawMessages JSONObject error!");
        }

    }
    */

}
