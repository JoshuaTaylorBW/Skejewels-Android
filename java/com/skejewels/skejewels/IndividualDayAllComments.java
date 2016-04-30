package com.skejewels.skejewels;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ScrollView;
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



/**
 * Created by j80ma_000 on 10/15/2015.
 */
public class IndividualDayAllComments extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener{
    private static final String TAG = IndividualDayActivity.class.getSimpleName();

    private Toolbar toolbar;
    private int eventId = 157;
    private Intent changeIntent;
    private TextView firstCommentName, secondCommentName, firstComment, secondComment;
    private Button likeButton, commentButton, editButton, homeButton;
    private LinearLayout layout;
    private LinearLayout.LayoutParams layoutParams, commentLayoutParams;
    private TextView name, indivComment;
    private ScrollView scrollLayout;
    private Button title;
    private TextView searchText, requestText, notificationText;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_individual_day_comments);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventId = Integer.parseInt(extras.getString("eventID", "157"));
        }

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //Declare all TextViews
       firstCommentName = (TextView) findViewById(R.id.FirstCommentName);firstComment = (TextView) findViewById(R.id.FirstComment);
        //Declare all Buttons
        editButton = (Button) findViewById(R.id.EditButton); homeButton = (Button) findViewById(R.id.homeButton);
        scrollLayout = (ScrollView) findViewById(R.id.ScrollView01);

        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        changeIntent = new Intent(this, Skejewels.class);
        new task2().execute();
        layout = (LinearLayout)findViewById(R.id.EventsLayout);

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        layoutParams.setMargins(0, 30, 0, 0);//Set initial positions
        commentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        commentLayoutParams.setMargins(0, 0, 0, 0);//Set initial positions

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.homeButton:
                Log.d(TAG,  "GOT TO HERE :) :) :)");
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
        }
    }

    public void onFragmentInteraction(View v) {

    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    class task2 extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(IndividualDayAllComments.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task2.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params){

            String url_select="http://skejewels.com/Android/printEventComments.php?EventId=" + eventId;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is =  httpEntity.getContent();




            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line=br.readLine())!=null)
                {
                    sb.append(line + "\n");
                    String[] values = line.split("-?-");


                }
                is.close();
                result=sb.toString();

                Log.d(TAG, "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }
        private int eventNumber = 0;
        protected void onPostExecute(Void v){
            try {
                String[] indivs = result.split("pampurppampurpampurp");
                if(Integer.parseInt(indivs[1]) > 0) {
                    for (int i = 0; i < indivs.length; i++) {
                        Log.d(TAG + "Second", "" + i + "=" + indivs[i]);
                    }
                    int amountOfComments = Integer.parseInt(indivs[1]);
                    makeFirstComment(indivs[2], indivs[3], indivs[4]);

                    for (int i = 5; i < indivs.length - 1; i += 3) {
                        makeNextComment(indivs[i], indivs[i + 1], indivs[i + 2]);
                    }
                    scrollLayout.post(new Runnable() {
                        public void run() {
                            scrollLayout.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }else{
                    removeFirstComment();
                }
                this.progressDialog.dismiss();

            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
    }
    public void makeFirstComment(String comment, String commenterName, String commenterId){
        firstComment.setText(comment);
        firstCommentName.setText(commenterName);
        firstCommentName.setHint(commenterId);
    }
    public void removeFirstComment(){
        firstComment.setText("");
        firstCommentName.setText("");
        firstCommentName.setHint(0);
    }
    public void makeNextComment(String comment, String commenterName, String commenterId){
        name = new TextView(IndividualDayAllComments.this);
        name.setTextSize(18);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextColor(Color.parseColor("#009688"));
        name.setPadding(60, 100, 0, 15);//Set padding of box. (Left, top, right, bottom)
        name.setText(commenterName);//Set time of event. This will be dynamic.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            name.setElevation(8);
        }

        layout.addView(name, commentLayoutParams);

        layoutParams.setMargins(0, 0, 0, 0);
        indivComment = new TextView(IndividualDayAllComments.this);//create new instance of box
        indivComment.setTextSize(18);
        indivComment.setTextColor(Color.BLACK);
        indivComment.setText(comment);// at Valley View Christian Church
        indivComment.setPadding(60, 0, 0, 0);//Set padding of box. (Left, top, right, bottom)
        indivComment.setHint(commenterId);
        indivComment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d(TAG, "This is an ID " + (String) name.getHint());
            }
        });
        layout.addView(indivComment, layoutParams);//add the box to the layout



    }


}
