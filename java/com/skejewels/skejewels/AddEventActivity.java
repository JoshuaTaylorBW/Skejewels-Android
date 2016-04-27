package com.skejewels.skejewels;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;
import com.basiccalc.slidenerdtut.Skejewels;


public class AddEventActivity extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    private Button nextButton;
    private EditText eventNameEditor;
    private Toolbar toolbar;
    private String eventName;
    private int month = 6; //which month are we looking at
    private int day = 24;//which day of the month are we looking at
    private int year = 2015;
    private Button title;
    private TextView searchText, requestText, notificationText;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);

        eventNameEditor = (EditText) findViewById(R.id.EventNameEditor);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            day = extras.getInt("eventStartDay", 0);
            month = extras.getInt("eventStartMonth", 0);
            year = extras.getInt("eventStartYear", 0);
        }
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
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
            case R.id.nextButton:
                eventName = eventNameEditor.getText().toString();
                if(!eventName.matches("")) {
                    Intent intent8 = new Intent(this, FirstClockActivity.class);
                    intent8.putExtra("eventStartDay", day);
                    intent8.putExtra("eventStartMonth", month);
                    intent8.putExtra("eventStartYear", year);
                    intent8.putExtra("specificEventName", eventName);
                    startActivity(intent8);
                }
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
