package com.rent.driveit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

public class DataDisplayActivity extends AppCompatActivity {
    private Menu menu;
    SharedPreferences sh_get;
    public Toolbar mToolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        TextView descrptiontv = (TextView) findViewById(R.id.datetext);
        TextView timetexttv = (TextView) findViewById(R.id.timetext);
        collapsingToolbarLayout = findViewById(R.id.collapsingtl);
        sh_get = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        long date1 = sh_get.getLong("pickupDate",1);
        long date2 = sh_get.getLong("dropoffDate",2);
        float time1 = sh_get.getFloat("pickupTime",-1);
        float time2 = sh_get.getFloat("dropoffTime",-1);
        String a = "", b= "";
        String datetext = sh_get.getString("headerText","");




        if(time1==0){
            a = "12 AM";
        }
        else if(time1==1){
            a= "1 AM";
        }
        else if(time1==2){
            a= "2 AM";
        }
        else if(time1==3){
            a= "3 AM";
        }
        else if(time1==4){
            a= "4 AM";
        }
        else if(time1==5){
            a= "5 AM";
        }
        else if(time1==6) {
            a = "6 AM";
        }
        else if(time1==7){
            a= "7 AM";
        }
        else if(time1==8){
            a= "8 AM";
        }
        else if(time1==9){
            a= "9 AM";
        }
        else if(time1==10){
            a= "10 AM";
        }
        else if(time1==11){
            a= "11 AM";
        }
        else if(time1==12){
            a= "12 PM";
        }
        else if(time1==13){
            a= "1 PM";
        }
        else if(time1==14){
            a= "2 PM";
        }
        else if(time1==15){
            a= "3 PM";
        }
        else if(time1==16){
            a= "4 PM";
        }
        else if(time1==17){
            a= "5 PM";
        }
        else if(time1==18){
            a= "6 PM";
        }
        else if(time1==19){
            a= "7 PM";
        }
        else if(time1==20){
            a= "8 PM";
        }
        else if(time1==21){
            a= "9 PM";
        }
        else if(time1==22){
            a= "10 PM";
        }
        else if(time1==23){
            a= "11 PM";
        }


        if(time2==0){
            b = "12 AM";
        }
        else if(time2==1){
            b= "1 AM";
        }
        else if(time2==2){
            b= "2 AM";
        }
        else if(time2==3){
            b= "3 AM";
        }
        else if(time2==4){
            b= "4 AM";
        }
        else if(time2==5){
            b= "5 AM";
        }
        else if(time2==6) {
            b = "6 AM";
        }
        else if(time2==7){
            b= "7 AM";
        }
        else if(time2==8){
            b= "8 AM";
        }
        else if(time2==9){
            b= "9 AM";
        }
        else if(time2==10){
            b= "10 AM";
        }
        else if(time2==11){
            b= "11 AM";
        }
        else if(time2==12){
            b= "12 PM";
        }
        else if(time2==13) {
            b = "1 PM";
        }
        else if(time2==14){
            b= "2 PM";
        }
        else if(time2==15){
            b= "3 PM";
        }
        else if(time2==16){
            b= "4 PM";
        }
        else if(time2==17){
            b= "5 PM";
        }
        else if(time2==18){
            b= "6 PM";
        }
        else if(time2==19){
            b= "7 PM";
        }
        else if(time2==20){
            b= "8 PM";
        }
        else if(time2==21){
            b= "9 PM";
        }
        else if(time2==22.0){
            b= "10 PM";
        }
        else if(time2==23){
            b= "11 PM";
        }




        if(!Objects.equals(datetext, "")){
            descrptiontv.setText(getString(R.string.date_display, datetext));
            timetexttv.setText(getString(R.string.time_display, a,b));

        }




        mToolbar = (Toolbar) findViewById(R.id.toolbarx);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarlay);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.datadisplaytoolbar_menu, menu);
        hideOption(R.id.sort1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;}
        else if (id == R.id.sort1) {
            return true;
        }
        else if(id == android.R.id.home){
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }





}