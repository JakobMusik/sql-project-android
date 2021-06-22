package com.databaseproj.caltracker.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.view.FeatureActivity2;
import com.databaseproj.caltracker.view.GlobalClass;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Response;

public class UpdateRemoteDB {
    public static void update(Context context) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SettingsManager settingsManager = SettingsManager.getInstance(context);

        DecimalFormat df = new DecimalFormat("##");
        int height = (settingsManager.getHeight());
        int weight = (settingsManager.getWeight());
        String age = df.format(settingsManager.getAge());
        String unit = settingsManager.getUnits();
        String name = settingsManager.getName();
        String sex = settingsManager.getSex();
        String email = settingsManager.getEmail();
        int exercise = settingsManager.getExercise();

        SQLRequest.post("select * from user_pref\n" +
                               "where name = '" + name + "' and email = '" + email + "';", context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                while (GlobalClass.isNull())
                    ;

                Response response = GlobalClass.getResponse();
                Gson gson = new Gson();
                //Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
                String str = response.body().toString();
                JsonArray jsonArray = new JsonArray();
                jsonArray = gson.fromJson(str, JsonArray.class);

                if (jsonArray.size() == 0) {
                    SQLRequest.post("insert into user_pref (name, email, age, sex, unit, weight, height, exercise)\n" +
                            "values ('" + name + "', '" + email + "', '" + age +
                            "', '" + sex + "', '" + unit + "', '" + weight +
                            "', '" + height + "', '" + exercise + "');", context);
                    GlobalClass.clear();
                }
                else {
                    GlobalClass.setConflict(true);
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    String email = jsonObject.get("email").getAsString();
                    int age = jsonObject.get("age").getAsInt();
                    String sex = jsonObject.get("sex").getAsString();
                    String unit = jsonObject.get("unit").getAsString();
                    int weight = jsonObject.get("weight").getAsInt();
                    int height = jsonObject.get("height").getAsInt();
                    int exercise = jsonObject.get("exercise").getAsInt();

                    SettingsManager settingsManager = SettingsManager.getInstance(context);
                    settingsManager.setName(name, context);
                    settingsManager.setEmail(email, context);
                    settingsManager.setAge(age, context);
                    settingsManager.setSex(sex, context);
                    settingsManager.setUnits(unit, context);
                    settingsManager.setWeight(weight, context);
                    settingsManager.setHeight(height, context);
                    settingsManager.setExercise(exercise, context);

                    Toast.makeText(context, "User already exist! Syncing..", Toast.LENGTH_LONG).show();

                }
            }
        }).start();


    }
}
