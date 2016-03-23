package com.skejewels.skejewels;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.basiccalc.slidenerdtut.NavigationDrawerFragment;
import com.basiccalc.slidenerdtut.R;

/**
 * Created by j80ma_000 on 3/20/2016.
 */
public class FriendRequests extends AppCompatActivity implements View.OnClickListener, NavigationDrawerFragment.OnFragmentInteractionListener, View.OnTouchListener {
    private Toolbar toolbar;
    private int userId;
    
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    public void onClick(View view) {

    }

    public void onFragmentInteraction(View v) {

    }
    public void onItemSelected(AdapterViewCompat<?> parent, View view, int position, long id) {

    }

    public void onNothingSelected(AdapterViewCompat<?> parent) {

    }

        class task extends AsyncTask<String, String, Void>
        {
            private ProgressDialog progressDialog = new ProgressDialog(Skejewels.this);
            InputStream is = null ;
            String result = "";
            protected void onPreExecute() {
                if(alreadyBegun == 0){
                    setMonthText();
                }
                progressDialog.setMessage("Fetching data...");
                progressDialog.show();
                progressDialog.setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface arg0) {
                        task.this.cancel(true);
                    }
                });
            }
            protected Void doInBackground(String... params){

                String url_select="http://skejewels.com/Android/AndroidFriendRequests.php?id=" + userId;
                Log.d(TAG, "" + url_select);

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

                    Log.d(TAG, "got to here now!!!! " + result);

                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result "+e.toString());
                }

                return null;

            }
            protected void onPostExecute(Void v){
                try {
                    String[] indivs = result.split("pampurppampurpampurp");
                    rows.clear();
                    for (int i = 0; i < indivs.length - 1; i++) {
                        String eventName = indivs[i];
                        Log.d(TAG, "Event:" + eventName + " " + dayOfWeek);
                    }

                    this.progressDialog.dismiss();

                    if(alreadyBegun == 0) {
                        onCreateCalendar();
                        alreadyBegun++;
                    }else{
                        rebuildCalendar();
                    }

                    onSpreadCreate();

                }catch(Exception e){
                    Log.e("log_tag", "Error parsing data "+e.toString());
                }
            }
        }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
