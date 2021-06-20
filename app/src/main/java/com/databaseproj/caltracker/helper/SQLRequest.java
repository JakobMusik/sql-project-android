package com.databaseproj.caltracker.helper;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.databaseproj.caltracker.view.FeatureActivity1;
import com.databaseproj.caltracker.view.GlobalClass;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SQLRequest {
    public static void post(String request, Context context) {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();

                OkHttpClient client = new OkHttpClient().newBuilder().build();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create("str=" + request, mediaType);
                Request request = new Request.Builder()
                        .url("https://iiww.cc/api/users/custom")
                        .method("POST", body)
                        .addHeader("key", "2899e614b19fac45600196785383d9d9")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build();

                try {
                    Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
                    Response response = client.newCall(request).execute();
                    GlobalClass.setResponse(response);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
