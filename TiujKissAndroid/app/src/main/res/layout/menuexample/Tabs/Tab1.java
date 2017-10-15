package com.mehme.menuexample.Tabs;

/**
 * Created by mehme on 14.07.2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehme.menuexample.R;

//Our class extending fragment
public class Tab1 extends Fragment {

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1_home in you classes
        return inflater.inflate(R.layout.tab1, container, false);
    }
}