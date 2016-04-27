package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;
import com.basiccalc.slidenerdtut.Skejewels;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by j80ma_000 on 3/29/2016.
 */
public class Notifications extends AppCompatActivity implements NavigationDrawerFragment.OnFragmentInteractionListener, View.OnClickListener, View.OnTouchListener{
    private Toolbar toolbar;
    private TextView addBox, nicknameBox, wantsToText;
    private int lastBoxId;
    private RelativeLayout layout;
    private Button title;
    private TextView searchText, requestText, notificationText;
    private int  userId = 188;
    private RelativeLayout.LayoutParams acceptButtonParams, declineButtonParams, mainParams, nicknameParams, wantsToParams;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        layout = (RelativeLayout)findViewById(R.id.cardholder);
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

        new task().execute();
        lastBoxId = R.id.textView4;
    }
    public void removeFirstBox(){
        TextView first = (TextView) findViewById(R.id.textView4);
        TextView second = (TextView) findViewById(R.id.textView5);
        first.setVisibility(View.INVISIBLE);
        second.setVisibility(View.INVISIBLE);
    }

    public void makeFirstCard(String usersId, String UsersName, String message, String eventId){
        TextView first = (TextView) findViewById(R.id.textView4);
        TextView second = (TextView) findViewById(R.id.textView5);

        first.setText(UsersName);
        second.setText(message);

    }

    public void makeCard(String usersId, String UsersName, String message, String eventId){
        mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainParams.setMargins((int)pxFromDp(getApplicationContext(), 2),(int)pxFromDp(getApplicationContext(), 10),(int)pxFromDp(getApplicationContext(), 5), 0);
        mainParams.addRule(RelativeLayout.BELOW, lastBoxId);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        addBox = new TextView(Notifications.this);
        addBox.setBackgroundColor(Color.WHITE);
        addBox.setTextColor(Color.parseColor("#009688"));
        addBox.setGravity(Gravity.CENTER_HORIZONTAL);
        addBox.setText(UsersName);
        addBox.setId(View.generateViewId());
        addBox.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 62));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addBox.setElevation(2);
        }
        layout.addView(addBox, mainParams);
        lastBoxId = addBox.getId();

        wantsToParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        wantsToParams.setMargins(0, (int)pxFromDp(getApplicationContext(), 21), 0, 0);
        wantsToParams.addRule(RelativeLayout.ALIGN_TOP, addBox.getId());
        wantsToParams.addRule(Gravity.CENTER);
        wantsToText = new TextView(Notifications.this);
        wantsToText.setTextColor(Color.parseColor("#000000"));
        wantsToText.setTextSize(13);
        wantsToText.setGravity(Gravity.CENTER_HORIZONTAL);
        wantsToText.setText(message);
        wantsToText.setId(View.generateViewId());
        wantsToText.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 0));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wantsToText.setElevation(4);
        }
        layout.addView(wantsToText, wantsToParams);
    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public void onClick(View view) {
        switch(view.getId()) {
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
                Intent intent4 = new Intent(this, FriendRequests.class);
                startActivity(intent4);
                break;
        }
    }

    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(Notifications.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {

            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params) {

            String url_select = "http://skejewels.com/Android/AndroidNotifications.php?id=" + userId;
            Log.d("Friend_Requests", "" + url_select);

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
                }
                is.close();
                result = sb.toString();

                Log.d("Notifications", "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {
            try {
                //who commented's id, who commented's name, the comment, commentEventId;
                String indivs1 = result.replaceAll("\\?", "pampurp").replaceAll("\\^--\\^", "");
                Log.d("Notified Length", indivs1);
                String[] indivs = indivs1.split("pampurppampurp");
                Log.d("Friends Length", indivs.length + " is how many friend notifications length is");
                if(indivs.length == 1){
                    removeFirstBox();
                }else {
                    for(int x = 0; x < indivs.length - 1; x++){
                        String[] thisOne = indivs[x].split(",");
                        Log.d("Indiv Request", indivs[x]);
                        String note;
                        if (x == 0) {
                            if (thisOne.length == 5) {
                                makeFirstCard(thisOne[0], thisOne[1], thisOne[2] + "\n" + thisOne[3], thisOne[4]);
                            } else {
                                makeFirstCard(thisOne[0], thisOne[1], thisOne[2], thisOne[3]);
                            }
                        }else {
                            if (thisOne.length == 5) {
                                makeCard(thisOne[0], thisOne[1], thisOne[2] + "\n" + thisOne[3], thisOne[4]);
                            } else {

                                makeCard(thisOne[0], thisOne[1], thisOne[2], thisOne[3]);
                            }
                        }

//                                //makeFirstCard(indivs[i + 1], indivs[i + 2], indivs[i + 3]);
//                            }else {
//                                makeCard(indivs[i], indivs[i + 1], indivs[i + 2] + "\n" + indivs[i + 3], indivs[i+4]);
//                                //makeCard(indivs[i + 1], indivs[i + 2], indivs[i + 3]);
//                            }
//                        }
                    }

                }

                this.progressDialog.dismiss();



            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

        public void onFragmentInteraction(View v) {

    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
