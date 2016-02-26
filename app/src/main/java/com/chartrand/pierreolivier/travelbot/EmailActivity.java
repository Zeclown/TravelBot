package com.chartrand.pierreolivier.travelbot;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class EmailActivity extends ListActivity {
    SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

    }
}
