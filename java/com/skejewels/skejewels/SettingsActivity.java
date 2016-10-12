package com.skejewels.skejewels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

/**
 * Created by joshsparks on 8/16/16.
 */
public class SettingsActivity extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {

    private TextView name, username, email, phoneNumber;
    private Button logout;
    private CheckBox privateAccountCheckbox;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        logout = (Button) findViewById(R.id.LogoutButton);
        logout.setOnClickListener(this);

        declarations();
    }

    public void declarations(){
        name = (TextView) findViewById(R.id.NameText);
        username = (TextView) findViewById(R.id.UsernameText);
        email = (TextView) findViewById(R.id.EmailText);
        phoneNumber = (TextView) findViewById(R.id.PhoneText);
        logout = (Button) findViewById(R.id.LogoutButton);
        privateAccountCheckbox = (CheckBox) findViewById(R.id.PublicCheckBox);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.LogoutButton:
               removePreferences();
            break;
        }
    }
    public void removePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent i = new Intent(getApplicationContext(), SignIn.class);
        startActivity(i);
    }

    public void onFragmentInteraction(View v) {

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
