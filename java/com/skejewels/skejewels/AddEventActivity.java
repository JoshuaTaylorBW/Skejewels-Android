package com.skejewels.skejewels;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;


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

        eventNameEditor = (EditText) findViewById(R.id.EventNameEditor);
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
