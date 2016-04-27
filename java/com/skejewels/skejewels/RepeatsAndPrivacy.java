package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;


public class RepeatsAndPrivacy extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, OnItemSelectedListener   {

    Resources system;
    TextView untilText;
    DatePicker calendar;
    Spinner repeatType;
    Button NextButton;
    private String eventName;
    private int startingHour;
    private int startingMinute;
    private int endingHour;
    private int endingMinute;
    private int repeatEndDay;
    private int repeatEndMonth;
    private int repeatEndYear;
    private String typeOfRepeat;
    private int month = 6; //which month are we looking at
    private int day = 24;//which day of the month are we looking at
    private int year = 2015;
    private Intent restartIntent;

    private Button title;
    private TextView searchText, requestText, notificationText;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeats_and_privacy);

        untilText = (TextView) findViewById(R.id.untilText);
        calendar = (DatePicker) findViewById(R.id.datePicker);
        repeatType = (Spinner) findViewById(R.id.spinner);
        repeatType.setOnItemSelectedListener(this);
        NextButton = (Button) findViewById(R.id.nextistButton);
        NextButton.setOnClickListener(this);
        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);
        restartIntent = new Intent(this, Skejewels.class);
        typeOfRepeat = "Once";

        untilText.setVisibility(View.GONE);
        calendar.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventName = extras.getString("EventName", "Random Event");
            startingHour = extras.getInt("StartingHour", 1);
            startingMinute = extras.getInt("StartingMinute", 1);
            endingHour = extras.getInt("EndingHour", 1);
            endingMinute = extras.getInt("EndingMinute", 1);
            day = extras.getInt("eventStartDay", 0);
            month = extras.getInt("eventStartMonth", 0);
            year = extras.getInt("eventStartYear", 0);
        }


    }

    public void onClick(View v) {
        switch (v.getId()){
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

            case R.id.nextistButton:
                repeatEndDay = calendar.getDayOfMonth();
                repeatEndMonth = calendar.getMonth() + 1;
                repeatEndYear = calendar.getYear();
                Log.d("TAG", repeatEndDay + " " + repeatEndMonth + " " + repeatEndYear);
                new task().execute();
                break;
            case R.id.spinner:
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parentView,View v,int position,long id){
        String strAge=(String)repeatType.getItemAtPosition(position);
        if(strAge.equals("Weekly") || strAge.equals("Monthly")){
            untilText.setVisibility(View.VISIBLE);
            calendar.setVisibility(View.VISIBLE);
            typeOfRepeat = strAge;
            repeatEndDay = calendar.getDayOfMonth();
            repeatEndMonth = calendar.getMonth();
            repeatEndYear = calendar.getYear();
            Log.d("TAG", repeatEndDay + " " + repeatEndMonth + " " + repeatEndYear);
        }else{
            untilText.setVisibility(View.GONE);
            calendar.setVisibility(View.GONE);
            typeOfRepeat = strAge;
            repeatEndDay = 0;
            repeatEndMonth = 0;
            repeatEndYear = 0;
        }
    }

     class task extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(RepeatsAndPrivacy.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {

            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params){
            //TODO get day
            String newEventName = eventName.replaceAll("\\s+", "JambaSlammerCameraManForNothingEverMattered");
            String url_select="http://skejewels.com/Android/SQLAdd.php?n='" + newEventName +
            "'&sM=" + month + "&sD=" + day + "&sY=" + year + "&sH=" + startingHour + "&sMi=" + startingMinute +
            "&eM=" + 10 + "&eD=" + 15 + "&eH=" + endingHour   + "&eMi=" + endingMinute +
            "&rT=" + typeOfRepeat + "&eRM=" + repeatEndMonth + "&eRD="+repeatEndDay + "&eRY=" + repeatEndYear;
            Log.d("Fuck That Man", url_select);

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
                }
                is.close();
                result=sb.toString();

                Log.d("Repate", "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }
        protected void onPostExecute(Void v){
            try {

                startActivity(restartIntent);


                this.progressDialog.dismiss();


            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFragmentInteraction(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}
