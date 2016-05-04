package com.skejewels.skejewels;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;
import com.basiccalc.slidenerdtut.Skejewels;

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
import java.util.ArrayList;

/**
 * Created by j80ma_000 on 12/28/2015.
 */
public class EditEvent  extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    private String numEventId = "0";
    private String eventId;
    private String eventName, startTime, endTime, repeatType,visibility;
    private Button nextButton, deleteButton;
    private EditText newEventName;
    private Button title;
    private TextView searchText, requestText, notificationText;
    private static final String TAG = Skejewels.class.getSimpleName();


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Log.d("From EditEvent.java", "Started");

        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);

        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        deleteButton = (Button)findViewById(R.id.DeleteButton);
        deleteButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        newEventName = (EditText)findViewById(R.id.EventNameEditor);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            numEventId = extras.getString("eventId", "12");
        }

        eventId = numEventId;

        new task().execute();

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextButton:
                eventName = newEventName.getText().toString();
                if(!eventName.matches("")) {
                    Intent intent = new Intent(this, EditEventFirstClock.class);
                    intent.putExtra("eventId", eventId);
                    intent.putExtra("eventStartTime", startTime);
                    intent.putExtra("eventEndTime", endTime);
                    intent.putExtra("eventRepeat", repeatType);
                    intent.putExtra("eventVisibility", visibility);
                    intent.putExtra("newEventName", eventName);
                    startActivity(intent);
                }
                break;
            case R.id.DeleteButton:
                Log.d("TAG", "Delete Button Has Been Clicked");
                areYouSureBox();
                break;
        }
    }
    public void areYouSureBox(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Do you really want to delete this event?");
      builder.setPositiveButton("yes",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                      new task2().execute();
                      goToSkejewel();
                  }
              });
      builder.setNegativeButton("no",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                  }
              });
      builder.create().show();
    }
    public void goToSkejewel(){
        Intent intent2 = new Intent(this, Skejewels.class);
        startActivity(intent2);

    }

    public void onFragmentInteraction(View v) {

    }

    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(EditEvent.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {

            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }

        protected Void doInBackground(String... params) {

            String url_select = "http://skejewels.com/Android/GetEventDetails.php?id="+eventId;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();


            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();

                Log.d("S", "got to here now!!!! " + result);

            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {
            try {
                String[] results = result.split(",,");

                eventName=results[2];
                startTime=results[3];
                endTime=results[4];
                repeatType =results[5];
                visibility=results[6];

                newEventName.setText(eventName, TextView.BufferType.EDITABLE);

                this.progressDialog.dismiss();


            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }
     class task2 extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(EditEvent.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {

            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task2.this.cancel(true);
                }
            });
        }

        protected Void doInBackground(String... params) {

            String url_select = "http://skejewels.com/DeleteEvent.php?id="+Integer.parseInt(eventId);


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();


            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();


            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        //    eventName
        //    startTime
        //    endTime
        //    repeatType
        //    visibility

        protected void onPostExecute(Void v) {
            try {

                changeActivity();

                this.progressDialog.dismiss();

            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }
    public void changeActivity(){
        Intent intent = new Intent(this, Skejewels.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
