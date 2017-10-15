package goodapp.company.kissmyass.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import goodapp.company.kissmyass.TiujKiss.Activities.MainActivity;
import goodapp.company.kissmyass.TiujKiss.Activities.ProfileActivity;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.Models.Feedback;
import goodapp.company.kissmyass.Models.Feedbacks;
import goodapp.company.kissmyass.R;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterFeedback extends BaseAdapter {

    private LayoutInflater mInflater;
    private Feedbacks mPostList;
    private Activity mActivity;
    private Feedback feed;
    private ImageLoader imgLoad;
    private ViewHolder holder;

    public AdapterFeedback(Activity activity, Feedbacks mPostList) {
        mInflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        this.mPostList = mPostList;
        mActivity = activity;
        imgLoad = new ImageLoader(currentActivity);
        holder = new ViewHolder();
    }
    @Override
    public int getCount() {
        return mPostList.size();
    }

    @Override
    public Feedback getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView notify_name_surname;
        TextView notify_type;
        TextView notify_date;
        ImageView owner_photo;
    }

    @Override
    public View getView(int position, View satirView, ViewGroup parent) {
        if(satirView == null){
            satirView = mInflater.inflate(R.layout.item_feedback, null);
        }
        feed = mPostList.get(position);
        holder.notify_name_surname = (TextView) satirView.findViewById(R.id.notify_name_surname);
        holder.notify_type = (TextView) satirView.findViewById(R.id.notify_type);
        holder.notify_date = (TextView) satirView.findViewById(R.id.notify_date);
        holder.owner_photo = (ImageView) satirView.findViewById(R.id.owner_photo);

/*        TextView notify_name_surname = (TextView) satirView.findViewById(R.id.notify_name_surname);
        TextView notify_type = (TextView) satirView.findViewById(R.id.notify_type);
        TextView notify_date = (TextView) satirView.findViewById(R.id.notify_date);
        ImageView owner_photo = (ImageView) satirView.findViewById(R.id.owner_photo);   */

        if(feed.getProfilePhoto() != null) {
            imgLoad.DisplayImage(ClassSession.SOCKET_URL+feed.getProfilePhoto(),holder.owner_photo);
           // Bitmap bm = BitmapFactory.decodeByteArray(feed.getProfilePhoto(), 0, feed.getProfilePhoto().length);
            //owner_photo.setImageBitmap(bm);
        }
        holder.owner_photo.setTag(R.id.SENDER_ID,feed.getFromUserID());
        holder.owner_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newInt = new Intent(currentActivity, ProfileActivity.class);
                newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
                currentActivity.startActivity(newInt);
            }
        });

        holder.notify_name_surname.setText(feed.getName()  + " " + feed.getLastName());
        holder.notify_type.setText(feed.getNotificationType() +"ed your ass.");
        holder.notify_date.setText(feed.getDateSending());
        if(!feed.getSeenStatus())
            satirView.setBackgroundResource(R.color.Silver);
        satirView.setTag(R.id.FEEDBACK_ID,feed.getFeedbackID());
        satirView.setTag(R.id.SENDER_ID,feed.getFromUserID());
        //satirView.setTag(holder);
        return satirView;
    }
}