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
public class EditEventFirstClock  extends ActionBarActivity implements OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {


    TimePicker time_picker;
    private Button nextButton;


    public String eventId;
    public String newEventName;
    public String oldBeginningTime;
    public String newBeginningTime;
    public String oldEndingTime;
    public String oldRepeaType;
    public String oldEventVisibility;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_edit_clock);

        nextButton = (Button) findViewById(R.id.nextistButton);
        nextButton.setOnClickListener(this);


        time_picker = (TimePicker) findViewById(R.id.FirstClock);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventId = extras.getString("eventId", "no event id");
            newEventName = extras.getString("newEventName", "Random Event");
            oldBeginningTime = extras.getString("eventStartTime", "not existent bro");
            oldEndingTime = extras.getString("eventEndTime", "eventEndTime does not exist bro");
            oldRepeaType = extras.getString("eventRepeat", "eventRepeat does not exist bro");
            oldEventVisibility = extras.getString("eventVisibility", "eventVisibility does not exist bro");
        }



        Log.d("First clock", oldBeginningTime);
        String[] oldBeginning = oldBeginningTime.split(" ")[1].split(":");
        time_picker.setCurrentHour(Integer.parseInt(oldBeginning[0]));
        time_picker.setCurrentMinute(Integer.parseInt(oldBeginning[1]));

        Log.d("first clock", eventId);
        Log.d("first clock", newEventName);
        Log.d("first clock", oldBeginningTime);
        Log.d("first clock", oldEndingTime);
        Log.d("first clock", oldRepeaType);
        Log.d("first clock", oldEventVisibility);
    }

    public void onClick(View view) {
switch(view.getId()) {
            case R.id.nextistButton:
                Intent intent = new Intent(this, EditEventSecondClock.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("newEventName", newEventName);
                intent.putExtra("newStartingHour", time_picker.getCurrentHour().toString());
                intent.putExtra("newStartingMinute", time_picker.getCurrentMinute().toString());
                intent.putExtra("eventEndTime", oldEndingTime);
                intent.putExtra("eventRepeat", oldRepeaType);
                intent.putExtra("eventVisibility", oldEventVisibility);
                startActivity(intent);
            break;
        }
    }

    public void onFragmentInteraction(int position) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
