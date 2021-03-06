package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;



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
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by j80ma_000 on 1/16/2016.
 */
public class Search extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, OnItemSelectedListener {

    public CheckBox test;
    public CheckBox created;
    public TextView actualName, nickName;
    public RelativeLayout contentHolder;
    public EditText eventNameEditor;
    private String what;
    private String[] friends;
    private ArrayList<String> ids;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ArrayList<Integer> nameIds; //used for onclicklistener
    private RelativeLayout.LayoutParams checkParams, nicknameParams, actualNameParams;
    private int lastNicknameId;
    private int currId = 188;
    final Context context = this;

    private Button title;
    private TextView searchText, requestText, notificationText;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        lastNicknameId = R.id.textView3;
        contentHolder = (RelativeLayout) findViewById(R.id.contentHolder);
        eventNameEditor = (EditText) findViewById(R.id.EventNameEditor);
        ids = new ArrayList<String>();

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);

        setId();

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
                  boolean areFriends = false;
                  for (int j = 0; j < friends.length; j++) {
                    if(((TextView) view).getHint().toString().split("\\s")[1].equals(friends[j])){
                      areFriends = true;
                    }
                  }
                  if(areFriends){
                    Intent intent = new Intent(getApplicationContext(), FriendsCalendar.class);
                    intent.putExtra("friendsName", name.getText());
                    intent.putExtra("friendsId", name.getHint().toString());
                    startActivity(intent);
                  }
                }
            }
        }
    };

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

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
        created.setOnClickListener(checks);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            created.setBackgroundTintList(this.getResources().getColorStateList(R.color.primaryColor));
            created.setButtonTintList(this.getResources().getColorStateList(R.color.primaryColor));
        }
        created.setHint("checkbox " + id);
        created.setId(View.generateViewId());
        for (int i = 0; i < friends.length; i++) {
          if(id.equals(friends[i])){
            created.setChecked(true);
            break;
          }
        }
        ids.add(Integer.toString(created.getId()));
        contentHolder.addView(created, checkParams);

        actualNameParams.addRule(RelativeLayout.BELOW, lastNicknameId);
        actualNameParams.addRule(RelativeLayout.RIGHT_OF, created.getId());
        actualNameParams.setMargins((int) pxFromDp(getApplicationContext(), -118), 0, 0, 0);

        actualName=new TextView(Search.this);
        actualName.setTextAppearance(this, android.R.style.TextAppearance_Large);
        actualName.setTextColor(Color.parseColor("#000000"));
        actualName.setId(View.generateViewId());
        ids.add(Integer.toString(actualName.getId()));
        nameIds.add(actualName.getId());
        actualName.setText(usersName);
        actualName.setHint("name " + id);
        actualName.setOnClickListener(clicks);
        contentHolder.addView(actualName, actualNameParams);

        nicknameParams.addRule(RelativeLayout.BELOW, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_LEFT, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_START, created.getId());
        nicknameParams.setMargins(140, 0, 0, 0);

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
        Log.d("Some", Arrays.toString(friends));
        for (int i = 1; i < friends.length; i++) {
          if(id.equals(friends[i])){
            Log.d("Searcher", "Correct");
            created.setChecked(true);
            break;
          }
        }
        created.setHint(id);
        created.setOnClickListener(checks);
        ids.add(Integer.toString(created.getId()));
        contentHolder.addView(created, checkParams);

        actualNameParams.addRule(RelativeLayout.BELOW, lastNicknameId);
        actualNameParams.setMargins((int) pxFromDp(getApplicationContext(), 47
        ), 0, 0, 0);

        nicknameParams.addRule(RelativeLayout.BELOW, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_LEFT, created.getId());
        nicknameParams.addRule(RelativeLayout.ALIGN_START, created.getId());
        nicknameParams.setMargins(140, 0, 0, 0);

        actualName=new TextView(Search.this);
        actualName.setTextAppearance(this, android.R.style.TextAppearance_Large);
        actualName.setTextColor(Color.parseColor("#000000"));
        actualName.setId(View.generateViewId());
        actualName.setHint("name " + id);
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

    public void Search(){
        checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        checkParams.setMargins(0, 0, 0, 0);//Set initial positions
        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//Create new dynamic layout
        nicknameParams.setMargins(0, -0, 0, 0);//Set initial positions
    }


    View.OnClickListener checks=new View.OnClickListener() {
      public void onClick(View view) {

          final CheckBox realView = (CheckBox)view;
          if(!realView.isChecked()) {
              AlertDialog.Builder builder = new AlertDialog.Builder(context);
              builder.setTitle("Do you really want to remove this friend?");

              builder.setPositiveButton("yes",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,
                                              int id) {

                              realView.setChecked(false);
                          }
                      });
              builder.setNegativeButton("no",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,
                                              int id) {

                              realView.setChecked(true);
                          }
                      });
              builder.create().show();
          }
      }
    };

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

        String url_select="http://skejewels.com/Android/AndroidSearch.php?cId=" + currId + "&q=" + what.replaceAll("\\s+","%20");
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
                String[] parts = result.split("~~~");
                friends = parts[0].split(",");
                moveFriendsToFront(parts[1].split(","), friends);

                parts[1].split(",");

            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
    }

    public ArrayList<String> moveFriendsToFront(String[] info, String[] friendIds){
        ArrayList<String> finalOrder = new ArrayList<String>();
        for(int i = 0; i < info.length - 1; i+=4){
            boolean areFriends = false;
            for(int j = 0; j < friendIds.length; j++){
                if(info[i+1].   equals(friendIds[j])){
                    areFriends = true;
                }
            }
            if(areFriends){
                finalOrder.add(0, (info[i + 1] + ",") + info[i + 2] + "," + info[i + 3]);
                Log.d("Search", "info " + info.length + " " + i);
            }else{
                finalOrder.add((info[i + 1] + ",") + info[i + 2] + "," + info[i + 3]);
                Log.d("Search", "info " + info.length + " " + i);
            }

        }

        for (int i = 0; i < finalOrder.size(); i++) {
            String[] part = finalOrder.get(i).split(",");

            Log.d("Search", "info " + i);

            if(i == 0){
                makeFirstSearchResult(part[1], part[2], part[0]);
            }else{
                makeSearchResult(part[1], part[2], part[0]);
            }
        }
        return finalOrder;
    }

    public void onFragmentInteraction(View v) {

    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private void setId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultValue = "NO ID";
        currId = Integer.parseInt(sharedPreferences.getString("current_user_id", defaultValue));
    }
}
