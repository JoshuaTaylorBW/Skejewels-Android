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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.Date;


/**
 * Created by j80ma_000 on 1/16/2016.
 */
public class Search extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, OnItemSelectedListener {

    public CheckBox test;
    public CheckBox created;
    public TextView nickName;
    public RelativeLayout contentHolder;
    private RelativeLayout.LayoutParams checkParams, nicknameParams;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        contentHolder = (RelativeLayout) findViewById(R.id.contentHolder);
        makeSearchResult();
    }

    public void makeSearchResult(){
        checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        checkParams.addRule(RelativeLayout.BELOW, R.id.textView3);
        checkParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.textView3);
        checkParams.addRule(RelativeLayout.ALIGN_START, R.id.textView3);

        nicknameParams.addRule(RelativeLayout.BELOW, R.id.checkBox1);
        nicknameParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.checkBox1);
        nicknameParams.addRule(RelativeLayout.ALIGN_START, R.id.checkBox1);
        nicknameParams.setMargins(140, 0, 0, 0);

        created = new CheckBox(Search.this);
        created.setTextColor(Color.parseColor("#000000"));
        created.setBackgroundTintList(this.getResources().getColorStateList(R.color.primaryColor));
        created.setButtonTintList(this.getResources().getColorStateList(R.color.primaryColor));
        created.setText("fuck this");

        nickName = new TextView(Search.this);
        nickName.setTextAppearance(this, android.R.style.TextAppearance_Small);
        created.setTextColor(Color.parseColor("#888888"));
        nickName.setText("Nick Name");

        contentHolder.addView(created, checkParams);    
        contentHolder.addView(nickName, nicknameParams);    


    }

    public void onClick(View view) {

    }

    public void addResults(){
        checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        checkParams.setMargins(0, 0, 0, 0);//Set initial positions
        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        nicknameParams.setMargins(0, -0, 0, 0);//Set initial positions
    }



    public void onCheckBoxClicked(View view) {


        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBox1:
                if (checked){
                    Log.d("FROM_SEARCH", "checked");    
                }else {
                    Log.d("FROM_SEARCH", "checked");
                }
                break;
        }
    }

    public void onFragmentInteraction(int position) {

    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
