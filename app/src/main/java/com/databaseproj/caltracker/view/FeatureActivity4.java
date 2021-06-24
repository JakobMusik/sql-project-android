package com.databaseproj.caltracker.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.helper.SQLRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import okhttp3.Response;

public class FeatureActivity4 extends AppCompatActivity {


    private Button button1, button2, button3;
    private ListView listView;
    private String[] strings = new String[11];
    private ArrayList<String> resultStrings;
    private ArrayAdapter adapter;
    private SettingsManager settingsManager;

    public FeatureActivity4() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature4);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);
        settingsManager = SettingsManager.getInstance(this);
        resultStrings = new ArrayList<String>();

        for (int i = 0; i < 11; i++) {
            strings[i] = "";
        }
        resultStrings.clear();
        adapter = new ArrayAdapter<String>(FeatureActivity4.this, R.layout.activity_listview, resultStrings);
        listView.setAdapter(adapter);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultStrings.clear();
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(System.currentTimeMillis());
                String date_str = date.get(Calendar.DAY_OF_MONTH) + "_" + (date.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
                String str = "select name, sum(kcal) as sum from\n" +
                        "user_pref join intake i on user_pref.id = i.user_pref_id\n" +
                        "where date = '" + date_str + "'\n" +
                        "group by name\n" +
                        "order by sum\n" +
                        "limit 10;";
                sendQuery(str);

                /*new Thread(new Runnable() {
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
                }).start();*/
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultStrings.clear();
                String name_str = settingsManager.getName();

                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(System.currentTimeMillis());
                String date_str = date.get(Calendar.DAY_OF_MONTH) + "_" + (date.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
                String str = "with name_kcal as (\n    select name, sum(kcal) as sum from\n    user_pref join intake i on user_pref.id = i.user_pref_id\n    where date = '" +
                        date_str + "'\n    group by name\n)\nselect name, n1.sum from name_kcal n1\nwhere n1.sum > all (\n    select n2.sum from name_kcal n2\n    where n2.name = '" +
                        name_str + "'\n)\norder by sum desc\nlimit 10;";
                sendQuery(str);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultStrings.clear();
                String name_str = settingsManager.getName();

                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(System.currentTimeMillis());
                String date_str = date.get(Calendar.DAY_OF_MONTH) + "_" + (date.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
                String str = "with name_kcal as (\n    select name, sum(kcal) as sum from\n    user_pref join intake i on user_pref.id = i.user_pref_id\n    where date = '" +
                        date_str + "'\n    group by name\n)\nselect name, n1.sum from name_kcal n1\nwhere n1.sum < all (\n    select n2.sum from name_kcal n2\n    where n2.name = '" +
                        name_str + "'\n)\norder by sum\nlimit 10;";
                sendQuery(str);
            }
        });

    }

    private void sendQuery(String str) {
        SQLRequest.post(str, false, FeatureActivity4.this.getApplicationContext());
        Toast.makeText(FeatureActivity4.this, "Pls wait a second, don't spam the button...", Toast.LENGTH_SHORT).show();

        FeatureActivity4.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while (GlobalClass.isNull())
                    ;
                try {
                    getResult(GlobalClass.getResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getResult(Response response) throws IOException {

        Gson gson = new Gson();
        String str = response.body().string();
        JsonArray jsonArray = new JsonArray();
        try {
            jsonArray = gson.fromJson(str, JsonArray.class);
        } catch (JsonSyntaxException e) {
            Toast.makeText(FeatureActivity4.this, "Did you finished your profile upload?", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            Map map = gson.fromJson(jsonArray.get(i).toString(), Map.class);
            String s1 = map.get("name").toString();
            String s2 = map.get("sum").toString();
            resultStrings.add(map.get("name").toString() + " -- " + map.get("sum").toString() + " kcal.");
            strings[i] = map.get("name").toString() + " -- " + map.get("sum").toString() + " kcal.";

        }
        strings[jsonArray.size()] = "Today til now. Only first 10 are shown";
        resultStrings.add("Today til now. Only first 10 are shown");

    }


}


