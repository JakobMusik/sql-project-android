package com.databaseproj.caltracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.helper.SQLRequest;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

//TODO
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
                String str = "select * from users" + "\n" +
                             "where ID = 1;";
                SQLRequest.post(str, FeatureActivity2.this.getApplicationContext());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        while (GlobalClass.isNull())
                            ;

                        try {
                            showResult(GlobalClass.getResponse(), FeatureActivity2.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void showResult(Response response, Context context) throws IOException {
        Gson gson = new Gson();
        //Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
        String str = response.body().string();
        JsonArray jsonArray = new JsonArray();
        jsonArray = gson.fromJson(str, JsonArray.class);

        //Test out..
        //Toast.makeText(context, jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();

        for (int i = 0; i < jsonArray.size(); i++) {
            Map map = gson.fromJson(jsonArray.get(i).toString(), Map.class);
            if (map.get("id").equals(1.0)) {
                Toast.makeText(context, map.get("name").toString(), Toast.LENGTH_LONG).show();
            }

        }
    }

}