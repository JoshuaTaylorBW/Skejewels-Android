
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
 * Created by j80ma_000 on 1/2/2016.
 */
public class EditRepeatsAndPrivacy  extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener, OnItemSelectedListener   {

    Resources system;
    Spinner repeatType;
    Button NextButton;
    private Intent restartIntent;

    private String typeOfRepeat;
    private String typeOfVisibility;

    private String eventId,newEventName, newStartingHour, newStartingMinute, newEndingHour,
    newEndingMinute, eventRepeat, eventVisibility;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_repeats_and_privacy);

        repeatType = (Spinner) findViewById(R.id.spinner);
        repeatType.setOnItemSelectedListener(this);
        NextButton = (Button) findViewById(R.id.nextistButton);
        NextButton.setOnClickListener(this);

        restartIntent = new Intent(this, Skejewels.class);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            eventId = extras.getString("eventId", "no event id");
            newEventName = extras.getString("newEventName", "newEventName Does Not Exist"); 
            newStartingHour = extras.getString("newStartingHour", "newStartingHour Does Not Exist"); 
            newStartingMinute = extras.getString("newStartingMinute", "newStartingMinute Does Not Exist"); 
            newEndingHour = extras.getString("newEndingHour", "newEndingHour Does Not Exist"); 
            newEndingMinute = extras.getString("newEndingMinute", "newEndingMinute Does Not Exist");
        }

        typeOfRepeat = eventRepeat;
        typeOfVisibility = eventVisibility;
    }

    public void onClick(View view) {
         switch (view.getId()){
            case R.id.nextistButton:
                new task().execute();
                break;
            case R.id.spinner:
                break;
        }
    }

    public void onFragmentInteraction(int position) {

    }

    public void onItemSelected(AdapterView<?> parentView,View v,int position,long id){
        String strAge=(String)repeatType.getItemAtPosition(position);
        if(strAge.equals("Weekly") || strAge.equals("Monthly")){
            typeOfRepeat = strAge;
        }else{
            typeOfRepeat = strAge;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    class task extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(EditRepeatsAndPrivacy.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {

            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params){
            //TODO get day
            String newEventName1 = newEventName.replaceAll("\\s+", "JambaSlammerCameraManForNothingEverMattered");
            String url_select="http://skejewels.com/Android/SQLEdit.php?id="+eventId+"&eN="+newEventName1+"&sH=10"+newStartingHour+"&sM=15"+newStartingMinute +"&eH="+newEndingHour+"&eM="+newEndingMinute+"&eR="+typeOfRepeat+"&eV="+eventVisibility;
            Log.d("Fuck That Man", url_select);

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
                }
                is.close();
                result=sb.toString();

                Log.d("Repate", "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }
        protected void onPostExecute(Void v){
            try {

                startActivity(restartIntent);


                this.progressDialog.dismiss();


            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
    }

}
