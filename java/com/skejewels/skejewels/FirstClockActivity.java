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
import android.widget.NumberPicker;
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


public class FirstClockActivity extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    TimePicker time_picker; //Instantiated in onCreate()
    Resources system;
    private Button nextButton;
    private String eventName;
    private int month = 6; //which month are we looking at
    private int day = 24;//which day of the month are we looking at
    private int year = 2015;
    private Button title;
    private TextView searchText, requestText, notificationText;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_clock);

        nextButton = (Button) findViewById(R.id.nextistButton);
        nextButton.setOnClickListener(this);

        time_picker = (TimePicker) findViewById(R.id.FirstClock);
        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventName = extras.getString("specificEventName", "Random Event");
            day = extras.getInt("eventStartDay", 0);
            month = extras.getInt("eventStartMonth", 0);
            year = extras.getInt("eventStartYear", 0);
        }

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
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
                Intent intent8 = new Intent(this, SecondClockActivity.class);
                intent8.putExtra("EventName", eventName);
                intent8.putExtra("eventStartDay", day);
                intent8.putExtra("eventStartMonth", month);
                intent8.putExtra("eventStartYear", year);
                intent8.putExtra("StartingHour", time_picker.getCurrentHour() );
                intent8.putExtra("StartingMinute", time_picker.getCurrentMinute());
                startActivity(intent8);
            break;
        }
    }

    @Override
    public void onFragmentInteraction(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}
