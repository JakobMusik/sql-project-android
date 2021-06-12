package com.databaseproj.caltracker.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.databaseproj.caltracker.R;


public class HelpActivity extends Activity {

    private TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.train_help);

        textView = (TextView) findViewById(R.id.TrainHelpTextView);
        textView.setText(getString(R.string.help_tips));
    }
}