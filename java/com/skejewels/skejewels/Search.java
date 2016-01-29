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


/**
 * Created by j80ma_000 on 1/16/2016.
 */
public class    Search extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, OnItemSelectedListener {

    public CheckBox test;
    public CheckBox created;
    public TextView nickName;
    public RelativeLayout contentHolder;

    public EditText eventNameEditor;
    private String what;
    private ArrayList<String> ids;
    private int lastNicknameId;

    private RelativeLayout.LayoutParams checkParams, nicknameParams;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        lastNicknameId = R.id.textView3;
        contentHolder = (RelativeLayout) findViewById(R.id.contentHolder);
        eventNameEditor = (EditText) findViewById(R.id.EventNameEditor);
        ids = new ArrayList<String>();
        eventNameEditor.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    for (int i = 0; i < ids.size(); i++) {
                        View myView = findViewById(Integer.parseInt(ids.get(i)));
                        contentHolder.removeView(myView);
                    }
                    lastNicknameId = R.id.textView3;
                    what = eventNameEditor.getText().toString();
                    if(!what.contains(" ")){
                        what += "*";
                    }else{
                        what.replace(" ", "%20");
                    }
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

    public void makeFirstSearchResult(String usersName, String usersNickName, String id){
        checkParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        nicknameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

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
        created.setText(usersName);
        created.setId(View.generateViewId());
        ids.add(Integer.toString(created.getId()));
        contentHolder.addView(created, checkParams);

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

        checkParams.addRule(RelativeLayout.BELOW, R.id.textView3);
        checkParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.textView3);
        checkParams.addRule(RelativeLayout.ALIGN_START, R.id.textView3);


        created = new CheckBox(Search.this);
        created.setTextColor(Color.parseColor("#000000"));
        created.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            created.setBackgroundTintList(this.getResources().getColorStateList(R.color.primaryColor));
            created.setButtonTintList(this.getResources().getColorStateList(R.color.primaryColor));
        }

        created.setText(usersName);
        created.setId(View.generateViewId());
        ids.add(Integer.toString(created.getId()));

        contentHolder.addView(created, checkParams);

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
//            case R.id.checkBox1:
//                if (checked){
//                    Log.d("FROM_SEARCH", "checked");
//                }else {
//                    Log.d("FROM_SEARCH", "checked");
//                }
//                break;
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

        String url_select="http://skejewels.com/Android/AndroidSearch.php?q=" + what + "";

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
