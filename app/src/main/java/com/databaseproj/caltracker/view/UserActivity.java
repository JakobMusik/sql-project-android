package com.databaseproj.caltracker.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.LabelledSpinner;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;


public class UserActivity extends AppCompatActivity {

    public static final String EU_UNITS = "km / kg";
    public static final String US_UNITS = "mile / pound";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    private LabelledSpinner exercise_spinner;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private Button startButton;
    private EditText ageET, weightET, heightET;
    private SettingsManager settingsManager;
    private TextView termsTextView;

    private CheckBox imperial, metric, man, woman;
    private boolean CheckStatus_1 = false;
    private boolean CheckStatus_2 = false;

    private ProductsDatabaseManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Prevent SoftKeyboard to pop up on start
        verifyPermissions(this);

        settingsManager = SettingsManager.getInstance(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        exercise_spinner = (LabelledSpinner) findViewById(R.id.exercise_spinner);

        startButton = (Button) findViewById(R.id.activity_hello_button_start);

        termsTextView = (TextView) findViewById(R.id.helloactivity_textview_terms);

        ageET = (EditText) findViewById(R.id.activity_hello_age);

        weightET = (EditText) findViewById(R.id.activity_hello_weight);

        heightET = (EditText) findViewById(R.id.activity_hello_height);

        exercise_spinner.setItemsArray(R.array.helloactivity_daily_exercise);

        manager = new ProductsDatabaseManager(this);
        manager.open();


        SharedPreferences prefs = getSharedPreferences("useractivity", MODE_PRIVATE);
        if (prefs.getBoolean("filldata", false)) {

      //      prefs.edit().putBoolean("firstrun", false).apply();

            DecimalFormat df = new DecimalFormat("##");
            int height = (settingsManager.getHeight());
            int weight = (settingsManager.getWeight());
            String age = df.format(settingsManager.getAge());

            heightET.setText(String.valueOf(height));
            weightET.setText(String.valueOf(weight));
            ageET.setText((age));

        } else {

            prefs.edit().putBoolean("filldata", true).apply();

            heightET.setText("");
            weightET.setText("");
            ageET.setText("");

            try {
                //	backupDatabase();
                copyDataBase();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }


        imperial = (CheckBox) findViewById(R.id.imperial);
        imperial.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UserActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SettingsManager.PREFERENCES_UNITS_TYPE_KEY, US_UNITS);
                    editor.apply();
                    metric.setChecked(false);

                    TextInputLayout til = (TextInputLayout) findViewById(R.id.til);
                    TextInputLayout til2 = (TextInputLayout) findViewById(R.id.til2);
                    til.setHint("Height in inches");
                    til2.setHint("Weight in lbs");
                    DecimalFormat df = new DecimalFormat("##");
                    int height = (settingsManager.getHeight());
                    int weight = (settingsManager.getWeight());
                    String age = df.format(settingsManager.getAge());

                    heightET.setText(String.valueOf(height));
                    weightET.setText(String.valueOf(weight));
                    ageET.setText((age));

                    CheckStatus_1 = true;
                }
                else {
                    CheckStatus_1 = false;
                }

            }
        });


        metric = (CheckBox) findViewById(R.id.metric);
        metric.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UserActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SettingsManager.PREFERENCES_UNITS_TYPE_KEY, EU_UNITS);
                    editor.apply();
                    imperial.setChecked(false);

                    TextInputLayout til = (TextInputLayout) findViewById(R.id.til);
                    TextInputLayout til2 = (TextInputLayout) findViewById(R.id.til2);
                    til.setHint("Height in cm");
                    til2.setHint("Weight in kg");
                    DecimalFormat df = new DecimalFormat("##");
                    int height = (settingsManager.getHeight());
                    int weight = (settingsManager.getWeight());
                    String age = df.format(settingsManager.getAge());

                    heightET.setText(String.valueOf(height));
                    weightET.setText(String.valueOf(weight));
                    ageET.setText((age));

                    CheckStatus_1 = true;
                }
                else {
                    CheckStatus_1 = false;
                }

            }
        });


        man = (CheckBox) findViewById(R.id.man);
        man.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UserActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SettingsManager.PREFERENCES_USER_SEX_KEY, MALE);
                    editor.apply();
                    //    settingsManager.setUnits(table[choosenPosition], UserActivity.this);
                    woman.setChecked(false);

                    CheckStatus_2 = true;
                }
                else {
                    CheckStatus_2 = false;
                }

            }
        });


        woman = (CheckBox) findViewById(R.id.woman);
        woman.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UserActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SettingsManager.PREFERENCES_USER_SEX_KEY, FEMALE);
                    editor.apply();

                    man.setChecked(false);

                    CheckStatus_2 = true;
                }
                else {
                    CheckStatus_2 = false;
                }

            }
        });


        final Drawable pinkArrow = ContextCompat.getDrawable(UserActivity.this, R.drawable.ic_navigate_next_pink_24px);
        pinkArrow.setBounds(0, 0, 60, 60);
        startButton.setCompoundDrawables(null, null, pinkArrow, null);


        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(UserActivity.this, LicenseActivity.class);
            startActivity(intent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onStartClicked();
            }
        });


    }


    public void onStartClicked() {

        if (ageET.getText() == null || heightET.getText() == null || weightET.getText() == null
                || exercise_spinner.getSpinner().getSelectedItemPosition() == 0
                || !CheckStatus_1 || !CheckStatus_2) {
            Toast.makeText(UserActivity.this, "Don't leave blank plz.", Toast.LENGTH_SHORT).show();
            return;
        }

        settingsManager.setAge(Integer.parseInt(ageET.getText().toString()), this);
        settingsManager.setHeight(Integer.parseInt(heightET.getText().toString()), this);
        settingsManager.setWeight(Integer.parseInt(weightET.getText().toString()), this);
        settingsManager.setExercise(exercise_spinner.getSpinner().getSelectedItemPosition(), this);

        Intent intent = new Intent(this, EditPlanActivity.class);
        startActivity(intent);

        finish();

    }


    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = this.getAssets().open("products.db");

        // Path to the just created empty db
        //String outFileName = "/data/data/com.databaseproj.caltracker/databases/" + "products.db";
        String outFileName = getDatabasePath("products.db").getPath();

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    public static void verifyPermissions(Activity activity) {
        // Check if we have write permission
        for (String permission : PERMISSIONS_STORAGE) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 0);
                break;
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            //	backupDatabase();
            copyDataBase();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}

