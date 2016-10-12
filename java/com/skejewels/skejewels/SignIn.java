package com.skejewels.skejewels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;
import com.basiccalc.slidenerdtut.Skejewels;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
 * Created by j80ma_000 on 12/5/2015.
 */
public class SignIn extends ActionBarActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {

    private Button signInButton, signUpButton;
    private EditText Username, Password;
    private String usernameText, passwordText;
    private int id = 0;
    public static final String MyPREFERENCES = "MyPrefs";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        signInButton = (Button) findViewById(R.id.SignInButton);
        signUpButton = (Button) findViewById(R.id.SignUpButton);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        Username = (EditText) findViewById(R.id.UsernameField);
        Password = (EditText) findViewById(R.id.PasswordField);

        printId();

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.SignInButton:
                getEditTextContents();
                new task().execute();
                break;
            case R.id.SignUpButton:
                //go to sign up page
                break;
        }
    }

    private void getEditTextContents() {
        usernameText = Username.getText().toString();
        passwordText = Password.getText().toString();
    }

    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
        InputStream is = null;
        String result = "";

        protected void onPreExecute() {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }

        protected Void doInBackground(String... params) {

            String url_select = "http://skejewels.com/Android/SignIn.php?username=" + usernameText + "&password=" + passwordText;
            Log.d("search", url_select);

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
                    String[] values = line.split("-?-");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }
            return null;
        }

        private int eventNumber = 0;

        protected void onPostExecute(Void v) {
            try {
                String[] resultSplit = result.split(",");
                if (resultSplit[0].equals("\"Correct")) {
                    Log.d("s", "correct homie");
                    id = Integer.parseInt(resultSplit[1]);
                    saveId();
                    goToCalendar();
                }
                printId();
            } catch (Exception e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
        }
    }

    public void onFragmentInteraction(View v) {

    }

    private void goToCalendar() {
        Intent intent = new Intent(this, Skejewels.class);
        startActivity(intent);
    }

    private void saveId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_user_id", Integer.toString(id));
        editor.commit();
    }

    private void printId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultValue = "NO ID";
        String printedId = sharedPreferences.getString("current_user_id", defaultValue);
        Log.d("s", printedId);
        try {
            Log.d("soooo", Integer.parseInt(sharedPreferences.getString("current_user_id", defaultValue)) + " Is this still here?");
        } catch(Exception e) {
            Log.d("soooo", e.getStackTrace().toString());
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
