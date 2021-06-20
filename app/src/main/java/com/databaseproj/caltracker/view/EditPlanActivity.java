package com.databaseproj.caltracker.view;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.SettingsManager;

import java.text.DecimalFormat;

public class EditPlanActivity extends AppCompatActivity {
    private static final float gramsPerPound = new Float(453.59237);
    private static DecimalFormat df = new DecimalFormat("###");
    private EditText caloriesEditText, fatEditText;
    private EditText carbohydratesEditText, proteinEditText;

    private SettingsManager settingsManager;
    private boolean USunits;
    private FloatingActionButton fab;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.configure_diet);
        settingsManager = SettingsManager.getInstance(EditPlanActivity.this);
        USunits = false;

        settingsManager = SettingsManager.getInstance(this);
        if (settingsManager.getUnits().equals(SettingsManager.EU_UNITS)) {
            USunits = false;


        } else {
            USunits = true;


        }


        caloriesEditText = (EditText) findViewById(R.id.caloriesValueEditText);
        proteinEditText = (EditText) findViewById(R.id.proteinValueEditText);
        carbohydratesEditText = (EditText) findViewById(R.id.carbohydratesValueEditText);
        fatEditText = (EditText) findViewById(R.id.fatValueEditText);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                save();
                finish();

            }
        });


        if (settingsManager.getUnits().equals(SettingsManager.EU_UNITS)) {
            USunits = false;
            calculateValuesMetric();

        } else {
            USunits = true;
            calculateValuesImperial();

        }

        prepareData();
    }

    private void prepareData() {
        String hint;
        float factor = 1;

        if (USunits) {
            hint = " lb";
            factor = 1 / gramsPerPound;
        } else {
            hint = " grams";
        }


    }

    private void save() {
        float factor = 1;
        if (USunits) {
            factor = gramsPerPound;
        }

        try {
            float calories = Float.parseFloat(caloriesEditText.getText().toString());
            settingsManager.setCaloriesRequirement(calories, EditPlanActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            float fat = Float.parseFloat(fatEditText.getText().toString());
            fat = fat * factor;
            settingsManager.setFatRequirement(fat, EditPlanActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            float protein = Float.parseFloat(proteinEditText.getText().toString());
            protein = protein * factor;
            settingsManager.setProteinRequirement(protein, EditPlanActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            float carbohydrates = Float.parseFloat(carbohydratesEditText.getText().toString());
            carbohydrates = carbohydrates * factor;
            settingsManager.setCarbohydratesRequirement(carbohydrates, EditPlanActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(EditPlanActivity.this, "Plan saved..", Toast.LENGTH_SHORT).show();
    }

    private void calculateValuesMetric() {


        String hint;
        float factor = 1;

        if (USunits) {
            hint = " lb";
            factor = 1 / gramsPerPound;
        } else {
            hint = " grams";
        }


        double height = settingsManager.getHeight();
        double weight = settingsManager.getWeight();
        double age = settingsManager.getAge();


        boolean isMan = true;
        if (settingsManager.getSex().equals(settingsManager.getSexValuesTable()[1])) {
            isMan = false;
        }

        double bmr2;

        if (isMan) {

            // Male
            bmr2 = (10 * weight + 6.25 * height - 5 * age + 5);

        } else {

            // female
            bmr2 = (10 * weight + 6.25 * height - 5 * age - 161);


        }


        float activity;


        if (settingsManager.getExercise().equals(SettingsManager.SEDENTARY)) {
            activity = (float) 1.25;
        } else if (settingsManager.getExercise().equals(SettingsManager.LIGHT)) {
            activity = (float) 1.375;
        } else if (settingsManager.getExercise().equals(SettingsManager.MODERATE)) {
            activity = (float) 1.55;
        } else if (settingsManager.getExercise().equals(SettingsManager.HEAVY)) {
            activity = (float) 1.725;
        } else if (settingsManager.getExercise().equals(SettingsManager.VERYHEAVY)) {
            activity = (float) 1.9;
        } else {
            activity = (float) 1;
        }


        float calories = (float) (bmr2 * activity);

        float proteins = (float) ((0.3 * calories) / 4);
        float carbohydrates = (float) ((0.55 * calories) / 4);
        float fats = (float) ((0.15 * calories) / 9);

        caloriesEditText.setText(new String(df.format(calories)).replace(",", "."));
        proteinEditText.setText(new String(df.format(proteins * factor)).replace(",", "."));
        carbohydratesEditText.setText(new String(df.format(carbohydrates * factor)).replace(",", "."));
        fatEditText.setText(new String(df.format(fats * factor)).replace(",", "."));
    }


    private void calculateValuesImperial() {
        String hint;
        float factor = 1;

        if (USunits) {
            hint = " lb";
            factor = 1 / gramsPerPound;
        } else {
            hint = " grams";
        }


        double height = (settingsManager.getHeight() * 2.54);
        double weight = settingsManager.getWeight() * 0.453592;
        double age = settingsManager.getAge();


        boolean isMan = true;
        if (settingsManager.getSex().equals(settingsManager.getSexValuesTable()[1])) {
            isMan = false;
        }

        double BMR;

        if (isMan) {

            //BMR for Men

            BMR = (10 * weight + 6.25 * height - 5 * age + 5);
        } else {

            // BMR for Women
            BMR = (10 * weight + 6.25 * height - 5 * age - 161);


        }


        float activity;

        if (settingsManager.getExercise().equals(SettingsManager.SEDENTARY)) {
            activity = (float) 1.25;
        } else if (settingsManager.getExercise().equals(SettingsManager.LIGHT)) {
            activity = (float) 1.375;
        } else if (settingsManager.getExercise().equals(SettingsManager.MODERATE)) {
            activity = (float) 1.55;
        } else if (settingsManager.getExercise().equals(SettingsManager.HEAVY)) {
            activity = (float) 1.725;
        } else if (settingsManager.getExercise().equals(SettingsManager.VERYHEAVY)) {
            activity = (float) 1.9;
        } else {
            activity = (float) 1;
        }

        float calories = (float) BMR * activity;

        float proteins = (float) ((0.3 * calories) / 4);
        float carbohydrates = (float) ((0.55 * calories) / 4);
        float fats = (float) ((0.15 * calories) / 9);

        caloriesEditText.setText(new String(df.format(calories)).replace(",", "."));
        proteinEditText.setText(new String(df.format(proteins * factor)).replace(",", ".") + " lb");
        carbohydratesEditText.setText(new String(df.format(carbohydrates * factor)).replace(",", ".") + " lb");
        fatEditText.setText(new String(df.format(fats * factor)).replace(",", ".") + " lb");
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "you cannot go back. finish it", Toast.LENGTH_LONG).show();
    }

}