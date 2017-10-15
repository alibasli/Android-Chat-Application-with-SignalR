package com.mehme.menuexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by mehme on 16.06.2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: add settings

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext() , com.mehme.menuexample.MainActivity.class);
        startActivity(intent);
    }


}
