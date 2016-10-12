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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class IndividualDayActivity extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {
    private Toolbar toolbar;
    private static final String TAG = IndividualDayActivity.class.getSimpleName();

    private int userId = 188;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private ArrayList<Integer> ids;
    private ArrayList<String> times;

    private int month = 6; //which month are we looking at
    private int day = 24;//which day of the month are we looking at
    private int year = 2015;

    //BOXES
    LayoutParams firstLayout, otherLayout;//first layout is for the first Box and otherLayout is the layout for all other boxes.
    private int boxWidth = 300;
    private int boxHeight = 10;
    private TextView box, time;
    private LinearLayout layout;
    private LinearLayout.LayoutParams layoutParams, timeLayoutParams;
    private Button title;
    private TextView searchText, requestText, notificationText, settingsText;

    private TextView topDate;//date that appears at top of screen.

    private int lineAmount = 0, eventsMade = 0;
    Intent changeIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_day_look);

        setId();

        ids = new ArrayList<Integer>();
        times = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        changeIntent = new Intent(this, AddEventActivity.class);
        if (extras != null) {
            day = extras.getInt("Day", 1);
            month = extras.getInt("Month", 1);
            year = extras.getInt("Year", 1);
        }


        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        topDate = (TextView) findViewById(R.id.EventExplanation);
        topDate.setText(makeDateName());

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

        layout = (LinearLayout)findViewById(R.id.EventsLayout);

        addBoxes();
        //addAddBox();

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        new task().execute();
    }

    public String makeDateName(){
        String monthFullName = "";
        String dayFullName = "";
        switch (month - 1) {
            case 0:
                monthFullName = "January";
                break;
            case 1:
                monthFullName = "February";
                break;
            case 2:
                monthFullName = "March";
                break;
            case 3:
                monthFullName = "April";
                break;
            case 4:
                monthFullName = "May";
                break;
            case 5:
                monthFullName = "June";
                break;
            case 6:
                monthFullName = "July";
                break;
            case 7:
                monthFullName = "August";
                break;
            case 8:
                monthFullName = "September";
                break;
            case 9:
                monthFullName = "October";
                break;
            case 10:
                monthFullName = "November";
                break;
            case 11:
                monthFullName = "December";
                break;
        }
        if(day == 1 || day == 21 || day == 31){
            dayFullName = ""+day+"st";
        }else if(day == 2 || day == 22){
            dayFullName = ""+day+"nd";
        }else if(day == 3 || day == 23){
            dayFullName = ""+day+"rd";
        }else{
            dayFullName = ""+day+"th";
        }
        return monthFullName + " " + dayFullName;
    }
    TextView addBox;

    public void addAddBox(){
        layoutParams.setMargins(20, (int)pxFromDp(getApplicationContext(), 75), 20, 0);
        addBox = new TextView(IndividualDayActivity.this);//create new instance of box
        addBox.setTextSize(20);
        addBox.setBackgroundColor(Color.WHITE);
        addBox.setTextColor(Color.parseColor("#748AA5"));
        addBox.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        addBox.setText(wrapEventName("Add Event"));// at Valley View Christian Church
        addBox.setPaintFlags(addBox.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        addBox.setPadding(0, 100, 0, 100);//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addBox.setElevation(8);
        }
        layout.addView(addBox, layoutParams);//add the box to the layout

        addBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeIntent.putExtra("eventStartDay", day);
                changeIntent.putExtra("eventStartMonth", month);
                changeIntent.putExtra("eventStartYear", year);
                startActivity(changeIntent);
            }
        });

        time = new TextView(IndividualDayActivity.this);
        time.setTextSize(20);
        time.setTextColor(Color.WHITE);
        time.setText("");//Set time of event. This will be dynamic.
        layout.addView(time, timeLayoutParams);
        eventsMade++;//add one to eventsMade for positioning.
    }
    public void addBoxes(){
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        layoutParams.setMargins(20, 0, 20, 0);//Set initial positions
        timeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        timeLayoutParams.setMargins((int)pxFromDp(getApplicationContext(), 275), (int)pxFromDp(getApplicationContext(), -80), 0, 0);//Set initial positions

    }

    View.OnClickListener clicks=new View.OnClickListener() {
        public void onClick(View view) {
            TextView box = (TextView) view;
            for (int i = 0; i < ids.size(); i++){
                if(view.getId() == ids.get(i)){
                    goToEditActivity(box.getHint().toString(), box.getText().toString(), times.get(i));
                }
            }
        }
    };

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void makeEvent(String eventName, String eventTime, String eventId){
        layoutParams.setMargins(20, 0, 20, (int)pxFromDp(getApplicationContext(), 0));

        box = new TextView(IndividualDayActivity.this);//create new instance of box
        box.setTextSize(20);
        box.setBackgroundColor(Color.WHITE);
        box.setTextColor(Color.BLACK);
        box.setText(wrapEventName(eventName));// at Valley View Christian Church
        box.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        box.setPadding((int) pxFromDp(getApplicationContext(), -200), 100, 0, 100);//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            box.setElevation(8);
        }
        box.setHint(eventId);
        box.setId(View.generateViewId());
        ids.add(box.getId());
        layout.addView(box, layoutParams);//add the box to the layout

        box.setOnClickListener(clicks);

        time = new TextView(IndividualDayActivity.this);
        time.setTextSize(20);
        time.setTextColor(Color.parseColor("#8899A6"));
        time.setText(eventTime);//Set time of event. This will be dynamic.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            time.setElevation(8);
        }
        times.add(time.getText().toString());
        layout.addView(time, timeLayoutParams);
        eventsMade++;//add one to eventsMade for positioning.
    }
    public void goToEditActivity(String id, String name, String time){
        Intent intent = new Intent(this, IndividualEventActivity.class);
        intent.putExtra("eventId", id);
        intent.putExtra("eventName", name);
        intent.putExtra("eventDate", topDate.getText());
        intent.putExtra("eventTime", time);
        startActivity(intent);
    }

    public int getTextTopPadding(){
        int yPosition = 0;
        switch (lineAmount){
            case 1:
                yPosition = 0;
                break;
            case 2:
                yPosition = 0;
                break;
            case 3:
                yPosition = 100;
                break;
            case 4:
                yPosition = 40;
                break;
        }
        return yPosition;
    }
    public int getTimeTopPadding(){
        int yPosition = 0;
        return 500 + (eventsMade + 570);
    }
    public String wrapEventName(String s){
        StringBuilder sb = new StringBuilder(s);
        int i = 0;
        lineAmount = 1;
        while (i + 15 < sb.length() && (i = sb.lastIndexOf(" ", i + 15)) != -1) {
            sb.replace(i, i + 1, "\n");
            lineAmount++;
        }
        System.out.println(sb.toString());
        return sb.toString();
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
            case R.id.setting_text:
                Intent intent5 = new Intent(this, SettingsActivity.class);
                startActivity(intent5);
                break;
        }
    }


    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(IndividualDayActivity.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params){

            String url_select="http://skejewels.com/Android/PrintEventsOfDay.php?UserId=" + userId + "&Month=" + (month) + "&Day=" + day + "&Year=" + year;

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
                String[] indivs = result.split("-?-");
                Log.d(TAG, Integer.toString(indivs.length));
                for (int i = 3; i < indivs.length - 1; i+=5) {
                    String idAndName = indivs[5 * eventNumber + 2];
                    String[] idAndNameSplitter = idAndName.split("pampurppampurpampurp");
                    String eventName1 = idAndNameSplitter[0];
                    String eventId1 = idAndNameSplitter[1];
                    eventName1 = eventName1.replace("\\/", "/");
                    String eventTime1 = indivs[5 * eventNumber + 4] + "-\n" + indivs[5 * eventNumber + 5];
                    Log.d(TAG, "EventName:" + eventId1);
                    Log.d(TAG, "EventTime:" + eventTime1);

                    makeEvent(eventName1, eventTime1, eventId1);
                    eventNumber++;
                }
                addAddBox();
                this.progressDialog.dismiss();

            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
    }

    //WHATEVER STUFF.
    public void onFragmentInteraction(View v) {
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_individual_day, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void setId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultValue = "NO ID";
        userId = Integer.parseInt(sharedPreferences.getString("current_user_id", defaultValue));
    }
}
