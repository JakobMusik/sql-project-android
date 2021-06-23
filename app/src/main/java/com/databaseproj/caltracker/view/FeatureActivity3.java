package com.databaseproj.caltracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.databaseproj.caltracker.R;

public class FeatureActivity3 extends AppCompatActivity {

    private EditText editText1;
    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature3);

        editText1 = findViewById(R.id.editText1);
        btn1 = findViewById(R.id.button2);
        btn2 = findViewById(R.id.button3);

    }



}