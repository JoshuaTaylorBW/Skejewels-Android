package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class AddEventActivity extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    private Button nextButton;
    private EditText eventNameEditor;
    private String eventName;
    private int month = 6; //which month are we looking at
    private int day = 24;//which day of the month are we looking at
    private int year = 2015;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        eventNameEditor = (EditText) findViewById(R.id.eventNameEditor);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            day = extras.getInt("eventStartDay", 0);
            month = extras.getInt("eventStartMonth", 0);
            year = extras.getInt("eventStartYear", 0);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.nextButton:
                eventName = eventNameEditor.getText().toString();
                if(!eventName.matches("")) {
                    Intent intent = new Intent(this, FirstClockActivity.class);
                    intent.putExtra("eventStartDay", day);
                    intent.putExtra("eventStartMonth", month);
                    intent.putExtra("eventStartYear", year);
                    intent.putExtra("specificEventName", eventName);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onFragmentInteraction(int position) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
