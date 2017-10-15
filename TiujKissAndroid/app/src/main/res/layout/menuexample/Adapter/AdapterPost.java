package com.mehme.menuexample.Adapter;

import android.app.Activity;
import android.content.Context;
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
public class AdapterPost extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<com.mehme.menuexample.Model.ModelPost> mPostList;

    public AdapterPost(Activity activity, List<com.mehme.menuexample.Model.ModelPost> mPostList) {
        mInflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        this.mPostList = mPostList;
    }
    @Override
    public int getCount() {
        return mPostList.size();
    }

    @Override
    public com.mehme.menuexample.Model.ModelPost getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.item_post, null);
        TextView owner_name_surname = (TextView) satirView.findViewById(R.id.post_owner_name_surname);
        ImageView owner_photo = (ImageView) satirView.findViewById(R.id.post_owner_sender_photo);
        TextView post_title = (TextView) satirView.findViewById(R.id.post_owner_title);
        TextView post_body = (TextView) satirView.findViewById(R.id.post_owner_body);
        TextView post_date = (TextView) satirView.findViewById(R.id.post_date_time);

        com.mehme.menuexample.Model.ModelPost post = mPostList.get(position);

        Bitmap bm = BitmapFactory.decodeByteArray(post.getPostOwnerProfileImage(), 0, post.getPostOwnerProfileImage().length);
        owner_photo.setImageBitmap(bm);
        owner_name_surname.setText(post.getPostOwnerNameSurname());
        post_date.setText(post.getPostDateTime());
        post_title.setText(post.getPostTitle());
        post_body.setText(post.getPostBody());

        return satirView;
    }
}