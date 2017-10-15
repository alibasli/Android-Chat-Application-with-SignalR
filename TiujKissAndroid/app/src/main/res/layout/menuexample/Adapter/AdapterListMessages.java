package com.mehme.menuexample.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehme.menuexample.MessageActivity;
import com.mehme.menuexample.R;

import java.util.List;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterListMessages extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<com.mehme.menuexample.Model.ModelListMessages> mMessagerList;
    private Activity activity;

    public AdapterListMessages(Activity activity, List<com.mehme.menuexample.Model.ModelListMessages> mMessagerList) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mMessagerList = mMessagerList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mMessagerList.size();
    }

    @Override
    public com.mehme.menuexample.Model.ModelListMessages getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mMessagerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;
        final com.mehme.menuexample.Model.ModelListMessages messager = mMessagerList.get(position);

        satirView = mInflater.inflate(R.layout.item_list_messages, null);
        satirView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newInt = new Intent(activity, MessageActivity.class);
                newInt.putExtra("SenderId" , messager.getSenderId());
                activity.startActivity(newInt);
            }
        });
        TextView name_surname = (TextView) satirView.findViewById(R.id.name_surname);
        ImageView sender_photo = (ImageView) satirView.findViewById(R.id.sender_photo);



        Bitmap bm = BitmapFactory.decodeByteArray(messager.getSenderPhoto(), 0, messager.getSenderPhoto().length);
        sender_photo.setImageBitmap(bm);

        name_surname.setText(messager.getSenderNameSurname());
        LinearLayout ly = (LinearLayout) satirView.findViewById(R.id.list_messager_background);
        if (messager.getMessageRead()) {
            ly.setBackgroundResource(R.color.colorBackGround);
        } else {
            ly.setBackgroundResource(R.color.colorBackGround);
        }
        return satirView;
    }
}