package com.databaseproj.caltracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.helper.SQLRequest;


public class FeatureActivity2 extends AppCompatActivity {

    private Button Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature2);

        Btn = findViewById(R.id.button);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "select * from users;";
                SQLRequest.post(str, FeatureActivity2.this.getApplicationContext());
            }
        });
    }


}