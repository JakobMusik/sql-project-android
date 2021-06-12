package com.databaseproj.caltracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.helper.ExifUtil;
import com.databaseproj.caltracker.helper.WriteLog;
import com.github.clans.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeatureActivity1 extends AppCompatActivity {


    private FloatingActionButton cameraBtn;
    private Button apiBtn;
    private TextView resultText;
    private ImageView resultImage;

    private File imageFile;
    private Bitmap orientedBitmap;

    private boolean fileStatus = false;

    private String url = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/food/images/classify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature1);

        cameraBtn = findViewById(R.id.cameraBtn);
        apiBtn = findViewById(R.id.apiBtn);
        resultText = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImage);

        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fileStatus) {
                    Toast.makeText(FeatureActivity1.this, "File not ready or not exist.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    /*Map<String, String> params = new HashMap<>();
                    params.put("file", upImage);
                    final JSONObject parameters = new JSONObject(params);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            textView.setText(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {


                            Map<String, String> params = new HashMap<>();
                            params.put("file", orientedBitmap);
                        return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String,String> params = new HashMap<>();
                            params.put("content-type","application/form-data");
                            params.put("x-rapidapi-key", "1bd92af9eemshd47ab55704f7b59p1ed9e5jsn92e9d76618c0");
                            params.put("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com");
                            return params;
                        }
                    };
                    RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(stringRequest);*/

                    Toast.makeText(FeatureActivity1.this, "Sending Picture, Please wait...", Toast.LENGTH_LONG).show();

                    new Thread(new Runnable() {
                        public void run() {
                            OkHttpClient client = new OkHttpClient();

                            /*Request request = new Request.Builder()
                                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/quickAnswer?q=How%20much%20vitamin%20c%20is%20in%202%20apples%3F")
                                    .get()
                                    .addHeader("x-rapidapi-key", "1bd92af9eemshd47ab55704f7b59p1ed9e5jsn92e9d76618c0")
                                    .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                                    .build();new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", "file",*/

                            MediaType mediaType = MediaType.parse("multipart/form-data");
                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", "image.bmp", RequestBody.create(imageFile, mediaType))
                                    .build();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .addHeader("x-rapidapi-key", "1bd92af9eemshd47ab55704f7b59p1ed9e5jsn92e9d76618c0")
                                    .addHeader("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                                    .build();


                            try {
                                Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
                                Response response = client.newCall(request).execute();
                                resultText.setText(response.body().string());
                                WriteLog.writeToFile(response.toString(), FeatureActivity1.this);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeatureActivity1.this, CameraActivity.class);
                /*startActivityForResult(intent, 2);*/
                startActivityForResult(intent, 1);
            }
        });
    }

    public String getStringImage(Bitmap bm) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, ba);
        byte[] imageByte = ba.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            /*Bundle ex = data.getExtras();
            Bitmap bitmap = ex.getParcelable("BitmapImage");
            imageView.setImageBitmap(bitmap);*/
            File fileDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imageFile = new File(fileDirectory, "image.bmp");
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
            orientedBitmap = ExifUtil.rotateBitmap(imageFile.getPath(), bitmap);

            resultImage.setImageBitmap(orientedBitmap);
            fileStatus = true;

            /*String binaryImage = getStringImage(orientedBitmap);
            WriteLog.writeToFile(binaryImage, getContext());*/

        }
        if (requestCode == 2) {
            Toast.makeText(FeatureActivity1.this, "(reserved feature)", Toast.LENGTH_SHORT).show();
        }
    }


}

