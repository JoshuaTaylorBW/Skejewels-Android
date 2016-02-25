package com.skejewels.skejewels;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

/**
 * Created by j80ma_000 on 1/1/2016.
 */
public class EditEventSecondClock  extends ActionBarActivity implements OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {


    TimePicker time_picker;
    private Button nextButton;


    public String eventId;
    public String newEventName;
    public String newBeginningHour;
    public String newBeginningMinute;
    public String oldEndingTime;
    public String oldRepeatType;
    public String oldEventVisibility;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_edit_clock);

        nextButton = (Button) findViewById(R.id.nexterButton);
        nextButton.setOnClickListener(this);


        time_picker = (TimePicker) findViewById(R.id.FirstClock);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventId = extras.getString("eventId", "no event id");
            newEventName = extras.getString("newEventName", "Random Event");
            newBeginningHour = extras.getString("newStartingHour", "eventStartTime not existent bro 1");
            newBeginningMinute = extras.getString("newStartingMinute", "newStartingMinute not existent bro");
            oldEndingTime = extras.getString("eventEndTime", "eventEndTime does not exist bro");
            oldRepeatType = extras.getString("eventRepeat", "eventRepeat does not exist bro");
            oldEventVisibility = extras.getString("eventVisibility", "eventVisibility does not exist bro");
        }

        String[] oldBeginning = oldEndingTime.split(" ")[1].split(":");

        Log.d("second clock FIRST AND", oldBeginning[0]);
        Log.d("second clock FIRST AND", oldBeginning[1]);
        time_picker.setCurrentHour(Integer.parseInt(oldBeginning[0]));
        time_picker.setCurrentMinute(Integer.parseInt(oldBeginning[1]));

    }

    public void onClick(View view) {
switch(view.getId()) {
            case R.id.nexterButton:
                Intent intent = new Intent(this, EditRepeatsAndPrivacy.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("newEventName", newEventName);
                intent.putExtra("newStartingHour", newBeginningHour);
                intent.putExtra("newStartingMinute", newBeginningMinute);
                intent.putExtra("newEndingHour", time_picker.getCurrentHour().toString());
                intent.putExtra("newEndingMinute", time_picker.getCurrentMinute().toString());
                startActivity(intent);
            break;
        }
    }

    public void onFragmentInteraction(View v) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
