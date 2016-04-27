package com.skejewels.skejewels;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

/**
 * Created by j80ma_000 on 12/5/2015.
 */
public class SignUp extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    private Button signInButton, signUpButton;
    private EditText Username, Password;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        signInButton = (Button) findViewById(R.id.SignInButton);
        signUpButton = (Button) findViewById(R.id.SignUpButton);

        Username = (EditText) findViewById(R.id.UsernameField);
        Password = (EditText) findViewById(R.id.PasswordField);

//        nextButton = (Button) findViewById(R.id.nextButton);
//        nextButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.SignInButton:
                //new task.execute();
                break;
            case R.id.SignUpButton:
                //go to sign up page
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
