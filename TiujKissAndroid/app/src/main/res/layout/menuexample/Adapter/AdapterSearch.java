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
import android.widget.TextView;

import com.mehme.menuexample.R;

import java.util.List;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterSearch extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<com.mehme.menuexample.Model.ModelSearch> mUserList;
    private Activity activity;

    public AdapterSearch(Activity activity, List<com.mehme.menuexample.Model.ModelSearch> mUserList) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mUserList = mUserList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public com.mehme.menuexample.Model.ModelSearch getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;
        final com.mehme.menuexample.Model.ModelSearch user = mUserList.get(position);

        satirView = mInflater.inflate(R.layout.item_search, null);
        satirView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newInt = new Intent(activity, com.mehme.menuexample.ProfileActivity.class);
                newInt.putExtra("UserId" , user.getUserId());
                activity.startActivity(newInt);
            }
        });
        TextView name_surname = (TextView) satirView.findViewById(R.id.user_name_surname);
        ImageView sender_photo = (ImageView) satirView.findViewById(R.id.user_photo);

        Bitmap bm = BitmapFactory.decodeByteArray(user.getUserPhoto(), 0, user.getUserPhoto().length);
        sender_photo.setImageBitmap(bm);

        name_surname.setText(user.getUserNameSurname());

        return satirView;
    }
}