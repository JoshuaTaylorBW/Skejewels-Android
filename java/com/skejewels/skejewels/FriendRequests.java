package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
 * Created by j80ma_000 on 3/20/2016.
 */
public class FriendRequests extends AppCompatActivity implements NavigationDrawerFragment.OnFragmentInteractionListener, View.OnClickListener, View.OnTouchListener{
    private Toolbar toolbar;
    private TextView addBox, nicknameBox, wantsToText;
    private Button acceptButton, declineButton;
    private int lastBoxId;
    private int  userId = 188;
    private RelativeLayout layout;
    private RelativeLayout.LayoutParams acceptButtonParams, declineButtonParams, mainParams, nicknameParams, wantsToParams;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        layout = (RelativeLayout)findViewById(R.id.cardholder);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        new task().execute();
        lastBoxId = R.id.textView4;

    }

    public void removeFirstBox(){
        TextView first = (TextView) findViewById(R.id.textView4);

        TextView second = (TextView) findViewById(R.id.textView5);
        TextView third = (TextView) findViewById(R.id.textView6);
        Button fourth = (Button) findViewById(R.id.button);
        Button fifth = (Button) findViewById(R.id.button2);
        first.setVisibility(View.INVISIBLE);
        second.setVisibility(View.INVISIBLE);
        third.setVisibility(View.INVISIBLE);
        fourth.setVisibility(View.INVISIBLE);
        fifth.setVisibility(View.INVISIBLE);
    }

    public void makeFirstCard(String usersId, String UsersName, String UsersNickname){
        TextView first = (TextView) findViewById(R.id.textView4);
        TextView second = (TextView) findViewById(R.id.textView5);
        TextView third = (TextView) findViewById(R.id.textView6);
        Button fourth = (Button) findViewById(R.id.button);
        Button fifth = (Button) findViewById(R.id.button2);

        first.setText(UsersName);
        third.setText("@" + UsersNickname);


    }

    public void makeCard(String usersId, String UsersName, String UsersNickname){
        mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainParams.setMargins((int)pxFromDp(getApplicationContext(), 2),(int)pxFromDp(getApplicationContext(), 10),(int)pxFromDp(getApplicationContext(), 5), 0);
        mainParams.addRule(RelativeLayout.BELOW, lastBoxId);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        addBox = new TextView(FriendRequests.this);
        addBox.setBackgroundColor(Color.WHITE);
        addBox.setTextColor(Color.parseColor("#009688"));
        addBox.setGravity(Gravity.CENTER_HORIZONTAL);
        addBox.setText(UsersName);
        addBox.setId(View.generateViewId());
        addBox.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 125));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addBox.setElevation(2);
        }
        layout.addView(addBox, mainParams);
        lastBoxId = addBox.getId();

        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        nicknameParams.setMargins(0, (int)pxFromDp(getApplicationContext(), 21), 0, 0);
        nicknameParams.addRule(RelativeLayout.ALIGN_TOP, lastBoxId);
        nicknameParams.addRule(Gravity.CENTER);
        nicknameBox = new TextView(FriendRequests.this);
        nicknameBox.setTextColor(Color.parseColor("#88A5C4"));
        nicknameBox.setTextSize(13);
        nicknameBox.setGravity(Gravity.CENTER_HORIZONTAL);
        nicknameBox.setText("@" + UsersNickname);
        nicknameBox.setId(View.generateViewId());
        nicknameBox.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 125));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nicknameBox.setElevation(4);
        }
        layout.addView(nicknameBox, nicknameParams);

        wantsToParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        wantsToParams.setMargins(0, (int)pxFromDp(getApplicationContext(), 21), 0, 0);
        wantsToParams.addRule(RelativeLayout.ALIGN_TOP, nicknameBox.getId());
        wantsToParams.addRule(Gravity.CENTER);
        wantsToText = new TextView(FriendRequests.this);
        wantsToText.setTextColor(Color.parseColor("#000000"));
        wantsToText.setTextSize(13);
        wantsToText.setGravity(Gravity.CENTER_HORIZONTAL);
        wantsToText.setText("Wants to be your \n friend");
        wantsToText.setId(View.generateViewId());
        wantsToText.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 0));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wantsToText.setElevation(4);
        }
        layout.addView(wantsToText, wantsToParams);

        declineButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        declineButtonParams.addRule(RelativeLayout.BELOW, wantsToText.getId());
        declineButtonParams.addRule(RelativeLayout.ALIGN_START, R.id.button2);
        declineButton = new Button(FriendRequests.this);
        declineButton.setTextColor(Color.parseColor("#494949"));
        declineButton.setText("Decline");
        declineButton.setBackgroundResource(R.drawable.button_border);
        declineButton.setGravity(Gravity.CENTER_HORIZONTAL);
        //declineButtonParams.setMargins(0,0,(int)pxFromDp(getApplicationContext(), 30),0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            declineButton.setElevation(0);
        }
        layout.addView(declineButton, declineButtonParams);

        acceptButtonParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        acceptButtonParams.addRule(RelativeLayout.BELOW, wantsToText.getId());
        acceptButtonParams.addRule(RelativeLayout.ALIGN_START, R.id.textView5);
        acceptButtonParams.addRule(RelativeLayout.ALIGN_END, R.id.textView5);
        acceptButtonParams.setMargins((int)pxFromDp(getApplicationContext(), 20),0,0,0);
        acceptButton = new Button(FriendRequests.this);
        acceptButton.setTextColor(Color.parseColor("#ffffff"));
        acceptButton.setText("Accept");
        acceptButton.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            acceptButton.setElevation(0);
        }
        layout.addView(acceptButton, acceptButtonParams);


    }

    public void onClick(View view) {

    }

    public void onFragmentInteraction(View v) {

    }

    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(FriendRequests.this);
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

            String url_select = "http://skejewels.com/Android/AndroidFriendRequests.php?id=" + userId;
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

                Log.d("Friend_Requests", "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {
            try {
                String[] indivs = result.split("pampurppampurpampurp");
                Log.d("Friends Length", indivs.length + " is how many friend requests length is");
                if(indivs.length == 1){
                    removeFirstBox();
                }else {
                    for (int i = 0; i < indivs.length - 1; i += 4) {
                        if(i == 0){
                            makeFirstCard(indivs[i + 1], indivs[i + 2], indivs[i + 3]);
                        }else {
                            makeCard(indivs[i + 1], indivs[i + 2], indivs[i + 3]);
                        }
                    }
                }

                this.progressDialog.dismiss();



            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
