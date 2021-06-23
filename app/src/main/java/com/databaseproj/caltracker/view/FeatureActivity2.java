package com.databaseproj.caltracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.helper.SQLRequest;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

//TODO
public class FeatureActivity2 extends AppCompatActivity {

    private Button Btn;
    private TextView textView, textView2;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature2);

        Btn = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        textView.setMovementMethod(new ScrollingMovementMethod());

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///String str = "select * from user_pref" + "\n" +
                            // "where id = 1;";
                String str = editText.getText().toString();
                SQLRequest.post(str, false, FeatureActivity2.this.getApplicationContext());
                Toast.makeText(FeatureActivity2.this, "Pls wait a second, dont spam the button...", Toast.LENGTH_SHORT);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        while (GlobalClass.isNull())
                            ;

                        try {
                            showResult(GlobalClass.getResponse());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void showResult(Response response) throws IOException {
        Gson gson = new Gson();
        //Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
        String str = response.body().string();
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray = gson.fromJson(str, JsonArray.class);
        } catch (JsonSyntaxException e) {
            Toast.makeText(FeatureActivity2.this, "Plz enter valid sql queries", Toast.LENGTH_SHORT).show();
            return;
        }

        //Test out..
        //Toast.makeText(context, jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();

        String result = jsonArray.toString();
        textView.setText(result);

        /*for (int i = 0; i < jsonArray.size(); i++) {
            Map map = gson.fromJson(jsonArray.get(i).toString(), Map.class);
            if (map.get("id").equals(1.0)) {
                Toast.makeText(context, map.get("name").toString(), Toast.LENGTH_LONG).show();
            }

        }*/

    }

}