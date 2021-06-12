package com.databaseproj.caltracker.view;

import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.model.Product;

import java.text.DecimalFormat;

public class AddProductActivity extends AppCompatActivity {
    private static final float gramsPerPound = new Float(453.59237);
    private static final float mililitersPerOunce = new Float(28.413);
    private static DecimalFormat df = new DecimalFormat("###.#");


    private EditText  caloriesEditText, fatEditText;
    private EditText carbohydratesEditText, nameEditText, proteinEditText;



    private Product product, tempProduct;
    private int id;


    private SettingsManager settingsManager;
    private boolean USunits, isFood;
    private String unitsString;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.product_add);

        isFood = true;
        USunits = false;
        unitsString = "gram";

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsManager = SettingsManager.getInstance(this);
        if (settingsManager.getUnits().equals(SettingsManager.EU_UNITS)) {
            USunits = false;
        } else {
            USunits = true;
        }



        caloriesEditText = (EditText) findViewById(R.id.edittext2);
        proteinEditText = (EditText) findViewById(R.id.edittext3);
        carbohydratesEditText = (EditText) findViewById(R.id.edittext4);
        fatEditText = (EditText) findViewById(R.id.edittext5);
        nameEditText = (EditText) findViewById(R.id.edittext1);




        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                addToDatabase();

                product = tempProduct;

                prepareData();
                CharSequence text = "Successfully added to database";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt(ManualProductSearchActivity.PRODUCT_ID);
            //      barcodeTextView.setText(" No barcode!");
        }

        ProductsDatabaseManager databaseManager = new ProductsDatabaseManager(this);
        //		Cursor cursor;
        databaseManager.open();
        try {
            //barcode found
            product = databaseManager.getProductById(id);

            prepareData();
        } catch (SQLException exception) {
            //barcode not found
            exception.printStackTrace();


        }
        databaseManager.close();


    }

    private void prepareData() {
        float factor = 1;
        String postfix = " per";
        if (USunits) {
            if (isFood) {
                factor = gramsPerPound / 100;
                postfix += " 1 lb";
            } else {
                factor = mililitersPerOunce / 100;
                postfix += " 1 oz";
            }
        } else {
            if (isFood) {
                postfix += " 100 g";
            } else {
                postfix += " 100 ml";
            }
        }


    }


    private void addToDatabase() throws NumberFormatException {
        String name = nameEditText.getText().toString().trim();

        float amountToCalculate = Float.parseFloat("100"); //check
       // float calories = Float.parseFloat(caloriesEditText.getText().toString()); //catch exception

        float calories = 0.0f;
        if (caloriesEditText.getText().toString().equals("") == false){
            calories = Float.parseFloat(caloriesEditText.getText().toString());
        }
        float fat = 0.0f;
        if (fatEditText.getText().toString().equals("") == false){
            fat = Float.parseFloat(fatEditText.getText().toString());
        }
        float protein = 0.0f;
        if (proteinEditText.getText().toString().equals("") == false){
            protein = Float.parseFloat(proteinEditText.getText().toString());
        }
        float carbohydrates = 0.0f;
        if (carbohydratesEditText.getText().toString().equals("") == false){
            carbohydrates = Float.parseFloat(carbohydratesEditText.getText().toString());
        }





        //		Log.d("sum", carbohydrates + protein + fat + "");
        //		Log.d("amount", amountToCalculate * 1.01 + "");
        if (carbohydrates + protein + fat > amountToCalculate * 1.01) {
            throw new NumberFormatException();
        }

        if (isFood) {
            if (USunits) {
                amountToCalculate *= gramsPerPound;
            }
        } else {
            if (USunits) {
                amountToCalculate *= mililitersPerOunce;
            }
        }

        calories = calories * 100 / amountToCalculate;
        fat = fat * 100 / amountToCalculate;
        protein = protein * 100 / amountToCalculate;
        carbohydrates = carbohydrates * 100 / amountToCalculate;
        Log.d("saved product", "->" + name + "<=");

        tempProduct = new Product(name, isFood, calories, "0", fat, protein, carbohydrates);

        ProductsDatabaseManager databaseManager = new ProductsDatabaseManager(this);
        databaseManager.open();
        databaseManager.insertWorkout(tempProduct);
        databaseManager.close();
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}
