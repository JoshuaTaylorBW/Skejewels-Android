//IMPORTS
package com.basiccalc.slidenerdtut;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SubActivity extends Activity implements OnClickListener{
    private static final String TAG = SubActivity.class.getSimpleName();
    Button getLines;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new task().execute();
    }
    class task extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(SubActivity.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        protected Void doInBackground(String... params){
            //TODO double check this URL
            String url_select="ftp://josspa11@bilbo.dreamhost.com/skejewels.com/Android/PrintEvents.php";

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

                Log.e("log_tag", "Error in http connection "+e.toString());
                //Toast.makeText(Skejewels.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                is.close();
                result=sb.toString();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }
        protected void onPostExecute(Void v){
            try {
                JSONArray Jarray = new JSONArray(result);
                for(int i = 0; i<Jarray.length(); i++){
                    JSONObject Jasonobject = null;
                    Jasonobject = Jarray.getJSONObject(i);
                    String name = Jasonobject.getString("EventName");
                    String db_detail="";
                    db_detail = Jasonobject.getString("detail");
                    Log.d(TAG, "Event:" + db_detail);
                    break;
                }
                this.progressDialog.dismiss();
            }catch(Exception e){
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
    }
    public void onClick(View v) {
        switch(v.getId()) {
        }
    }
}