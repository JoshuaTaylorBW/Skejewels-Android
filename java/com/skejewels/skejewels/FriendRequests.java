package com.skejewels.skejewels;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

/**
 * Created by j80ma_000 on 3/20/2016.
 */
public class FriendRequests extends AppCompatActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, AdapterViewCompat.OnItemSelectedListener {
    private Toolbar toolbar;
    
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    public void onClick(View view) {

    }

    public void onFragmentInteraction(View v) {

    }

    public void onItemSelected(AdapterViewCompat<?> parent, View view, int position, long id) {

    }

    public void onNothingSelected(AdapterViewCompat<?> parent) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
