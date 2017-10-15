package goodapp.company.kissmyass.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.Models.Chat;
import goodapp.company.kissmyass.Models.Chats;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.TiujKiss.Activities.*;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterChats extends BaseAdapter {

    private LayoutInflater mInflater;
    private Chats mChats;
    private Chat chat;
    private ImageLoader imgLoad;
    private ViewHolder holder;

    public AdapterChats(Activity activity, Chats chats) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mChats = chats;
        imgLoad = new ImageLoader(currentActivity);
        holder = new ViewHolder();
    }

    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Chat getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mChats.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView list_msg_name_surname;
        TextView list_msg_date;
        TextView list_msg_content;
        TextView list_msg_unread_count;
        ImageView sender_photo;
        View lyt_message;
    }

    @Override
    public View getView(int position, View satirView, ViewGroup parent) {
        if(satirView == null){
            satirView = mInflater.inflate(R.layout.item_list_messages, null);
        }
        chat = getItem(position);
        holder.lyt_message = satirView.findViewById(R.id.lyt_message);
        holder.list_msg_name_surname = (TextView) satirView.findViewById(R.id.list_msg_name_surname);
        holder.list_msg_date = (TextView) satirView.findViewById(R.id.list_msg_date);
        holder.list_msg_content = (TextView) satirView.findViewById(R.id.list_msg_content);
        holder.list_msg_unread_count = (TextView) satirView.findViewById(R.id.list_msg_unread_count);
        holder.sender_photo = (ImageView) satirView.findViewById(R.id.sender_photo);

        if (chat.getPartnerPhoto() != null) {
            imgLoad.DisplayImage(ClassSession.SOCKET_URL+chat.getPartnerPhoto(),holder.sender_photo);
            //imgLoad.DisplayImage(ClassSession.SOCKET_URL+chat.getPartnerPhoto(),holder.sender_photo);
           // Bitmap bm = BitmapFactory.decodeByteArray(messager.getSenderPhoto(), 0, messager.getSenderPhoto().length);
           // sender_photo.setImageBitmap(bm);
        }
        holder.sender_photo.setTag(R.id.SENDER_ID,chat.getPartnerId());
        holder.sender_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newInt = new Intent(currentActivity, ProfileActivity.class);
                newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
                currentActivity.startActivity(newInt);
            }
        });

        holder.list_msg_name_surname.setText(chat.getPartnerName() +" "+ chat.getPartnerLastname());
        holder.list_msg_date.setText(chat.getLastMessageDate());
        holder.list_msg_content.setText(chat.getLastMessage());
        if (chat.getUnreadMessageCount() == 0)
            holder.list_msg_unread_count.setVisibility(View.GONE);
        else {
            holder.list_msg_unread_count.setText(Integer.toString(chat.getUnreadMessageCount()));
            holder.list_msg_unread_count.setBackgroundResource(R.color.SlateGrey);
            holder.lyt_message.setBackgroundResource(R.color.Silver);
        }
        satirView.setTag(R.id.SENDER_ID,chat.getPartnerId());
        satirView.setTag(R.id.CHAT_ID,chat.getChatId());

        //satirView.setTag(holder);
        return satirView;
    }

}