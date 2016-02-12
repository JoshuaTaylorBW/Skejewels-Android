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
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by j80ma_000 on 1/16/2016.
 */
public class    Search extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, OnItemSelectedListener {

    public CheckBox test;
    public CheckBox created;
    public TextView actualName, nickName;
    public RelativeLayout contentHolder;
    public EditText eventNameEditor;
    private String what;
    private ArrayList<String> ids;
    private ArrayList<Integer> nameIds; //used for onclicklistener
    private RelativeLayout.LayoutParams checkParams, nicknameParams, actualNameParams;
    private int lastNicknameId;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        lastNicknameId = R.id.textView3;
        contentHolder = (RelativeLayout) findViewById(R.id.contentHolder);
        eventNameEditor = (EditText) findViewById(R.id.EventNameEditor);
        ids = new ArrayList<String>();
        nameIds = new ArrayList<Integer>();
        eventNameEditor.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() != 0) {
                    what = eventNameEditor.getText().toString();
                    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(what);
                    boolean b = m.find();
                    Log.d("search", "it is: " + b);

                    Log.d("search", what);
                    new task().execute();
                } else {
                    for (int i = 0; i < ids.size(); i++) {
                        View myView = findViewById(Integer.parseInt(ids.get(i)));
                        contentHolder.removeView(myView);
                    }
                    lastNicknameId = R.id.textView3;
                }
            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    View.OnClickListener clicks=new View.OnClickListener() {
        public void onClick(View view) {
            TextView name = (TextView)view;
            for (int i = 0; i < nameIds.size(); i++){
                if(view.getId() == nameIds.get(i)){
                    Log.d("from search", "name is: " + name.getHint());
                }
            }
        }
    };

    public void makeFirstSearchResult(String usersName, String usersNickName, String id){
        checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        actualNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        checkParams.addRule(RelativeLayout.BELOW, lastNicknameId);
        checkParams.addRule(RelativeLayout.ALIGN_LEFT, lastNicknameId);
        checkParams.addRule(RelativeLayout.ALIGN_START, lastNicknameId);

        created = new CheckBox(Search.this);
        created.setTextColor(Color.parseColor("#000000"));
        created.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            created.setBackgroundTintList(this.getResources().getColorStateList(R.color.primaryColor));
            created.setButtonTintList(this.getResources().getColorStateList(R.color.primaryColor));
        }
        created.setId(View.generateViewId());
        ids.add(Integer.toString(created.getId()));
        contentHolder.addView(created, checkParams);

        actualNameParams.addRule(RelativeLayout.BELOW, lastNicknameId);
        checkParams.addRule(RelativeLayout.ALIGN_START, lastNicknameId);
        actualNameParams.addRule(RelativeLayout.RIGHT_OF, created.getId());

        nicknameParams.addRule(RelativeLayout.BELOW, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_LEFT, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_START, created.getId());
        nicknameParams.setMargins(140, 0, 0, 0);

        actualName=new TextView(Search.this);
        actualName.setTextAppearance(this, android.R.style.TextAppearance_Large);
        actualName.setTextColor(Color.parseColor("#000000"));
        actualName.setId(View.generateViewId());
        ids.add(Integer.toString(actualName.getId()));
        nameIds.add(actualName.getId());
        actualName.setText(usersName);
        contentHolder.addView(actualName, actualNameParams);
        actualName.setOnClickListener(clicks);
        nickName = new TextView(Search.this);
        nickName.setTextAppearance(this, android.R.style.TextAppearance_Small);
        nickName.setTextColor(Color.parseColor("#888888"));
        nickName.setText(usersNickName);
        nickName.setId(View.generateViewId());
        ids.add(Integer.toString(nickName.getId()));
        lastNicknameId = nickName.getId();

        contentHolder.addView(nickName, nicknameParams);


    }
    public void makeSearchResult(String usersName, String usersNickName, String id){
        checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        actualNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        setName(usersName);

        checkParams.addRule(RelativeLayout.BELOW, lastNicknameId);
        checkParams.addRule(RelativeLayout.ALIGN_LEFT, lastNicknameId);
        checkParams.addRule(RelativeLayout.ALIGN_START, lastNicknameId);
        checkParams.setMargins(-140, 0, 0, 0);

        created = new CheckBox(Search.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            created.setBackgroundTintList(this.getResources().getColorStateList(R.color.primaryColor));
            created.setButtonTintList(this.getResources().getColorStateList(R.color.primaryColor));
        }
        created.setId(View.generateViewId());
        created.setHint(id);
        ids.add(Integer.toString(created.getId()));
        contentHolder.addView(created, checkParams);

        actualNameParams.addRule(RelativeLayout.BELOW, lastNicknameId);
        actualNameParams.addRule(RelativeLayout.RIGHT_OF, created.getId());

        nicknameParams.addRule(RelativeLayout.BELOW, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_LEFT, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_START, created.getId());
        nicknameParams.setMargins(140, 0, 0, 0);

        actualName=new TextView(Search.this);
        actualName.setTextAppearance(this, android.R.style.TextAppearance_Large);
        actualName.setTextColor(Color.parseColor("#000000"));
        actualName.setId(View.generateViewId());
        actualName.setHint(id);
        ids.add(Integer.toString(actualName.getId()));
        nameIds.add(actualName.getId());
        actualName.setText(usersName);
        actualName.setOnClickListener(clicks);
        contentHolder.addView(actualName, actualNameParams);

        nickName = new TextView(Search.this);
        nickName.setTextAppearance(this, android.R.style.TextAppearance_Small);
        nickName.setTextColor(Color.parseColor("#888888"));
        nickName.setText(usersNickName);
        nickName.setId(View.generateViewId());
        ids.add(Integer.toString(nickName.getId()));
        lastNicknameId = nickName.getId();

        contentHolder.addView(nickName, nicknameParams);
    }
    public String tempName; //temp name is the name we're passing so we can access it in the onclick listener
    public void setName(String name){
        tempName = name;
    }

    public String getName(){
        return tempName;
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
        }
    }


    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(Search.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params){

        String url_select="http://skejewels.com/Android/AndroidSearch.php?q=" + what;
        Log.d("search", url_select);

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url_select);


        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(param));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

                    //read content
            is =  httpEntity.getContent();




        } catch (Exception e) {

            Log.e("log_tag", "Error in http connection " + e.toString());
            //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line=br.readLine())!=null)
                {
                    sb.append(line + "\n");
                    String[] values = line.split("-?-");


                }
                is.close();
                result=sb.toString();

                Log.d("ss", "got to here now!!!! " + result);

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        return null;

        }
        private int eventNumber = 0;
        protected void onPostExecute(Void v){
            try {
                lastNicknameId = R.id.textView3;
                for (int i = 0; i < ids.size(); i++) {
                    View myView = findViewById(Integer.parseInt(ids.get(i)));
                    contentHolder.removeView(myView);
                }
                String[] indivs = result.split(",");
                for (int i = 1; i < indivs.length; i+=4) {
                    if(i == 1){
                        makeFirstSearchResult(indivs[i + 1], indivs[i + 2], indivs[i]);
                    }else{
                        makeSearchResult(indivs[i + 1], indivs[i + 2], indivs[i]);
                    }
                }

            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
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
