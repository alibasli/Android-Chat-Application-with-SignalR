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

import com.mehme.menuexample.Model.ModelFeedback;
import com.mehme.menuexample.R;

import java.util.List;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterFeedback extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ModelFeedback> mPostList;

    public AdapterFeedback(Activity activity, List<ModelFeedback> mPostList) {
        mInflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        this.mPostList = mPostList;
    }
    @Override
    public int getCount() {
        return mPostList.size();
    }

    @Override
    public ModelFeedback getItem(int position) {
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

        satirView = mInflater.inflate(R.layout.item_feedback, null);

        TextView owner_name_surname = (TextView) satirView.findViewById(R.id.owner_name_surname);
        TextView feed_body = (TextView) satirView.findViewById(R.id.feed_body);
        TextView post_title = (TextView) satirView.findViewById(R.id.post_title);
        TextView feed_date_time = (TextView) satirView.findViewById(R.id.feed_date_time);
        ImageView owner_photo = (ImageView) satirView.findViewById(R.id.owner_photo);

        ModelFeedback feed = mPostList.get(position);
        Bitmap bm = BitmapFactory.decodeByteArray(feed.getOwnerPhoto(), 0, feed.getOwnerPhoto().length);
        owner_photo.setImageBitmap(bm);
        owner_name_surname.setText(feed.getOwnerNameSurname());
        feed_body.setText(feed.getFeedBody());
        post_title.setText(feed.gePostTitle());
        feed_date_time.setText(feed.getFeedDateTime());
        return satirView;
    }
}