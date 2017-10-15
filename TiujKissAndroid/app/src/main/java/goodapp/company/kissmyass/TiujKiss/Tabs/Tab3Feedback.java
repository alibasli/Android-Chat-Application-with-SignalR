package goodapp.company.kissmyass.TiujKiss.Tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import goodapp.company.kissmyass.Adapters.AdapterChats;
import goodapp.company.kissmyass.Adapters.AdapterFeedback;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.TiujKiss.Activities.MainActivity;
import goodapp.company.kissmyass.TiujKiss.Activities.ProfileActivity;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.chats;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.feedbacks;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.messages;

/**
 * Created by mehme on 14.07.2016.
 */
public class Tab3Feedback extends Fragment {
    private AdapterFeedback adaptor;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab3_feedback, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateFeedbacks();
    }

    public void populateFeedbacks() {
        try {
            if (adaptor != null)
                adaptor.notifyDataSetChanged();
            else if (!feedbacks.isEmpty()) {
                listView = (ListView) currentActivity.findViewById(R.id.feedback_list);
                adaptor = new AdapterFeedback(currentActivity, feedbacks);
                listView.setAdapter(adaptor);
                listView.setOnItemClickListener(itemClick);
            }
        } catch (Exception NPE) {
            //       Toast.makeText(currentActivity,NPE.toString(),Toast.LENGTH_LONG).show();
        }
    }

    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                 /*   MainActivity mainActivityActivity = (MainActivity) mActivity;
                    if(!mainActivityActivity.httpRequest("Api/ChangeFeedbackStatus/" + view.getTag(R.id.FEEDBACK_ID).toString() , "GET"))
                        Toast.makeText(currentActivity,"Error seen feedback status",Toast.LENGTH_LONG).show();
*/
            try {
                HubConnection.setFeedbackSeenStatus(view.getTag(R.id.FEEDBACK_ID).toString())
                        .done(new Action<Boolean>() {
                            @Override
                            public void run(final Boolean status) {
                                currentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (status) {
                                            feedbacks.removeByFeedbackId(view.getTag(R.id.FEEDBACK_ID).toString());
                                            onResume();
                                        } else {
                                        }
                                        Intent newInt = new Intent(currentActivity, ProfileActivity.class);
                                        newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
                                        currentActivity.startActivity(newInt);
                                    }
                                });

                            }
                        });
            } catch (Exception ex) {
                Intent newInt = new Intent(currentActivity, ProfileActivity.class);
                newInt.putExtra("SenderId", view.getTag(R.id.SENDER_ID).toString());
                currentActivity.startActivity(newInt);
            }
        }

    };

/*    private void getFeedbacks(){
        // progressDialog.setMessage("loading chats...");
        //  progressDialog.show();
        HubConnection.getFeedbacks(currentUser.UserID).done(new Action<Feedbacks>()
        {
            @Override
            public void run(final Feedbacks feedbacks)
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawChats(feedbacks);
                    }
                });

            }
        });
    }
    private void drawChats(Feedbacks feedbacks){
        try {
            final ListView feedback_list = (ListView) mView.findViewById(R.id.feedback_list);
            AdapterFeedback adaptorumuz = new AdapterFeedback(getActivity(), feedbacks);
            feedback_list.setAdapter(adaptorumuz);
        }catch (Exception ex){
            Toast.makeText(currentActivity,"Draw chats error!",Toast.LENGTH_LONG).show();
        }
    }*/
    /*
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            requesterToServer();

        }
    };
    private void requesterToServer() {
        JSONObject login = new JSONObject();
        try {
             String userId = sharedpreferences.getString(UserSession.Id, "");
            if (userId.equals("")) {
                mMainActivityActivity.showProgress(false);
                return;
            }
            login.put("userId", userId);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/feedback", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                drawFeedback(response.body);
                            } else {
                                showFailedView("feedback/requesterToServer " + response.code + " Error!");
                                //Toast.makeText(getActivity().getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                            mMainActivityActivity.showProgress(false);
                        }
                    }).execute();
        } catch (JSONException e) {
            mMainActivityActivity.showProgress(false);
            showFailedView("feedback/requesterToServer Error!");
            //throw new RuntimeException(e);
        }

    }
    private void drawFeedback(String jsonString) {
        feedbackList.clear();
        try {
            if(jsonString.equals(null) || jsonString.equals("[]") || jsonString.equals("")){
                showFailedView(getString(R.string.no_feedback_text));
                return;
            }
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                feedbackList.add(new Feedback(
                        jsonObject.getString("NotificationID"),
                        jsonObject.getString("SharingID"),
                        jsonObject.getString("FromUserID"),
                        jsonObject.getString("Name"),
                        jsonObject.getString("LastName"),
                        jsonObject.getString("ProfilePhoto").equals("null") ? null : Base64.decode(jsonObject.getString("ProfilePhoto"), Base64.DEFAULT),
                        jsonObject.getString("Type"),
                        jsonObject.getString("DateSending"),
                        jsonObject.getBoolean("SeenStatus")
                ));
            }
            final ListView feedback_list = (ListView) mView.findViewById(R.id.feedback_list);
            AdapterFeedback adaptorumuz = new AdapterFeedback(getActivity(), feedbackList);
            feedback_list.setAdapter(adaptorumuz);

            handler.postDelayed(myRunnable, 10000);
        } catch (Exception ex) {
            showFailedView("drawFeedback jsonObject Error!");
        }

    }
    public void showFailedView(String text){
        ListView feedback_list = (ListView) mView.findViewById(R.id.feedback_list);
        feedback_list.setVisibility(View.GONE);
        View view_failed = mView.findViewById(R.id.view_failed);
        view_failed.setVisibility(View.VISIBLE);
        TextView txt_failed = (TextView) mView.findViewById(R.id.txt_failed);
        txt_failed.setText(text);
    }

*/
}
