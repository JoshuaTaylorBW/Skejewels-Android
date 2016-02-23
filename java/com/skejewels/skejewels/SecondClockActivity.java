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


public class SecondClockActivity extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    Resources system;
    TimePicker time_picker; //Instantiated in onCreate()
    private Button nextButton;
    private String eventName;
    private int startingHour;
    private int startingMinute;
    private int month = 6; //which month are we looking at
    private int day = 24;//which day of the month are we looking at
    private int year = 2015;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_clock);

        nextButton = (Button) findViewById(R.id.nexterButton);
        nextButton.setOnClickListener(this);

        time_picker = (TimePicker) findViewById(R.id.FirstClock);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventName = extras.getString("EventName", "Random Event");
            startingHour = extras.getInt("StartingHour", 1);
            startingMinute = extras.getInt("StartingMinute", 1);
            day = extras.getInt("eventStartDay", 0);
            month = extras.getInt("eventStartMonth", 0);
            year = extras.getInt("eventStartYear", 0);
        }

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nexterButton:
                Intent intent = new Intent(this, RepeatsAndPrivacy.class);
                 intent.putExtra("EventName", eventName);
                 intent.putExtra("StartingHour", startingHour);
                 intent.putExtra("StartingMinute", startingMinute);
                 intent.putExtra("EndingHour", time_picker.getCurrentHour());
                 intent.putExtra("EndingMinute", time_picker.getCurrentMinute());
                intent.putExtra("eventStartDay", day);
                intent.putExtra("eventStartMonth", month);
                intent.putExtra("eventStartYear", year);
                startActivity(intent);
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
