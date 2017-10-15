package goodapp.company.kissmyass.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import goodapp.company.kissmyass.Models.Messages;
import goodapp.company.kissmyass.Models.Message;
import goodapp.company.kissmyass.R;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;

/**
 * Created by mehme on 24.06.2016.
 */
public class AdapterMessage extends BaseAdapter {

    private LayoutInflater mInflater;
    private Messages messages;
    private Message message;
    private ViewHolder holder;

    public AdapterMessage(Activity activity, Messages messages) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messages = messages;
        holder = new ViewHolder();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView other_message;
        TextView own_message;
        TextView other_sent_date;
        TextView own_sent_date;
    }
    @Override
    public View getView(int position, View satirView, ViewGroup parent) {
        if(satirView == null){
            satirView = mInflater.inflate(R.layout.item_message, null);
        }
        message = getItem(position);
        holder.other_message = (TextView) satirView.findViewById(R.id.other_message);
        holder.own_message = (TextView) satirView.findViewById(R.id.own_message);
        holder.other_sent_date = (TextView) satirView.findViewById(R.id.other_date);
        holder.own_sent_date = (TextView) satirView.findViewById(R.id.own_date);

        if (message.getSenderId().equals(currentUser.getUserID())) {
            holder.own_message.setText(message.getContent());
            holder.own_sent_date.setText(message.getSentDate());
            holder.other_message.setVisibility(View.GONE);
            holder.other_sent_date.setVisibility(View.GONE);
        } else {
            holder.other_message.setText(message.getContent());
            holder.other_sent_date.setText(message.getSentDate());
            holder.own_sent_date.setVisibility(View.GONE);
            holder.own_message.setVisibility(View.GONE);
        }

        satirView.setTag(R.id.MESSAGE_ID , message.getMessageID());
        satirView.setTag(R.id.MESSAGE_CONTENT , message.getContent());
/*
        final CharSequence MessageDetail[] = new CharSequence[]{
                currentActivity.getString(R.string.sent_date) + message.getSentDate(),
                currentActivity.getString(R.string.delivered_date) + (message.getDeliveredStatus() ? message.getDeliveredDate() : " - "),
                currentActivity.getString(R.string.seen_date) + (message.getSeenStatus() ? message.getSeenDate() : " - "),
                currentActivity.getString(R.string.cancel)
        };

        satirView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MessageActivity msgActivity = (MessageActivity) currentActivity;
                final String MsgID = view.getTag(R.id.MESSAGE_ID).toString();
                final String MsgContent = view.getTag(R.id.MESSAGE_CONTENT).toString();
                CharSequence OptionMenu[] = new CharSequence[] {currentActivity.getString(R.string.message_detail),
                        currentActivity.getString(R.string.delete_message), currentActivity.getString(R.string.copy_text),
                        currentActivity.getString(R.string.cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(currentActivity.getString(R.string.choose_event));
                builder.setItems(OptionMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
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
                            case 1:
                                msgActivity.deleteMessage(MsgID , vPosition);
                                break;
                            case 2:
                                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) currentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setText(MsgContent);
                                } else {
                                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) currentActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", MsgContent);
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
            }
        });
        */
        satirView.setTag(holder);
        return satirView;
    }
}