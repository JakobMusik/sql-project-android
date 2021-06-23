package com.databaseproj.caltracker.helper;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.view.GlobalClass;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

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
        String gender = settingsManager.getGender();
        String email = settingsManager.getEmail();
        int exercise = settingsManager.getExercise();

        SQLRequest.post("select * from user_pref\n" +
                               "where name = '" + name + "' and email = '" + email + "';", false, context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                while (GlobalClass.isNull())
                    ;

                Response response = GlobalClass.getResponse();
                Gson gson = new Gson();
                //Toast.makeText(context, response.body().toString(), Toast.LENGTH_LONG).show();
                String str = null;
                try {
                    str = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonArray jsonArray = gson.fromJson(str, JsonArray.class);

                if (jsonArray.size() == 0) {

                    GlobalClass.clear();
                    SQLRequest.post("insert into user_pref (name, email, age, gender, unit, weight, height, exercise)\n" +
                            "values ('" + name + "', '" + email + "', " + age +
                            ", '" + gender + "', '" + unit + "', " + weight +
                            ", " + height + ", " + exercise + ");", false, context);
                    while (GlobalClass.isNull())
                        ;
                    GlobalClass.clear();

                    SQLRequest.post("select id from user_pref where name = '" + name + "';", false, context);
                    while (GlobalClass.isNull())
                        ;
                    try {
                        Gson gson1 = new Gson();
                        str = GlobalClass.getResponse().body().string();
                        JsonArray jsonArray1 = gson1.fromJson(str, JsonArray.class);
                        Map map = gson1.fromJson(jsonArray1.get(0).toString(), Map.class);
                        int res_int = (int)Float.parseFloat(map.get("id").toString());
                        settingsManager.setID(res_int, context.getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    GlobalClass.setConflict(true);
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    String email = jsonObject.get("email").getAsString();
                    int age = jsonObject.get("age").getAsInt();
                    String gender = jsonObject.get("gender").getAsString();
                    String unit = jsonObject.get("unit").getAsString();
                    int weight = jsonObject.get("weight").getAsInt();
                    int height = jsonObject.get("height").getAsInt();
                    int exercise = jsonObject.get("exercise").getAsInt();
                    int id = jsonObject.get("id").getAsInt();

                    SettingsManager settingsManager = SettingsManager.getInstance(context);
                    settingsManager.setName(name, context);
                    settingsManager.setEmail(email, context);
                    settingsManager.setAge(age, context);
                    settingsManager.setGender(gender, context);
                    settingsManager.setUnits(unit, context);
                    settingsManager.setWeight(weight, context);
                    settingsManager.setHeight(height, context);
                    settingsManager.setExercise(exercise, context);
                    settingsManager.setID(id, context);

                    Toast.makeText(context, "User already exist! Syncing..", Toast.LENGTH_LONG).show();

                }
            }
        }).start();


    }
}
