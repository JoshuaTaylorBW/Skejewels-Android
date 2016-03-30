package com.skejewels.skejewels;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

/**
 * Created by j80ma_000 on 3/29/2016.
 */
public class Notifications extends AppCompatActivity implements NavigationDrawerFragment.OnFragmentInteractionListener, View.OnClickListener, View.OnTouchListener{
    private Toolbar toolbar;
    private TextView addBox, nicknameBox, wantsToText;
    private int lastBoxId;
    private RelativeLayout layout;
    private RelativeLayout.LayoutParams acceptButtonParams, declineButtonParams, mainParams, nicknameParams, wantsToParams;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        layout = (RelativeLayout)findViewById(R.id.cardholder);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //new task().execute();
        lastBoxId = R.id.textView4;
        makeCard("1","Josh Sparks", "JoshuaTaylor8", "Wants to fuck you");
        makeCard("1","Josh Sparks", "JoshuaTaylor8", "Wants to fuck you");
        makeCard("1","Josh Sparks", "JoshuaTaylor8", "Wants to fuck you");

    }

    public void makeCard(String usersId, String UsersName, String UsersNickname, String message){
        mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainParams.setMargins((int)pxFromDp(getApplicationContext(), 2),(int)pxFromDp(getApplicationContext(), 10),(int)pxFromDp(getApplicationContext(), 5), 0);
        mainParams.addRule(RelativeLayout.BELOW, lastBoxId);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        mainParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        addBox = new TextView(Notifications.this);
        addBox.setBackgroundColor(Color.WHITE);
        addBox.setTextColor(Color.parseColor("#009688"));
        addBox.setGravity(Gravity.CENTER_HORIZONTAL);
        addBox.setText(UsersName);
        addBox.setId(View.generateViewId());
        addBox.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 62));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addBox.setElevation(2);
        }
        layout.addView(addBox, mainParams);
        lastBoxId = addBox.getId();

        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        nicknameParams.setMargins(0, (int)pxFromDp(getApplicationContext(), 21), 0, 0);
        nicknameParams.addRule(RelativeLayout.ALIGN_TOP, lastBoxId);
        nicknameParams.addRule(Gravity.CENTER);
        nicknameBox = new TextView(Notifications.this);
        nicknameBox.setTextColor(Color.parseColor("#88A5C4"));
        nicknameBox.setTextSize(13);
        nicknameBox.setGravity(Gravity.CENTER_HORIZONTAL);
        nicknameBox.setText("@" + UsersNickname);
        nicknameBox.setId(View.generateViewId());
        nicknameBox.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 125));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nicknameBox.setElevation(4);
        }
        layout.addView(nicknameBox, nicknameParams);

        wantsToParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        wantsToParams.setMargins(0, (int)pxFromDp(getApplicationContext(), 21), 0, 0);
        wantsToParams.addRule(RelativeLayout.ALIGN_TOP, nicknameBox.getId());
        wantsToParams.addRule(Gravity.CENTER);
        wantsToText = new TextView(Notifications.this);
        wantsToText.setTextColor(Color.parseColor("#000000"));
        wantsToText.setTextSize(13);
        wantsToText.setGravity(Gravity.CENTER_HORIZONTAL);
        wantsToText.setText("Wants to be your \n friend");
        wantsToText.setId(View.generateViewId());
        wantsToText.setPadding(0, 0, 0, (int)pxFromDp(getApplicationContext(), 0));//Set padding of box. (Left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wantsToText.setElevation(4);
        }
        layout.addView(wantsToText, wantsToParams);
    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public void onClick(View view) {

    }
    public void onFragmentInteraction(View v) {

    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
