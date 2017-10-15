package goodapp.company.kissmyass.Adapters;

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

import java.util.List;

import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.Models.Users;
import goodapp.company.kissmyass.TiujKiss.Activities.ProfileActivity;
import goodapp.company.kissmyass.R;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterSearch extends BaseAdapter {

    private LayoutInflater mInflater;
    private Users users;
    private ViewHolder holder;
    private ImageLoader imgLoad;

    public AdapterSearch(Activity activity, Users users) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.users = users;
        holder = new ViewHolder();
        imgLoad = new ImageLoader(currentActivity);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return users.get(position);
    }

    static class ViewHolder {
        TextView name_surname;
        TextView age;
        ImageView photo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View satirView, ViewGroup parent) {
        try {
            if (satirView == null) {
                satirView = mInflater.inflate(R.layout.item_search, null);
            }
            holder.name_surname = (TextView) satirView.findViewById(R.id.name_surname);
            holder.age = (TextView) satirView.findViewById(R.id.age);
            holder.photo = (ImageView) satirView.findViewById(R.id.photo);

            if (getItem(position).getPhoto() != null) {
                imgLoad.DisplayImage(ClassSession.SOCKET_URL + getItem(position).getPhoto(), holder.photo);
            }
            holder.name_surname.setText(getItem(position).getName() + " " + getItem(position).getLastname());
            holder.age.setText(getItem(position).getBirthDate());
            satirView.setTag(R.id.USER_ID, getItem(position).getUserID());
            satirView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newInt = new Intent(currentActivity, ProfileActivity.class);
                    newInt.putExtra("SenderId", view.getTag(R.id.USER_ID).toString());
                    currentActivity.startActivity(newInt);
                }
            });
            satirView.setTag(holder);
            return satirView;
        }catch (Exception ex) {return null;}
    }
}