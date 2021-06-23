package com.databaseproj.caltracker.view;

import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.helper.SQLRequest;
import com.databaseproj.caltracker.model.Product;
import com.databaseproj.caltracker.model.ShowEatenProduct;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowProductActivity extends AppCompatActivity {
    private static final float gramsPerPoun = new Float(453.59237);
    private static final float gramsPerOunce = new Float(28.3495);

    private static final float mililitersPerOunce = new Float(28.413);
    private static DecimalFormat df = new DecimalFormat("###");

    private TextView productNameTextView, proteinTextView;
    private TextView carbohydratesTextView, fatTextView, caloriesTextView, header;

    private EditText amountEditText, caloriesEditText, fatEditText;
    private EditText carbohydratesEditText, nameEditText, proteinEditText;

    private ArrayList<RowModel> productsList;
    private ProductsDatabaseManager manager;
    private ArrayList<Product> productsArrayList;


    private Button addButton, cancelButton;

    private Product product, tempProduct;
    private int id;
    private boolean barcodeFound;

    private SettingsManager settingsManager;
    private boolean USunits, isFood;
    private String unitsString;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.product_show);

        isFood = true;
        USunits = false;
        unitsString = "gr";

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsManager = SettingsManager.getInstance(this);
        if (settingsManager.getUnits().equals(SettingsManager.EU_UNITS)) {
            USunits = false;
        } else {
            USunits = true;
        }

        header = (TextView) findViewById(R.id.chooseAmountHeaderTextView);

        productNameTextView = (TextView) findViewById(R.id.productNameValueTextView);
        proteinTextView = (TextView) findViewById(R.id.proteinValueTextView);
        carbohydratesTextView = (TextView) findViewById(R.id.carbohydratesValueTextView);
        fatTextView = (TextView) findViewById(R.id.fatValueTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesValueTextView);

        amountEditText = (EditText) findViewById(R.id.amountValueEditText);

        caloriesEditText = (EditText) findViewById(R.id.caloriesValueEditText);
        proteinEditText = (EditText) findViewById(R.id.proteinValueEditText);
        carbohydratesEditText = (EditText) findViewById(R.id.carbohydratesValueEditText);
        fatEditText = (EditText) findViewById(R.id.fatValueEditText);
       // nameEditText = (EditText) findViewById(R.id.productNameEditText);

        addButton = (Button) findViewById(R.id.addButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);


        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt(ManualProductSearchActivity.PRODUCT_ID);

        }

        ProductsDatabaseManager databaseManager = new ProductsDatabaseManager(this);
        databaseManager.open();
        try {

            product = databaseManager.getProductById(id);
            barcodeFound = true;
            prepareData();
        } catch (SQLException exception) {
            exception.printStackTrace();
            barcodeFound = false;
            addButton.setText(getApplicationContext().getString(R.string.add_to_database_button_string));
        }
        databaseManager.close();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                if (barcodeFound) {

                    addToDiet();

                } else {
                    try {
                        addToDatabase();
                        Toast.makeText(ShowProductActivity.this, "test!!", Toast.LENGTH_LONG).show();
                        product = tempProduct;
                        barcodeFound = true;
                        prepareData();

                        CharSequence text = "Successfully added to database";
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();

                        CharSequence text = "Check typed in values";
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
             //   finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               finish();
            }
        });

    }

    private void prepareData() {
        float factor = 1;
        String postfix = "";
        if (USunits) {
            if (isFood) {
                factor = gramsPerOunce / 100;
                postfix += " 1 oz";
            } else {
                factor = mililitersPerOunce / 100;
                postfix += " 1 fl oz";
            }
        } else {
            if (isFood) {
                postfix += " 100 gr";
            } else {
                postfix += " 100 ml";
            }
        }

        productNameTextView.setText(product.getName());
        proteinTextView.setText(df.format(product.getProtein() * factor) + " " + unitsString );
        carbohydratesTextView.setText(df.format(product.getCarbohydrates() * factor) + " " + unitsString );
        fatTextView.setText(df.format(product.getFat() * factor) + " " + unitsString );
        caloriesTextView.setText(df.format(product.getCalories() * factor));


        addButton.setText(getApplicationContext().getString(R.string.add_to_diet_button_string));
     //   amountEditText.setHint(getApplicationContext().getString(R.string.value_per_hint1) + " " + unitsString);
    }

    private void addToDiet() {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(System.currentTimeMillis());
        String productPointsFileName = date.get(Calendar.DAY_OF_MONTH) + "_" + (date.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
        ArrayList<ShowEatenProduct> showeatenProducts = new ArrayList<ShowEatenProduct>();

        float protein, carbohydrates, fats, calories;
        long time;
        int amount;
        String name;
        boolean isFood;

        try {
            FileInputStream inputStream = openFileInput(productPointsFileName);
            FileDescriptor descriptor = null;
            try {
                descriptor = inputStream.getFD();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            FileReader fileReader = new FileReader(descriptor);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            String array[];

            try {
                while ((line = reader.readLine()) != null) {
                    array = line.split(" ");
                    amount = Integer.parseInt(array[0].trim());
                    protein = Float.parseFloat(array[1].trim());
                    carbohydrates = Float.parseFloat(array[2].trim());
                    fats = Float.parseFloat(array[3].trim());
                    calories = Float.parseFloat(array[4].trim());
                    time = Long.parseLong(array[5].trim());
                    isFood = Boolean.parseBoolean(array[6].trim());
                    name = array[6];
                    for (int i = 7; i < array.length; i++) {
                        name += " " + array[i];
                    }
                    ShowEatenProduct product = new ShowEatenProduct(amount, protein, carbohydrates, fats, calories, time,  name);
                    showeatenProducts.add(product);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //add my product

        if (amountEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Please input a valid number (grams)", Toast.LENGTH_LONG).show();
            return;
        }
        amount = Integer.parseInt(amountEditText.getText().toString());
        //  amountToCalculate *= gramsPerPound;

        //amount = Float.valueOf(amountToCalculate).intValue();

        time = System.currentTimeMillis();
        protein = (float) amount * product.getProtein() / 100;
        carbohydrates = (float) amount * product.getCarbohydrates() / 100;
        fats = (float) amount * product.getFat() / 100;
        calories = (float) amount * product.getCalories() / 100;

        name = product.getName();
        isFood = product.isFood();

        SQLRequest.post("insert into intake (user_pref_id, product_id, kcal, date)\n" +
                "values(" + settingsManager.getID() + ", " + id + ", " + calories + ", '" + productPointsFileName + "');", true, ShowProductActivity.this);

        ShowEatenProduct product = new ShowEatenProduct(amount, protein, carbohydrates, fats, calories, time,  name);
        showeatenProducts.add(product);

        try {
            FileOutputStream fos = openFileOutput(productPointsFileName, Context.MODE_PRIVATE);
            PrintWriter out = new PrintWriter(fos);

            for (int i = 0; i < showeatenProducts.size(); i++) {
                product = showeatenProducts.get(i);
                out.print(product.getAmount() + " ");
                out.print(product.getProtein() + " ");
                out.print(product.getCarbohydrates() + " ");
                out.print(product.getFat() + " ");
                out.print(product.getCalories() + " ");
                out.print(product.getTime() + " ");

                out.println(product.getName());
            }

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Successfully added to diet", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addToDatabase() throws NumberFormatException {
        String name = nameEditText.getText().toString().trim();
        float amountToCalculate = Float.parseFloat(amountEditText.getText().toString()); //check
        float calories = Float.parseFloat(caloriesEditText.getText().toString()); //catch exception
        float fat = Float.parseFloat(fatEditText.getText().toString());
        float protein = Float.parseFloat(proteinEditText.getText().toString());
        float carbohydrates = Float.parseFloat(carbohydratesEditText.getText().toString());


        if (carbohydrates + protein + fat > amountToCalculate * 1.01) {
            throw new NumberFormatException();
        }

            //    amountToCalculate *= gramsPerPound;


        calories = calories * 100 / amountToCalculate;
        fat = fat * 100 / amountToCalculate;
        protein = protein * 100 / amountToCalculate;
        carbohydrates = carbohydrates * 100 / amountToCalculate;
        Log.d("save product", "->" + name + "<=");

        tempProduct = new Product(name, isFood, calories, "0", fat, protein, carbohydrates);

        ProductsDatabaseManager databaseManager = new ProductsDatabaseManager(this);
        databaseManager.open();
        databaseManager.insertWorkout(tempProduct);
        databaseManager.close();
    }


    /**
     * Let the user tap the activity icon to go 'home'.
     * Requires setHomeButtonEnabled() in onCreate().
     */
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
