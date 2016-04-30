package com.basiccalc.slidenerdtut;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skejewels.skejewels.FriendRequests;
import com.skejewels.skejewels.IndividualDayActivity;
import com.skejewels.skejewels.IndividualDayAllComments;
import com.skejewels.skejewels.IndividualEventActivity;
import com.skejewels.skejewels.Notifications;
import com.skejewels.skejewels.Search;
import com.skejewels.skejewels.SignIn;
import com.skejewels.skejewels.SignUp;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Skejewels extends AppCompatActivity implements NavigationDrawerFragment.OnFragmentInteractionListener, View.OnClickListener, View.OnTouchListener {
    private Toolbar toolbar;
    private Button nextMonthButton, lastMonthButton, homeButton;

    private Button title;
    private TextView searchText, requestText, notificationText;
    private static final String TAG = Skejewels.class.getSimpleName();
    private ArrayList rows = new ArrayList<String>();
    private ArrayList<Integer> spreadButtonIds = new ArrayList<Integer>();
    private String[] Maybe = new String[]{};
    private int monthInt = 0;
    private int yearInt = 2015;
    private int alreadyBegun = 0;
    private int dayToOpen = 0;//which day do we open when moving to an individual day page.
    private int spreadLength = 10; //length of feed. INITIALLY SET TO 10

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date date;
    String dateString;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        title = (Button) findViewById(R.id.homeButton);
        title.setOnClickListener(this);

        searchText = (TextView) findViewById(R.id.search_text);
        searchText.setOnClickListener(this);

        requestText = (TextView) findViewById(R.id.request_text);
        requestText.setOnClickListener(this);

        notificationText = (TextView) findViewById(R.id.notification_text);
        notificationText.setOnClickListener(this);

        nextMonthButton = (Button) findViewById(R.id.nextMonthClickable);
        nextMonthButton.setOnClickListener(this);
        lastMonthButton= (Button) findViewById(R.id.lastMonthClickable);
        lastMonthButton.setOnClickListener(this);



        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        new task().execute();
        new getSpread().execute();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //CALENDAR
        public void onCreateCalendar(){
            getHeaders();
            getContents();
            setFirstDay();
            setContents();
            setContentsAndHeadersClick();
        }
        public void rebuildCalendar(){
            resetContents();
            setFirstDay();
            setContents();
            setMonthText(monthInt);
        }

        //SET VARIABLES OUTSIDE OF CALENDAR
        public void setMonthText(){
            TextView month = (TextView) findViewById(R.id.EventExplanation);
            String monthFullName = "";

            monthInt = Calendar.getInstance().get(Calendar.MONTH);
            yearInt = Calendar.getInstance().get(Calendar.YEAR);

            dateString  = yearInt + "-" + monthInt + "-01";
            try {
                date = format.parse(dateString);
            }catch(Exception e){
                e.printStackTrace();
            }



            switch (monthInt){
                case 0:
                    monthFullName = "January";
                    break;
                case 1:
                    monthFullName = "February";
                    break;
                case 2:
                    monthFullName = "March";
                    break;
                case 3:
                    monthFullName = "April";
                    break;
                case 4:
                    monthFullName = "May";
                    break;
                case 5:
                    monthFullName = "June";
                    break;
                case 6:
                    monthFullName = "July";
                    break;
                case 7:
                    monthFullName = "August";
                    break;
                case 8:
                    monthFullName = "September";
                    break;
                case 9:
                    monthFullName = "October";
                    break;
                case 10:
                    monthFullName = "November";
                    break;
                case 11:
                    monthFullName = "December";
                    break;
            }
            month.setText(monthFullName);
            Typeface myCustomTypeFace = Typeface.createFromAsset(getAssets(), "fonts/nexa_font.otf");

            month.setTypeface(myCustomTypeFace);
        }
        public void setMonthText(int whichMonth){
            TextView month = (TextView) findViewById(R.id.EventExplanation);
            String monthFullName = "";
            int monthInt = whichMonth;
            dateString  = yearInt + "-" + whichMonth + "-01";
            try {
                date = format.parse(dateString);
            }catch (Exception e){
                e.printStackTrace();
            }

            switch (whichMonth){
                case 0:
                    monthFullName = "January";
                    break;
                case 1:
                    monthFullName = "February";
                    break;
                case 2:
                    monthFullName = "March";
                    break;
                case 3:
                    monthFullName = "April";
                    break;
                case 4:
                    monthFullName = "May";
                    break;
                case 5:
                    monthFullName = "June";
                    break;
                case 6:
                    monthFullName = "July";
                    break;
                case 7:
                    monthFullName = "August";
                    break;
                case 8:
                    monthFullName = "September";
                    break;
                case 9:
                    monthFullName = "October";
                    break;
                case 10:
                    monthFullName = "November";
                    break;
                case 11:
                    monthFullName = "December";
                    break;
            }
            month.setText(monthFullName);
            Typeface myCustomTypeFace = Typeface.createFromAsset(getAssets(), "fonts/nexa_font.otf");

            month.setTypeface(myCustomTypeFace);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            //        if(id == R.id.navigate){
            //            startActivity(new Intent(this, SubActivity.class));
            //        }

            return super.onOptionsItemSelected(item);
        }
        public void onFragmentInteraction(View v){
            Log.d("frag", Integer.toString(v.getId()));
        }

        public void nextMonth(){
            if(monthInt == 11){
                monthInt = 0;
                yearInt++;
            }else{
                monthInt++;
            }
            new task().execute();
        }
        public void lastMonth(){
            if(monthInt == 0){
                monthInt = 11;
                yearInt--;
            }else{
                monthInt--;
            }
            new task().execute();
        }

        //TOOLS
        public static Typeface getTypeface(Context context, String typeface){
            Typeface mFont = Typeface.createFromAsset(context.getAssets(), typeface);
            //mTextView = (TextView) findViewById(R.id.text_view);
            //mTextView.setTypeFace(MyApplication.getTypeface(this, TYPEFACEPATH);
            return mFont;
        }

        //CALENDAR CREATION
        //RETRIEVING VARIABLES
        private TextView header1;private TextView header2;private TextView header3;private TextView header4;private TextView header5;private TextView header6;private TextView header7;private TextView header8;private TextView header9;private TextView header10;private TextView header11;private TextView header12;private TextView header13;private TextView header14;private TextView header15;private TextView header16;private TextView header17;private TextView header18;private TextView header19;private TextView header20;private TextView header21;private TextView header22;private TextView header23;private TextView header24;private TextView header25;private TextView header26;private TextView header27;private TextView header28;private TextView header29;private TextView header30;private TextView header31;private TextView header32;private TextView header33;private TextView header34;private TextView header35;private TextView header36;private TextView header37;private TextView header38;private TextView header39;private TextView header40;private TextView header41;private TextView header42;private TextView content1;private TextView content2;private TextView content3;private TextView content4;private TextView content5;private TextView content6;private TextView content7;private TextView content8;private TextView content9;private TextView content10;private TextView content11;private TextView content12;private TextView content13;private TextView content14;private TextView content15;private TextView content16;private TextView content17;private TextView content18;private TextView content19;private TextView content20;private TextView content21;private TextView content22;private TextView content23;private TextView content24;private TextView content25;private TextView content26;private TextView content27;private TextView content28;private TextView content29;private TextView content30;private TextView content31;private TextView content32;private TextView content33;private TextView content34;private TextView content35;private TextView content36;private TextView content37;private TextView content38;private TextView content39;private TextView content40;private TextView content41;private TextView content42;
        public TextView[] allContents;

        public void getHeaders(){
            header1 = (TextView) findViewById(R.id.header1);header2 = (TextView) findViewById(R.id.header2);header3 = (TextView) findViewById(R.id.header3);header4 = (TextView) findViewById(R.id.header4);header5 = (TextView) findViewById(R.id.header5);header6 = (TextView) findViewById(R.id.header6);header7 = (TextView) findViewById(R.id.header7);header8 = (TextView) findViewById(R.id.header8);header9 = (TextView) findViewById(R.id.header9);header10 = (TextView) findViewById(R.id.header10);header11 = (TextView) findViewById(R.id.header11);header12 = (TextView) findViewById(R.id.header12);header13 = (TextView) findViewById(R.id.header13);header14 = (TextView) findViewById(R.id.header14);header15 = (TextView) findViewById(R.id.header15);header16 = (TextView) findViewById(R.id.header16);header17 = (TextView) findViewById(R.id.header17);header18 = (TextView) findViewById(R.id.header18);header19 = (TextView) findViewById(R.id.header19);header20 = (TextView) findViewById(R.id.header20);header21 = (TextView) findViewById(R.id.header21);header22 = (TextView) findViewById(R.id.header22);header23 = (TextView) findViewById(R.id.header23);header24 = (TextView) findViewById(R.id.header24);header25 = (TextView) findViewById(R.id.header25);header26 = (TextView) findViewById(R.id.header26);header27 = (TextView) findViewById(R.id.header27);header28 = (TextView) findViewById(R.id.header28);header29 = (TextView) findViewById(R.id.header29);header30 = (TextView) findViewById(R.id.header30);header31 = (TextView) findViewById(R.id.header31);header32 = (TextView) findViewById(R.id.header32);header33 = (TextView) findViewById(R.id.header33);header34 = (TextView) findViewById(R.id.header34);header35 = (TextView) findViewById(R.id.header35);header36 = (TextView) findViewById(R.id.header36);header37 = (TextView) findViewById(R.id.header37);header38 = (TextView) findViewById(R.id.header38);header39 = (TextView) findViewById(R.id.header39);header40 = (TextView) findViewById(R.id.header40);header41 = (TextView) findViewById(R.id.header41);header42 = (TextView) findViewById(R.id.header42);
        }
        public void getContents(){
            content1 = (TextView) findViewById(R.id.content1);content2 = (TextView) findViewById(R.id.content2);content3 = (TextView) findViewById(R.id.content3);content4 = (TextView) findViewById(R.id.content4);content5 = (TextView) findViewById(R.id.content5);content6 = (TextView) findViewById(R.id.content6);content7 = (TextView) findViewById(R.id.content7);content8 = (TextView) findViewById(R.id.content8);content9 = (TextView) findViewById(R.id.content9);content10 = (TextView) findViewById(R.id.content10);content11 = (TextView) findViewById(R.id.content11);content12 = (TextView) findViewById(R.id.content12);content13 = (TextView) findViewById(R.id.content13);content14 = (TextView) findViewById(R.id.content14);content15 = (TextView) findViewById(R.id.content15);content16 = (TextView) findViewById(R.id.content16);content17 = (TextView) findViewById(R.id.content17);content18 = (TextView) findViewById(R.id.content18);content19 = (TextView) findViewById(R.id.content19);content20 = (TextView) findViewById(R.id.content20);content21 = (TextView) findViewById(R.id.content21);content22 = (TextView) findViewById(R.id.content22);content23 = (TextView) findViewById(R.id.content23);content24 = (TextView) findViewById(R.id.content24);content25 = (TextView) findViewById(R.id.content25);content26 = (TextView) findViewById(R.id.content26);content27 = (TextView) findViewById(R.id.content27);content28 = (TextView) findViewById(R.id.content28);content29 = (TextView) findViewById(R.id.content29);content30 = (TextView) findViewById(R.id.content30);content31 = (TextView) findViewById(R.id.content31);content32 = (TextView) findViewById(R.id.content32);content33 = (TextView) findViewById(R.id.content33);content34 = (TextView) findViewById(R.id.content34);content35 = (TextView) findViewById(R.id.content35);content36 = (TextView) findViewById(R.id.content36);content37 = (TextView) findViewById(R.id.content37);content38 = (TextView) findViewById(R.id.content38);content39 = (TextView) findViewById(R.id.content39);content40 = (TextView) findViewById(R.id.content40);content41 = (TextView) findViewById(R.id.content41);content42 = (TextView) findViewById(R.id.content42);
            allContents = new TextView[]{content1,content2,content3,content4,content5,content6,content7,content8,content9,content10,content11,content12,content13,content14,content15,content16,content17,content18,content19,content20,content21,content22,content23,content24,content25,content26,content27,content28,content29,content30,content31,content32,content33,content34,content35,content36,content37,content38,content39,content40,content41,content42};
        }
        private void resetContents(){
            for(int i = 0; i < allContents.length; i++){
                allContents[i].setBackgroundColor(Color.parseColor("#Ffffff"));
            }
        }
        private Date getFirstDateOfCurrentMonth() {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthInt);
            cal.set(Calendar.YEAR, yearInt);
            cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
            return cal.getTime();
        }
        private Date getLastDateOfCurrentMonth(){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthInt);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, yearInt);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            return cal.getTime();

        }
        private int dayOfWeek;
        private int initDayOfWeek;
        public void setFirstDay(){
            Calendar c = Calendar.getInstance();
            c.setTime(getFirstDateOfCurrentMonth());
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            Log.d("skejewels", "&&&&&&&&&&&&&&" + dayOfWeek);
            if(alreadyBegun == 0){
                initDayOfWeek = dayOfWeek;
            }
            Log.d(TAG, "Firstday is on: " + dayOfWeek);
            setLastDay();
            makeFirstWeek(dayOfWeek);
        }
        private int lastDay;
        public void setLastDay(){
            Calendar c = Calendar.getInstance();
            c.setTime(getLastDateOfCurrentMonth());
            lastDay = c.get(Calendar.DAY_OF_MONTH);
        }
        public void makeFirstWeek(int first){
            colorSecondLastRow();
            switch (first) {
                case 1:
                    header1.setText("1");
                    header2.setText("2");
                    header3.setText("3");
                    header4.setText("4");
                    header5.setText("5");
                    header6.setText("6");
                    header7.setText("7");
                    header8.setText("8");
                    header9.setText("9");
                    header10.setText("10");
                    header11.setText("11");
                    header12.setText("12");
                    header13.setText("13");
                    header14.setText("14");
                    header15.setText("15");
                    header16.setText("16");
                    header17.setText("17");
                    header18.setText("18");
                    header19.setText("19");
                    header20.setText("20");
                    header21.setText("21");
                    header22.setText("22");
                    header23.setText("23");
                    header24.setText("24");
                    header25.setText("25");
                    header26.setText("26");
                    header27.setText("27");
                    header28.setText("28");

                    if(lastDay > 28) {
                        header29.setText("29");

                    }else{
                        header29.setText("");
                        killSecondLastRow();

                    }
                    if(lastDay > 29) {
                        header30.setText("30");

                    }else{
                        header30.setText("");
                    }

                    if(lastDay > 30) {
                        header31.setText("31");
                    }else{
                        header31.setText("");
                    }
                    killLastRow();
                    header32.setText("");
                    header33.setText("");
                    header34.setText("");
                    header35.setText("");

                    break;
                case 2:
                    header1.setText("");
                    header2.setText("1");
                    header3.setText("2");
                    header4.setText("3");
                    header5.setText("4");
                    header6.setText("5");
                    header7.setText("6");
                    header8.setText("7");
                    header9.setText("8");
                    header10.setText("9");
                    header11.setText("10");
                    header12.setText("11");
                    header13.setText("12");
                    header14.setText("13");
                    header15.setText("14");
                    header16.setText("15");
                    header17.setText("16");
                    header18.setText("17");
                    header19.setText("18");
                    header20.setText("19");
                    header21.setText("20");
                    header22.setText("21");
                    header23.setText("22");
                    header24.setText("23");
                    header25.setText("24");
                    header26.setText("25");
                    header27.setText("26");
                    header28.setText("27");
                    header29.setText("28");
                    if(lastDay > 28) {
                        header30.setText("29");
                    }else{
                        header30.setText("");

                    }
                    if(lastDay > 29) {
                        header31.setText("30");
                    }else{
                        header31.setText("");
                    }

                    if(lastDay > 30) {
                        header32.setText("31");
                    }else{
                        header32.setText("");
                    }
                    header33.setText("");
                    header34.setText("");
                    header35.setText("");
                    killLastRow();
                    break;
                case 3:
                    header1.setText("");
                    header2.setText("");
                    header3.setText("1");
                    header4.setText("2");
                    header5.setText("3");
                    header6.setText("4");
                    header7.setText("5");
                    header8.setText("6");
                    header9.setText("7");
                    header10.setText("8");
                    header11.setText("9");
                    header12.setText("10");
                    header13.setText("11");
                    header14.setText("12");
                    header15.setText("13");
                    header16.setText("14");
                    header17.setText("15");
                    header18.setText("16");
                    header19.setText("17");
                    header20.setText("18");
                    header21.setText("19");
                    header22.setText("20");
                    header23.setText("21");
                    header24.setText("22");
                    header25.setText("23");
                    header26.setText("24");
                    header27.setText("25");
                    header28.setText("26");
                    header29.setText("27");
                    header30.setText("28");
                    if(lastDay > 28) {
                        header31.setText("29");
                    }else{
                        header31.setText("");
                    }
                    if(lastDay > 29) {
                        header32.setText("30");
                    }else{
                        header32.setText("");
                    }
                    if(lastDay > 30) {
                        header33.setText("31");
                    }else{
                        header33.setText("");
                    }
                    header34.setText("");
                    header35.setText("");
                    killLastRow();
                    break;
                case 4:
                    header1.setText("");
                    header2.setText("");
                    header3.setText("");
                    header4.setText("1");
                    header5.setText("2");
                    header6.setText("3");
                    header7.setText("4");
                    header8.setText("5");
                    header9.setText("6");
                    header10.setText("7");
                    header11.setText("8");
                    header12.setText("9");
                    header13.setText("10");
                    header14.setText("11");
                    header15.setText("12");
                    header16.setText("13");
                    header17.setText("14");
                    header18.setText("15");
                    header19.setText("16");
                    header20.setText("17");
                    header21.setText("18");
                    header22.setText("19");
                    header23.setText("20");
                    header24.setText("21");
                    header25.setText("22");
                    header26.setText("23");
                    header27.setText("24");
                    header28.setText("25");
                    header29.setText("26");
                    header30.setText("27");
                    header31.setText("28");
                    if(lastDay > 28) {
                        header32.setText("29");
                    }else{
                        header32.setText("");
                    }
                    if(lastDay > 29) {
                        header33.setText("30");
                    }else{
                        header33.setText("");
                    }
                    if(lastDay > 30) {
                        header34.setText("31");
                    }else{
                        header34.setText("");
                    }
                    header35.setText("");
                    killLastRow();
                    break;
                case 5:
                    header1.setText("");
                    header2.setText("");
                    header3.setText("");
                    header4.setText("");
                    header5.setText("1");
                    header6.setText("2");
                    header7.setText("3");
                    header8.setText("4");
                    header9.setText("5");
                    header10.setText("6");
                    header11.setText("7");
                    header12.setText("8");
                    header13.setText("9");
                    header14.setText("10");
                    header15.setText("11");
                    header16.setText("12");
                    header17.setText("13");
                    header18.setText("14");
                    header19.setText("15");
                    header20.setText("16");
                    header21.setText("17");
                    header22.setText("18");
                    header23.setText("19");
                    header24.setText("20");
                    header25.setText("21");
                    header26.setText("22");
                    header27.setText("23");
                    header28.setText("24");
                    header29.setText("25");
                    header30.setText("26");
                    header31.setText("27");
                    header32.setText("28");
                    if(lastDay > 28) {
                        header33.setText("29");
                    }else{
                        header33.setText("");
                    }
                    if(lastDay > 29) {
                        header34.setText("30");
                    }else{
                        header34.setText("");
                    }
                    if(lastDay > 30) {
                        header35.setText("31");
                    }else{
                        header35.setText("");
                    }
                    killLastRow();
                    break;
                case 6:
                    header1.setText("");
                    header2.setText("");
                    header3.setText("");
                    header4.setText("");
                    header5.setText("");
                    header6.setText("1");
                    header7.setText("2");
                    header8.setText("3");
                    header9.setText("4");
                    header10.setText("5");
                    header11.setText("6");
                    header12.setText("7");
                    header13.setText("8");
                    header14.setText("9");
                    header15.setText("10");
                    header16.setText("11");
                    header17.setText("12");
                    header18.setText("13");
                    header19.setText("14");
                    header20.setText("15");
                    header21.setText("16");
                    header22.setText("17");
                    header23.setText("18");
                    header24.setText("19");
                    header25.setText("20");
                    header26.setText("21");
                    header27.setText("22");
                    header28.setText("23");
                    header29.setText("24");
                    header30.setText("25");
                    header31.setText("26");
                    header32.setText("27");
                    header33.setText("28");
                    if(lastDay > 28) {
                        header34.setText("29");
                    }else{
                        header34.setText("");
                    }
                    if(lastDay > 29) {
                        header35.setText("30");
                    }else{
                        header35.setText("");
                    }
                    if(lastDay > 30) {
                        header36.setText("31");
                    }else{
                        header36.setText("");
                    }
                    colorLastRow();
                    header37.setText("");
                    header38.setText("");
                    header39.setText("");
                    header40.setText("");
                    header41.setText("");
                    header42.setText("");
                    break;
                case 7:
                    header1.setText("");
                    header2.setText("");
                    header3.setText("");
                    header4.setText("");
                    header5.setText("");
                    header6.setText("");
                    header7.setText("1");
                    header8.setText("2");
                    header9.setText("3");
                    header10.setText("4");
                    header11.setText("5");
                    header12.setText("6");
                    header13.setText("7");
                    header14.setText("8");
                    header15.setText("9");
                    header16.setText("10");
                    header17.setText("11");
                    header18.setText("12");
                    header19.setText("13");
                    header20.setText("14");
                    header21.setText("15");
                    header22.setText("16");
                    header23.setText("17");
                    header24.setText("18");
                    header25.setText("19");
                    header26.setText("20");
                    header27.setText("21");
                    header28.setText("22");
                    header29.setText("23");
                    header30.setText("24");
                    header31.setText("25");
                    header32.setText("26");
                    header33.setText("27");
                    header34.setText("28");
                    if(lastDay > 28) {
                        header35.setText("29");
                    }else{
                        header35.setText("");
                    }
                    if(lastDay > 29) {
                        header36.setText("30");
                    }else{
                        header36.setText("");
                    }
                    if(lastDay > 30) {
                        header37.setText("31");
                    }else{
                        header37.setText("");
                    }

                    colorLastRow();
                    header38.setText("");
                    header39.setText("");
                    header40.setText("");
                    header41.setText("");
                    header42.setText("");
            }
        }
        public void colorLastRow(){
            header36.setBackgroundColor(Color.parseColor("#FFE332"));
            header37.setBackgroundColor(Color.parseColor("#FFE332"));
            header38.setBackgroundColor(Color.parseColor("#FFE332"));
            header39.setBackgroundColor(Color.parseColor("#FFE332"));
            header40.setBackgroundColor(Color.parseColor("#FFE332"));
            header41.setBackgroundColor(Color.parseColor("#FFE332"));
            header42.setBackgroundColor(Color.parseColor("#FFE332"));
            content36.setBackgroundColor(Color.parseColor("#Ffffff"));
            content37.setBackgroundColor(Color.parseColor("#Ffffff"));
            content38.setBackgroundColor(Color.parseColor("#Ffffff"));
            content39.setBackgroundColor(Color.parseColor("#Ffffff"));
            content40.setBackgroundColor(Color.parseColor("#Ffffff"));
            content41.setBackgroundColor(Color.parseColor("#Ffffff"));
            content42.setBackgroundColor(Color.parseColor("#Ffffff"));
        }
        public void colorSecondLastRow(){

            content29.setBackgroundColor(Color.parseColor("#Ffffff"));
            header29.setBackgroundColor(Color.parseColor("#FFE332"));
            header30.setBackgroundColor(Color.parseColor("#FFE332"));
            header31.setBackgroundColor(Color.parseColor("#FFE332"));
            header32.setBackgroundColor(Color.parseColor("#FFE332"));
            header33.setBackgroundColor(Color.parseColor("#FFE332"));
            header34.setBackgroundColor(Color.parseColor("#FFE332"));
            header35.setBackgroundColor(Color.parseColor("#FFE332"));
            content30.setBackgroundColor(Color.parseColor("#Ffffff"));
            content31.setBackgroundColor(Color.parseColor("#Ffffff"));
            content32.setBackgroundColor(Color.parseColor("#Ffffff"));
            content33.setBackgroundColor(Color.parseColor("#Ffffff"));
            content34.setBackgroundColor(Color.parseColor("#Ffffff"));
            content35.setBackgroundColor(Color.parseColor("#Ffffff"));
        }
        public void killSecondLastRow(){
            content29.setBackgroundColor(Color.parseColor("#ffffff"));
            content30.setBackgroundColor(Color.parseColor("#ffffff"));
            content31.setBackgroundColor(Color.parseColor("#ffffff"));
            content32.setBackgroundColor(Color.parseColor("#ffffff"));
            content33.setBackgroundColor(Color.parseColor("#ffffff"));
            content34.setBackgroundColor(Color.parseColor("#ffffff"));
            content35.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        public void killLastRow(){
            header36.setText("");
            header37.setText("");
            header38.setText("");
            header39.setText("");
            header40.setText("");
            header41.setText("");
            header42.setText("");
            header36.setBackgroundColor(Color.parseColor("#F1F2F3"));
            header37.setBackgroundColor(Color.parseColor("#F1F2F3"));
            header38.setBackgroundColor(Color.parseColor("#F1F2F3"));
            header39.setBackgroundColor(Color.parseColor("#F1F2F3"));
            header40.setBackgroundColor(Color.parseColor("#F1F2F3"));
            header41.setBackgroundColor(Color.parseColor("#F1F2F3"));
            header42.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content36.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content37.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content38.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content39.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content40.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content41.setBackgroundColor(Color.parseColor("#F1F2F3"));
            content42.setBackgroundColor(Color.parseColor("#F1F2F3"));

        }
        public void setContents(){

            int currDay = 0;//day we're putting values into.
            for(int i = 0; i < allContents.length; i++){
                if(dayOfWeek > i+1){//if the day of the week that the month starts on is after the current day do nothing
                    Log.d(TAG, "SkippedDay " + dayOfWeek) ;
                }else{
                    currDay++;
                    for(int j = 0; j < rows.size(); j++){

                        // goToIndividualDay(TAG, "no changed Color");

                        if(Integer.parseInt((String)rows.get(j)) == currDay){
                            //      goToIndividualDay(TAG, "changed Color");
                            allContents[i].setBackgroundColor(Color.parseColor("#CECECE"));
                        }
                    }
                }
            }
        }

        //TASK



        //INPUT

        public void setContentsAndHeadersClick(){
            // content1.setOnClickListener(this);
            // content2.setOnClickListener(this);
            // content3.setOnClickListener(this);
            // content4.setOnClickListener(this);
            // content5.setOnClickListener(this);
            // content6.setOnClickListener(this);
            // content7.setOnClickListener(this);
            // content8.setOnClickListener(this);
            // content9.setOnClickListener(this);
            // content10.setOnClickListener(this);
            // content11.setOnClickListener(this);
            // content12.setOnClickListener(this);
            // content13.setOnClickListener(this);
            // content14.setOnClickListener(this);
            // content15.setOnClickListener(this);
            // content16.setOnClickListener(this);
            // content17.setOnClickListener(this);
            // content18.setOnClickListener(this);
            // content19.setOnClickListener(this);
            // content20.setOnClickListener(this);
            // content21.setOnClickListener(this);
            // content22.setOnClickListener(this);
            // content23.setOnClickListener(this);
            // content24.setOnClickListener(this);
            // content25.setOnClickListener(this);
            // content26.setOnClickListener(this);
            // content27.setOnClickListener(this);
            // content28.setOnClickListener(this);
            // content29.setOnClickListener(this);
            // content30.setOnClickListener(this);
            // content31.setOnClickListener(this);
            // content32.setOnClickListener(this);
            // content33.setOnClickListener(this);
            // content34.setOnClickListener(this);
            // content35.setOnClickListener(this);
            // content36.setOnClickListener(this);
            // content37.setOnClickListener(this);
            // content38.setOnClickListener(this);
            // content39.setOnClickListener(this);
            // content40.setOnClickListener(this);
            // content41.setOnClickListener(this);
            // content42.setOnClickListener(this);
            header1.setOnClickListener(this);
            header2.setOnClickListener(this);
            header3.setOnClickListener(this);
            header4.setOnClickListener(this);
            header5.setOnClickListener(this);
            header6.setOnClickListener(this);
            header7.setOnClickListener(this);
            header8.setOnClickListener(this);
            header9.setOnClickListener(this);
            header10.setOnClickListener(this);
            header11.setOnClickListener(this);
            header12.setOnClickListener(this);
            header13.setOnClickListener(this);
            header14.setOnClickListener(this);
            header15.setOnClickListener(this);
            header16.setOnClickListener(this);
            header17.setOnClickListener(this);
            header18.setOnClickListener(this);
            header19.setOnClickListener(this);
            header20.setOnClickListener(this);
            header21.setOnClickListener(this);
            header22.setOnClickListener(this);
            header23.setOnClickListener(this);
            header24.setOnClickListener(this);
            header25.setOnClickListener(this);
            header26.setOnClickListener(this);
            header27.setOnClickListener(this);
            header28.setOnClickListener(this);
            header29.setOnClickListener(this);
            header30.setOnClickListener(this);
            header31.setOnClickListener(this);
            header32.setOnClickListener(this);
            header33.setOnClickListener(this);
            header34.setOnClickListener(this);
            header35.setOnClickListener(this);
            header36.setOnClickListener(this);
            header37.setOnClickListener(this);
            header38.setOnClickListener(this);
            header39.setOnClickListener(this);
            header40.setOnClickListener(this);
            header41.setOnClickListener(this);
            header42.setOnClickListener(this);
        }
        public void checkIfContentsHeadersClicked(View v){
            switch(v.getId()){
                case R.id.content1:
                    dayToOpen = 1;
                    break;
                case R.id.content2:
                    dayToOpen = 2 - (dayOfWeek - 1);
                    break;

                case R.id.content3:
                    dayToOpen = 3 - (dayOfWeek - 1);
                    break;

                case R.id.content4:
                    dayToOpen = 4 - (dayOfWeek - 1);
                    break;

                case R.id.content5:
                    dayToOpen = 5 - (dayOfWeek - 1);
                    break;

                case R.id.content6:
                    dayToOpen = 6 - (dayOfWeek - 1);
                    break;

                case R.id.content7:
                    dayToOpen = 7 - (dayOfWeek - 1);
                    break;

                case R.id.content8:
                    dayToOpen = 8 - (dayOfWeek - 1);
                    break;

                case R.id.content9:
                    dayToOpen = 9 - (dayOfWeek - 1);
                    break;

                case R.id.content10:
                    dayToOpen = 10 - (dayOfWeek - 1);
                    break;

                case R.id.content11:
                    dayToOpen = 11 - (dayOfWeek - 1);
                    break;

                case R.id.content12:
                    dayToOpen = 12 - (dayOfWeek - 1);
                    break;

                case R.id.content13:
                    dayToOpen = 13 - (dayOfWeek - 1);
                    break;

                case R.id.content14:
                    dayToOpen = 14 - (dayOfWeek - 1);
                    break;

                case R.id.content15:
                    dayToOpen = 15 - (dayOfWeek - 1);
                    break;

                case R.id.content16:
                    dayToOpen = 16 - (dayOfWeek - 1);
                    break;

                case R.id.content17:
                    dayToOpen = 17 - (dayOfWeek - 1);
                    break;

                case R.id.content18:
                    dayToOpen = 18 - (dayOfWeek - 1);
                    break;
                case R.id.content19:
                    dayToOpen = 19 - (dayOfWeek - 1);
                    break;

                case R.id.content20:
                    dayToOpen = 20 - (dayOfWeek - 1);
                    break;

                case R.id.content21:
                    dayToOpen = 21 - (dayOfWeek - 1);
                    break;

                case R.id.content22:
                    dayToOpen = 22 - (dayOfWeek - 1);
                    break;

                case R.id.content23:
                    dayToOpen = 23 - (dayOfWeek - 1);
                    break;

                case R.id.content24:
                    dayToOpen = 24 - (dayOfWeek - 1);
                    break;

                case R.id.content25:
                    dayToOpen = 25 - (dayOfWeek - 1);
                    break;

                case R.id.content26:
                    dayToOpen = 26 - (dayOfWeek - 1);
                    break;

                case R.id.content27:
                    dayToOpen = 27 - (dayOfWeek - 1);
                    break;

                case R.id.content28:
                    dayToOpen = 28 - (dayOfWeek - 1);
                    break;

                case R.id.content29:
                    dayToOpen = 29 - (dayOfWeek - 1);
                    break;

                case R.id.content30:
                    dayToOpen = 30 - (dayOfWeek - 1);
                    break;

                case R.id.content31:
                    dayToOpen = 31 - (dayOfWeek - 1);
                    break;

                case R.id.content32:
                    dayToOpen = 32 - (dayOfWeek - 1);
                    break;

                case R.id.content33:
                    dayToOpen = 33 - (dayOfWeek - 1);
                    break;

                case R.id.content34:
                    dayToOpen = 34 - (dayOfWeek - 1);
                    break;

                case R.id.content35:
                    dayToOpen = 35 - (dayOfWeek - 1);
                    break;

                case R.id.content36:
                    dayToOpen = 36 - (dayOfWeek - 1);
                    break;

                case R.id.content37:
                    dayToOpen = 37 - (dayOfWeek - 1);
                    break;

                case R.id.content38:
                    dayToOpen = 38 - (dayOfWeek - 1);
                    break;

                case R.id.content39:
                    dayToOpen = 39 - (dayOfWeek - 1);
                    break;

                case R.id.content40:
                    dayToOpen = 40 - (dayOfWeek - 1);
                    break;

                case R.id.content41:
                    dayToOpen = 41 - (dayOfWeek - 1);
                    break;

                case R.id.content42:
                    dayToOpen = 42 - (dayOfWeek - 1);
                    break;

                case R.id.header1:
                    dayToOpen = 1;
                    break;

                case R.id.header2:
                    dayToOpen = 2 - (dayOfWeek - 1);
                    break;

                case R.id.header3:
                    dayToOpen = 3 - (dayOfWeek - 1);
                    break;

                case R.id.header4:
                    dayToOpen = 4 - (dayOfWeek - 1);
                    break;

                case R.id.header5:
                    dayToOpen = 5 - (dayOfWeek - 1);
                    break;

                case R.id.header6:
                    dayToOpen = 6 - (dayOfWeek - 1);
                    break;

                case R.id.header7:
                    dayToOpen = 7 - (dayOfWeek - 1);
                    break;

                case R.id.header8:
                    dayToOpen = 8 - (dayOfWeek - 1);
                    break;

                case R.id.header9:
                    dayToOpen = 9 - (dayOfWeek - 1);
                    break;

                case R.id.header10:
                    dayToOpen = 10 - (dayOfWeek - 1);
                    break;

                case R.id.header11:
                    dayToOpen = 11 - (dayOfWeek - 1);
                    break;

                case R.id.header12:
                    dayToOpen = 12 - (dayOfWeek - 1);
                    break;

                case R.id.header13:
                    dayToOpen = 13 - (dayOfWeek - 1);
                    break;

                case R.id.header14:
                    dayToOpen = 14 - (dayOfWeek - 1);
                    break;

                case R.id.header15:
                    dayToOpen = 15 - (dayOfWeek - 1);
                    break;

                case R.id.header16:
                    dayToOpen = 16 - (dayOfWeek - 1);
                    break;

                case R.id.header17:
                    dayToOpen = 17 - (dayOfWeek - 1);
                    break;

                case R.id.header18:
                    dayToOpen = 18 - (dayOfWeek - 1);
                    break;
                case R.id.header19:
                    dayToOpen = 19 - (dayOfWeek - 1);
                    break;

                case R.id.header20:
                    dayToOpen = 20 - (dayOfWeek - 1);
                    break;

                case R.id.header21:
                    dayToOpen = 21 - (dayOfWeek - 1);
                    break;

                case R.id.header22:
                    dayToOpen = 22 - (dayOfWeek - 1);
                    break;

                case R.id.header23:
                    dayToOpen = 23 - (dayOfWeek - 1);
                    break;

                case R.id.header24:
                    dayToOpen = 24 - (dayOfWeek - 1);
                    break;

             case R.id.header25:
                    dayToOpen = 25 - (dayOfWeek - 1);
                    break;

                case R.id.header26:
                    dayToOpen = 26 - (dayOfWeek - 1);
                    break;

                case R.id.header27:
                    dayToOpen = 27 - (dayOfWeek - 1);
                    break;

                case R.id.header28:
                    dayToOpen = 28 - (dayOfWeek - 1);
                    break;

                case R.id.header29:
                    dayToOpen = 29 - (dayOfWeek - 1);
                    break;

                case R.id.header30:
                    dayToOpen = 30 - (dayOfWeek - 1);
                    break;

                case R.id.header31:
                    dayToOpen = 31 - (dayOfWeek - 1);
                    break;

                case R.id.header32:
                    dayToOpen = 32 - (dayOfWeek - 1);
                    break;

                case R.id.header33:
                    dayToOpen = 33 - (dayOfWeek - 1);
                    break;

                case R.id.header34:
                    dayToOpen = 34 - (dayOfWeek - 1);
                    break;

                case R.id.header35:
                    dayToOpen = 35 - (dayOfWeek - 1);
                    break;

                case R.id.header36:
                    dayToOpen = 36 - (dayOfWeek - 1);
                    break;

                case R.id.header37:
                    dayToOpen = 37 - (dayOfWeek - 1);
                    break;

                case R.id.header38:
                    dayToOpen = 38 - (dayOfWeek - 1);
                    break;

                case R.id.header39:
                    dayToOpen = 39 - (dayOfWeek - 1);
                    break;

                case R.id.header40:
                    dayToOpen = 40 - (dayOfWeek - 1);
                    break;

                case R.id.header41:
                    dayToOpen = 41 - (dayOfWeek - 1);
                    break;

                case R.id.header42:
                    dayToOpen = 42 - (dayOfWeek - 1);
                    break;
            }
        }

        public void goToIndividualDay(String dayOfMonth){
            if (dayOfMonth.equals("") || dayOfMonth == null){

            }else {
                Intent i = new Intent(getApplicationContext(), IndividualDayActivity.class);
                i.putExtra("Day", Integer.parseInt(dayOfMonth));
                i.putExtra("Month", monthInt + 1);
                i.putExtra("Year", yearInt);
                startActivity(i);
            }
        }
    //CALENDAR
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

                String url_select="http://skejewels.com/Android/printDaysOfMonth.php?Month=" + (monthInt + 1) + "&Year=" + yearInt;
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
                    String[] indivs = result.split(",");
                    rows.clear();
                    for (int i = 0; i < indivs.length - 1; i++) {
                        String eventName = indivs[i];
                        eventName = eventName.replace("\"", "");
                        rows.add(eventName);
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
    //SPREAD
        private TextView spreadCardUserNameAndCard, spreadCardEvent, spreadCardEventTime, spreadCardLikeAmount, spreadCardCommentAmount, spreadCardLikeThisAnd;
        private Button spreadCardLikeButton, spreadCardCommentButton;
        private RelativeLayout holder;
        private int lastCardId = 0;//lastCardId is the id of the card that was just built. changes in makeSpreadCard method
        private int nextCardId = 0;
        private int repeater = 0;
        private RelativeLayout.LayoutParams holderParams, eventParams, eventTimeParams, likeButtonParams, commentButtonParams, likeThisAndParams, commentAmountParams, likeAmountParams;
        public void onSpreadCreate(){
            holder = (RelativeLayout) findViewById(R.id.spreadHolder);
            lastCardId = R.id.SpreadCard1;
        }
        /*
        cardId = the ID of the card directly above the one being built

        */
        private long mLastClickTime = 0;
        View.OnClickListener spreadButtonClicks = new View.OnClickListener() {
            public void onClick(View view) {
                Button button = (Button) view;
                for (int i = 0; i < spreadButtonIds.size(); i++) {
                    if (button.getId() == spreadButtonIds.get(i)) {
                        mLastClickTime = SystemClock.elapsedRealtime();
                        if (i % 2 == 0) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 100) {
                                if (button.getText().toString().equals("Like")) {
                                    button.setText("Unlike");
                                } else {
                                    button.setText("Like");
                                }
                            }
                        } else {

                            Intent changeIntent = new Intent(getApplicationContext(), IndividualDayAllComments.class);
                            changeIntent.putExtra("eventID", button.getHint());
                            startActivity(changeIntent);
                        }
                    }
                }
            }
        };

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void makeSpreadCard(int cardId, int usersId, String eventName, String usersName, int commentAmount,
                                   int likeAmount, int eventId, String eventBeginDate, String eventBegintime, String eventEndDate, String eventEndTime){
            //Create Params
                holderParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                eventParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                eventTimeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                likeButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                commentButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                likeThisAndParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                commentAmountParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                likeAmountParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            //Add Spread Card and userName
                holderParams.setMargins(0, 50, 0, 0);
                holderParams.addRule(RelativeLayout.ALIGN_LEFT, R.id.calendar);
                holderParams.addRule(RelativeLayout.ALIGN_START, R.id.calendar);
                holderParams.addRule(RelativeLayout.ALIGN_RIGHT, R.id.calendar);
                holderParams.addRule(RelativeLayout.ALIGN_END, R.id.calendar);
                holderParams.addRule(RelativeLayout.BELOW, cardId);

                spreadCardUserNameAndCard = new TextView(Skejewels.this);
                spreadCardUserNameAndCard.setTypeface(null, Typeface.BOLD);
                spreadCardUserNameAndCard.setTextColor(Color.parseColor("#009688"));
                spreadCardUserNameAndCard.setGravity(Gravity.CENTER_HORIZONTAL);
                spreadCardUserNameAndCard.setText(usersName);
                spreadCardUserNameAndCard.setPadding(0, 0, 0, 450);
                spreadCardUserNameAndCard.setElevation(6);
                spreadCardUserNameAndCard.setBackgroundColor(Color.parseColor("#ffffff"));
                spreadCardUserNameAndCard.setId(View.generateViewId());
                Log.d(TAG, "indirectly related " + Integer.toString(spreadCardUserNameAndCard.getId()));
                if (cardId == 2131558595 && repeater == 0) {
                    lastCardId = spreadCardUserNameAndCard.getId();
                    repeater++;
                }else{
                    lastCardId = spreadCardUserNameAndCard.getId();
                }

            holder.addView(spreadCardUserNameAndCard, holderParams);

            //Add Event Text
                eventParams.setMargins(0, 120, 0, 0);
            eventParams.addRule(RelativeLayout.ALIGN_TOP, spreadCardUserNameAndCard.getId());

            spreadCardEvent = new TextView(Skejewels.this);
                spreadCardEvent.setGravity(Gravity.CENTER_HORIZONTAL);
                spreadCardEvent.setText("Is Going to " + eventName + " on " + eventBeginDate);
                        spreadCardEvent.setTextColor(Color.parseColor("#000000"));
                spreadCardEvent.setElevation(7);
                spreadCardEvent.setId(View.generateViewId());
                holder.addView(spreadCardEvent, eventParams);

            //Add Event Time Text
                eventTimeParams.setMargins(0, 80, 0, 0);
                eventTimeParams.addRule(RelativeLayout.ALIGN_TOP, spreadCardEvent.getId());

                spreadCardEventTime = new TextView(Skejewels.this);
                spreadCardEventTime.setGravity(Gravity.CENTER_HORIZONTAL);
                spreadCardEventTime.setText(eventBegintime + " - " + eventEndTime);
                        spreadCardEventTime.setTextColor(Color.parseColor("#000000"));
                spreadCardEventTime.setElevation(7);
                spreadCardEventTime.setId(View.generateViewId());
                holder.addView(spreadCardEventTime, eventTimeParams);

            //Like This And Text
                likeThisAndParams.setMargins(0, 0, 0, 0);
                likeThisAndParams.addRule(RelativeLayout.ALIGN_BOTTOM, spreadCardUserNameAndCard.getId());
                likeThisAndParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


                spreadCardLikeThisAnd = new TextView(Skejewels.this);
                spreadCardLikeThisAnd.setGravity(Gravity.CENTER_HORIZONTAL);
                if(commentAmount > 0 && likeAmount > 0){
                    spreadCardLikeThisAnd.setText("like this and");
                }else if(likeAmount > 0 && commentAmount == 0){
                    spreadCardLikeThisAnd.setText("like this");
                }else{
                    spreadCardLikeThisAnd.setText("");
                }
                spreadCardLikeThisAnd.setTextColor(Color.parseColor("#000000"));
                spreadCardLikeThisAnd.setTextSize(11);
                spreadCardLikeThisAnd.setElevation(7);
                spreadCardLikeThisAnd.setId(View.generateViewId());
                holder.addView(spreadCardLikeThisAnd, likeThisAndParams);

            //Add Like Button
                    likeButtonParams.setMargins(0, 0, 0, 0);
                    likeButtonParams.addRule(RelativeLayout.LEFT_OF, spreadCardLikeThisAnd.getId());
                    likeButtonParams.addRule(RelativeLayout.BELOW, spreadCardEventTime.getId());

                    spreadCardLikeButton = new Button(Skejewels.this);
                    spreadCardLikeButton.setText("Like");
                    spreadCardLikeButton.setBackgroundColor(Color.TRANSPARENT);
                    spreadCardLikeButton.setTextColor(Color.parseColor("#009688"));
                    spreadCardLikeButton.setElevation(12);
                    spreadCardLikeButton.setPadding(80, 0, 80, 0);
                    spreadCardLikeButton.setId(View.generateViewId());

                    spreadButtonIds.add(spreadCardLikeButton.getId());
                    spreadCardLikeButton.setOnClickListener(spreadButtonClicks);
                    holder.addView(spreadCardLikeButton, likeButtonParams);

            //Add Comment Button
                    commentButtonParams.setMargins(0, 0, 0, 0);
                    commentButtonParams.addRule(RelativeLayout.RIGHT_OF, spreadCardLikeThisAnd.getId());
                    commentButtonParams.addRule(RelativeLayout.BELOW, spreadCardEventTime.getId());

                    spreadCardCommentButton = new Button(Skejewels.this);
                    spreadCardCommentButton.setText("Comment");
                    spreadCardCommentButton.setBackgroundColor(Color.TRANSPARENT);
                    spreadCardCommentButton.setTextColor(Color.parseColor("#009688"));
                    spreadCardCommentButton.setElevation(12);
                    spreadCardCommentButton.setPadding(80, 0, 80, 0);
                    spreadCardCommentButton.setHint(Integer.toString(eventId));
                    spreadCardCommentButton.setId(View.generateViewId());
                    spreadButtonIds.add(spreadCardCommentButton.getId());

                    spreadCardCommentButton.setOnClickListener(spreadButtonClicks);
                    holder.addView(spreadCardCommentButton, commentButtonParams);

            //Like Amount Text
                if(likeAmount > 0){
                    likeAmountParams.setMargins(0, 0, 0, 0);
                    likeAmountParams.addRule(RelativeLayout.ALIGN_BOTTOM, spreadCardUserNameAndCard.getId());
                    likeAmountParams.addRule(RelativeLayout.LEFT_OF, spreadCardLikeThisAnd.getId());

                    spreadCardLikeAmount = new TextView(Skejewels.this);
                    spreadCardLikeAmount.setGravity(Gravity.RIGHT);
                    if(likeAmount > 1){
                        spreadCardLikeAmount.setText(likeAmount + " people ");
                    }else{
                        spreadCardLikeAmount.setText(likeAmount + " person ");
                    }
                    spreadCardLikeAmount.setTextColor(Color.parseColor("#009688"));
                    spreadCardLikeAmount.setTextSize(11);
                    spreadCardLikeAmount.setElevation(7);
                    spreadCardLikeAmount.setId(View.generateViewId());
                    holder.addView(spreadCardLikeAmount, likeAmountParams);
                }

            //Comment Amount Text
                if(commentAmount > 0){
                    commentAmountParams.setMargins(0, 0, 0, 0);
                    commentAmountParams.addRule(RelativeLayout.ALIGN_BOTTOM, spreadCardUserNameAndCard.getId());
                    commentAmountParams.addRule(RelativeLayout.RIGHT_OF, spreadCardLikeThisAnd.getId());

                    spreadCardCommentAmount = new TextView(Skejewels.this);
                    spreadCardCommentAmount.setGravity(Gravity.RIGHT);
                    spreadCardCommentAmount.setText(" " + commentAmount + " comments");
                    spreadCardCommentAmount.setTextColor(Color.parseColor("#009688"));
                    spreadCardCommentAmount.setTextSize(11);
                    spreadCardCommentAmount.setElevation(7);
                    spreadCardCommentAmount.setId(View.generateViewId());
                    holder.addView(spreadCardCommentAmount, commentAmountParams);
                }


        }

    //SPREAD
    class getSpread extends AsyncTask<String, String, Void>
         {
            private ProgressDialog progressDialog = new ProgressDialog(Skejewels.this);
            InputStream is = null ;
            String result = "";
            protected void onPreExecute() {
                progressDialog.setMessage("Fetching data...");
                progressDialog.show();
                progressDialog.setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface arg0) {
                        getSpread.this.cancel(true);
                    }
                });
            }
            protected Void doInBackground(String... params){

                String url_select="http://skejewels.com/Android/MakeAndroidFeed.php?length=" + spreadLength;
                Log.d(TAG, "" + yearInt);

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




                } catch (Exception e) {
                    Log.e("log_tag", "Error converting result "+e.toString());
                }

                return null;

            }
            protected void onPostExecute(Void v){
                try {
                    String[] individual = result.split("doubledtumbleswillfumblemybumblebee");
                    Log.d(TAG, "got to the feed now!!!! " + result);

                    for (int i = 1; i < individual.length  - 1; i++) {
                       String[] details = individual[i].split("pampurppampurpampurp");
                        Log.d(TAG, "got to the feed now!!!! " + Arrays.toString(details));
                       makeSpreadCard(lastCardId, Integer.parseInt(details[1]), details[2], details[3], Integer.parseInt(details[4]), Integer.parseInt(details[5]), Integer.parseInt(details[6]), details[8],details[7],details[10],details[9]);
                       String eventName = individual[i];
                       eventName = eventName.replace("\"", "");
                    }

                    this.progressDialog.dismiss();

                }catch(Exception e){
                    Log.e("log_tag", "Error parsing data "+e.toString());
                }
            }
        }

    public void onClick(View v) {
        checkIfContentsHeadersClicked(v);
        switch(v.getId()) {
            case R.id.homeButton:
                Log.d(TAG,  "GOT TO HERE :) :) :)");
                alreadyBegun = 0;
                Intent intent = new Intent(this, Skejewels.class);
                startActivity(intent);
                break;
            case R.id.notification_text:
                Intent intent3 = new Intent(this, Notifications.class);
                startActivity(intent3);
                break;
            case R.id.search_text:
                Intent intent2 = new Intent(this, Search.class);
                startActivity(intent2);
                break;
            case R.id.request_text:
                Log.d(TAG, "this is being clicked");
                Intent intent4 = new Intent(this, FriendRequests.class);
                startActivity(intent4);
                break;
            case R.id.nextMonthClickable:
                rows.clear();
                nextMonth();
                Log.d(TAG, "INPUT RECIEVED :D" + monthInt);
                break;
            case R.id.lastMonthClickable:
                lastMonth();
                break;
            case R.id.header1:
                goToIndividualDay((String) header1.getText());
                break;
            case R.id.header2:
                goToIndividualDay((String) header2.getText());
                break;
            case R.id.header3:
                goToIndividualDay((String) header3.getText());
                break;
            case R.id.header4:
                goToIndividualDay((String) header4.getText());
                break;
            case R.id.header5:
                goToIndividualDay((String) header5.getText());
                break;
            case R.id.header6:
                goToIndividualDay((String) header6.getText());
                break;
            case R.id.header7:
                goToIndividualDay((String) header7.getText());
                break;
            case R.id.header8:
                goToIndividualDay((String) header8.getText());
                break;
            case R.id.header9:
                goToIndividualDay((String) header9.getText());
                break;
            case R.id.header10:
                goToIndividualDay((String) header10.getText());
                break;
            case R.id.header11:
                goToIndividualDay((String) header11.getText());
                break;
            case R.id.header12:
                goToIndividualDay((String) header12.getText());
                break;
            case R.id.header13:
                goToIndividualDay((String) header13.getText());
                break;
            case R.id.header14:
                goToIndividualDay((String) header14.getText());
                break;
            case R.id.header15:
                goToIndividualDay((String) header15.getText());
                break;
            case R.id.header16:
                goToIndividualDay((String) header16.getText());
                break;
            case R.id.header17:
                goToIndividualDay((String) header17.getText());
                break;
            case R.id.header18:
                goToIndividualDay((String) header18.getText());
                break;
            case R.id.header19:
                goToIndividualDay((String) header19.getText());
                break;
            case R.id.header20:
                goToIndividualDay((String) header20.getText());
                break;
            case R.id.header21:
                goToIndividualDay((String) header21.getText());
                break;
            case R.id.header22:
                goToIndividualDay((String) header22.getText());
                break;
            case R.id.header23:
                goToIndividualDay((String) header23.getText());
                break;
            case R.id.header24:
                goToIndividualDay((String) header24.getText());
                break;
            case R.id.header25:
                goToIndividualDay((String) header25.getText());
                break;
            case R.id.header26:
                goToIndividualDay((String) header26.getText());
                break;
            case R.id.header27:
                goToIndividualDay((String) header27.getText());
                break;
            case R.id.header28:
                goToIndividualDay((String) header28.getText());
                break;
            case R.id.header29:
                goToIndividualDay((String) header29.getText());
                break;
            case R.id.header30:
                goToIndividualDay((String) header30.getText());
                break;
            case R.id.header31:
                goToIndividualDay((String) header31.getText());
                break;
            case R.id.header32:
                goToIndividualDay((String) header32.getText());
                break;
            case R.id.header33:
                goToIndividualDay((String) header33.getText());
                break;
            case R.id.header34:
                goToIndividualDay((String) header34.getText());
                break;
            case R.id.header35:
                goToIndividualDay((String) header35.getText());
                break;
            case R.id.header36:
                goToIndividualDay((String) header36.getText());
                break;
            case R.id.header37:
                goToIndividualDay((String) header37.getText());
                break;
            case R.id.header38:
                goToIndividualDay((String) header38.getText());
                break;
            case R.id.header39:
                goToIndividualDay((String) header39.getText());
                break;
            case R.id.header40:
                goToIndividualDay((String) header40.getText());
                break;

        }

    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
