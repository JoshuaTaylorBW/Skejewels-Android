package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basiccalc.slidenerdtut.Skejewels;
import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FriendsIndividualEvent extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {
    private static final String TAG = IndividualDayActivity.class.getSimpleName();

    private Toolbar toolbar;
    private String friendsName = "Terrence Mullen";
    private String eventName = "";
    private String eventDate = "";
    private String eventTime = "";
    private int userId = 188;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //private int numEventId = 0;
    private String eventId = "0";
    private Intent changeIntent;
    private Button title;
    private TextView searchText, requestText, notificationText, settingsText;
    private TextView eventExplanation, likeAmount, firstCommentName, secondCommentName, firstComment, secondComment, totalCommentAmount;
    private Button likeButton, commentButton, editButton, homeButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_individual_event);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //Declare all TextViews
        eventExplanation = (TextView) findViewById(R.id.EventExplanation);
        likeAmount = (TextView) findViewById(R.id.LikeAmount);
        firstCommentName = (TextView) findViewById(R.id.FirstCommentName);
        secondCommentName = (TextView) findViewById(R.id.SecondCommentName);
        firstComment = (TextView) findViewById(R.id.FirstComment);
        secondComment = (TextView) findViewById(R.id.SecondComment);
        totalCommentAmount = (TextView) findViewById(R.id.TotalCommentAmount);
        //Declare all Buttons
        likeButton = (Button) findViewById(R.id.LikeButton);
        editButton = (Button) findViewById(R.id.EditButton);
        homeButton = (Button) findViewById(R.id.homeButton);

        setId();

        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);

        settingsText = (TextView) findViewById(R.id.setting_text);
        settingsText.setOnClickListener(this);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId", "ALMOST ANYTHING");
            friendsName = extras.getString("friendsName", "");
            eventName = extras.getString("eventName", "an event");
            eventDate = extras.getString("eventDate", "an event");
            eventTime = extras.getString("eventTime", "1:00AM - 1:00AM");
        }

        eventExplanation.setText(friendsName + " is going to " + eventName + " On " + eventDate + " From " + eventTime.replaceAll("(\\r|\\n)", ""));

        new task().execute();

        totalCommentAmount.setText("");
        changeIntent = new Intent(this, IndividualDayAllComments.class);
    }

    public void onFragmentInteraction(View v) {

    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                Intent intent = new Intent(this, Skejewels.class);
                startActivity(intent);
                break;
            case R.id.notification_text:
                Intent intent3 = new Intent(this, Notifications.class);
                startActivity(intent3);
                break;
            case R.id.search_text:
                Intent intent2 = new Intent(this, Search.class);
                startActivity(intent2);
                break;
            case R.id.request_text:
                Log.d(TAG, "this is being clicked");
                Intent intent4 = new Intent(this, FriendRequests.class);
                startActivity(intent4);
                break;
            case R.id.setting_text:
                Intent intent5 = new Intent(this, SettingsActivity.class);
                startActivity(intent5);
                break;
        }
    }

    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(FriendsIndividualEvent.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }

        protected Void doInBackground(String... params) {

            String url_select = "http://skejewels.com/Android/LikeAmount.php?id=" + eventId;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();


            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                    String[] values = line.split("-?-");


                }
                is.close();
                result = sb.toString();

                Log.d(TAG, "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        private int eventNumber = 0;

        protected void onPostExecute(Void v) {
            try {
                String[] indivs = result.split(",");
                if (Integer.parseInt(indivs[1]) > 0) {
                    likeAmount.setText(indivs[1] + " Likes");
                }
                if (Integer.parseInt(indivs[2]) > 0) {
                    new task2().execute();
                }
                Log.d(TAG, Integer.toString(indivs.length));

                this.progressDialog.dismiss();

            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

    class task2 extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(FriendsIndividualEvent.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task2.this.cancel(true);
                }
            });
        }

        protected Void doInBackground(String... params) {

            String url_select = "http://skejewels.com/Android/printEventComments.php?EventId=" + eventId;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();


            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                    String[] values = line.split("-?-");


                }
                is.close();
                result = sb.toString();


            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        private int eventNumber = 0;

        protected void onPostExecute(Void v) {
            try {
                String[] indivs = result.split("pampurppampurpampurp");
                for (int i = 0; i < indivs.length; i++) {
                    Log.d(TAG + "Second", "" + i + "=" + indivs[i]);
                }
                int amountOfComments = Integer.parseInt(indivs[1]);
                if (amountOfComments >= 1) {
                    makeFirstComment(indivs[indivs.length - 7], indivs[indivs.length - 6], indivs[indivs.length - 5]);
                }
                if (amountOfComments >= 2) {
                    makeSecondComment(indivs[indivs.length - 4], indivs[indivs.length - 3], indivs[indivs.length - 2]);
                }
                if (amountOfComments > 2) {
                    makeRestOfCommentsLink(Integer.parseInt(indivs[1]));
                }
                //Comment = i
                //Commenter Name = i+1
                //Commenter Id = i+2


                Log.d(TAG, Integer.toString(indivs.length));

                this.progressDialog.dismiss();

            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

    public void makeFirstComment(String comment, String commenterName, String commenterId) {
        firstComment.setText(comment);
        firstCommentName.setText(commenterName);
        firstCommentName.setHint(commenterId);
    }

    public void makeSecondComment(String comment, String commenterName, String commenterId) {
        secondComment.setText(comment);
        secondCommentName.setText(commenterName);
        secondCommentName.setHint(commenterId);
    }

    public void makeRestOfCommentsLink(int amountOfComments) {
        totalCommentAmount.setText("View All " + amountOfComments + " Comments");
        totalCommentAmount.setHint(eventId);
        totalCommentAmount.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                changeIntent.putExtra("eventID", eventId);
                startActivity(changeIntent);
            }
        });
    }

    private void setId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultValue = "NO ID";
        userId = Integer.parseInt(sharedPreferences.getString("current_user_id", defaultValue));
    }
}
