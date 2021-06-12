package com.databaseproj.caltracker.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.clans.fab.FloatingActionMenu;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.fragments.DashboardFragment;
import com.databaseproj.caltracker.fragments.FoodFragment;
import com.databaseproj.caltracker.model.EatenProduct;
import com.databaseproj.caltracker.model.ShowEatenProduct;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import antonkozyriatskyi.circularprogressindicator.PatternProgressTextAdapter;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    public static final String PRODUCT_ID = "product_id";
    static final int DATE_DIALOG_ID = 0;
    static final int SUGGEST_DIALOG = 1;
    private static final float gramsPerPound = new Float(453.59237);

    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout bottomSheetDelete;
    LinearLayout bottomSheetAddRecall;

    FloatingActionMenu fab;
    SharedPreferences prefs = null;
    private ListView productsListView;
    private TextView textView;
    private ProductsDatabaseManager manager;
    private ArrayAdapter<RowModelSearch> arrayAdapter;
    private ArrayList<RowModelSearch> productsList;
    private SettingsManager settingsManager;
    private float wholeProteins, wholeCarbohydrates, wholeFats, wholeCalories;
    private ArrayList<EatenProduct> eatenProducts;
    private ArrayList<ShowEatenProduct> showeatenProducts;

    private Calendar tempDate;
    private TextView caloriesEditText, fatEditText, carbohydratesEditText, proteinEditText, remainCalories;
    private int mYear;
    private int mMonth;
    private int mDay;
    static ChipNavigationBar bottomNavBar;
    Fragment fragment;
    FragmentManager fragmentManager;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            tempDate.set(mYear, mMonth, mDay);
            prepareProductsList(productsListView, productsList, tempDate);
        }
    };
    ArcProgress arcProgress1;
    CircularProgressIndicator circularProgressIndicator;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.dashboard);



      //  mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

       // bottomSheetDelete = (LinearLayout) findViewById(R.id.bottom_sheet_delete);

      //  bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDelete);


        manager = new ProductsDatabaseManager(this);
        manager.open();

        prefs = getSharedPreferences("useractivity", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).apply();
            Intent intent = new Intent(Dashboard.this, UserActivity.class);
            startActivity(intent);
        } else {

            //  Toast toast = Toast.makeText(this, "Toast", Toast.LENGTH_SHORT);
            //  toast.show();

        }


        textView = (TextView) findViewById(R.id.AnalyzeTextView);
        productsListView = (ListView) this.findViewById(R.id.AnalyzeList);
        settingsManager = SettingsManager.getInstance(this);
        caloriesEditText = (TextView) findViewById(R.id.calorietext);
        proteinEditText = (TextView) findViewById(R.id.proteinValueEditText);
        carbohydratesEditText = (TextView) findViewById(R.id.carbohydratesValueEditText);
        fatEditText = (TextView) findViewById(R.id.fatValueEditText);
        remainCalories = (TextView) findViewById(R.id.calorietext);
        bottomNavBar = (ChipNavigationBar) findViewById(R.id.bottomNavBar);

        tempDate = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        fab = (FloatingActionMenu) findViewById(R.id.menu1);
        fab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
            }
        });
        fab.setClosedOnTouchOutside(true);

        com.github.clans.fab.FloatingActionButton fab1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab12);
        com.github.clans.fab.FloatingActionButton fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab22);


        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);


        fragmentManager = getSupportFragmentManager();

        fragment = new DashboardFragment();
        bottomNavBar.setItemSelected(R.id.one, true);
        fragmentManager.beginTransaction()
                .replace(R.id.coordinatorLayout, fragment)
                .commit();


        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                fragment = null;

                switch (id) {

                    case R.id.one:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.two:
                        fragment = new FoodFragment();
                        break;
                    case R.id.three:
                        fragment = new FoodFragment();

                    default:
                        break;

                }

                if (fragment != null) {

                    fragmentManager.beginTransaction()
                            .replace(R.id.coordinatorLayout, fragment)
                            .commit();

                } else {

                    Log.i("ERROR", "IN FRAGMENT LOOK HERE!!!!!!!!!!!!!!!");

                }

            }
        });






    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        TextView empty = (TextView) findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.AnalyzeList);
        list.setEmptyView(empty);
    }




    @Override
    public void onResume() {
        super.onResume();

        wholeProteins = wholeCarbohydrates = wholeFats = wholeCalories = 0;
        productsList = new ArrayList<RowModelSearch>();

        tempDate = Calendar.getInstance();
        tempDate.setTimeInMillis(System.currentTimeMillis());

        arrayAdapter = new RowAdapter(this, productsList);

        productsListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
                //show details dialog
            }
        });

        productsListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showAlertDialog(arg2);
                return true;
            }

        });

        productsListView.setAdapter(arrayAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            productsListView.setNestedScrollingEnabled(true);
        }


        prepareProductsList(productsListView, productsList, tempDate);
        setHeader();


        int calorieString = (int) wholeCalories;
        int calorieStringTo = (int) settingsManager.getCaloriesRequirement();


      // ProgressLayout progressLayout = (ProgressLayout) findViewById(R.id.progressLayout);
       // progressLayout.setMaxProgress(calorieStringTo);
      //  progressLayout.setCurrentProgress(calorieString);
      //  boolean isPlaying = progressLayout.isPlaying();


      //  arcProgress1 = (ArcProgress) findViewById(R.id.arc_progress);

     //   arcProgress1.setProgress((int) wholeCalories);


        CircularProgressIndicator circularProgressIndicator = (CircularProgressIndicator) findViewById(R.id.circular_progress);

        circularProgressIndicator.setProgress(calorieString, calorieStringTo);
        PatternProgressTextAdapter: circularProgressIndicator.setProgressTextAdapter(new PatternProgressTextAdapter("%.0f" + " kCal"));

        circularProgressIndicator.setOnProgressChangeListener(new CircularProgressIndicator.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(double progress, double maxProgress) {
                Log.d("PROGRESS", String.format("Current: %1$.0f, max: %2$.0f", progress, maxProgress));
            }
        });
    }

    @SuppressLint("NewApi")
    private void setHeader() {
        float factor = 1;

        DecimalFormat df = new DecimalFormat("##");
        String postfix;

        if (settingsManager.getUnits().equals(SettingsManager.US_UNITS)) {
            postfix = "lb";
            factor = 1 / gramsPerPound;
            wholeProteins /= gramsPerPound;
            wholeCarbohydrates /= gramsPerPound;
            wholeFats /= gramsPerPound;

        } else {
            postfix = "g";
        }

        String proteinString = df.format(wholeProteins) + "/" + df.format(settingsManager.getProteinRequirement() * factor) + postfix;// + "\n";
        String carbohydrateString = df.format(wholeCarbohydrates) + "/" + df.format(settingsManager.getCarbohydratesRequirement() * factor) + postfix;// + "\n";
        String fatString = df.format(wholeFats) + "/" + df.format(settingsManager.getFatRequirement() * factor) + postfix;// + "\n";
        String calorieString = df.format(wholeCalories);
        String remaincalorieString = "/" + df.format(settingsManager.getCaloriesRequirement());

        float remainingcalories = (settingsManager.getCaloriesRequirement() - wholeCalories);

        String remainkcal = df.format(remainingcalories);



        ProgressBar simpleProgressBar=(ProgressBar) findViewById(R.id.simpleProgressBar); // initiate the progress bar
        simpleProgressBar.setMax((int) settingsManager.getProteinRequirement());
        simpleProgressBar.setProgress((int) wholeProteins);

        ProgressBar simpleProgressBar1=(ProgressBar) findViewById(R.id.simpleProgressBar1); // initiate the progress bar
        simpleProgressBar1.setMax((int) settingsManager.getCarbohydratesRequirement());
        simpleProgressBar1.setProgress((int) wholeCarbohydrates);


        ProgressBar simpleProgressBar2=(ProgressBar) findViewById(R.id.simpleProgressBar2); // initiate the progress bar
        simpleProgressBar2.setMax((int) settingsManager.getFatRequirement());
        simpleProgressBar2.setProgress((int) wholeFats);

        caloriesEditText.setText(remainkcal);
//        remainCalories.setText(remainkcal + " kcal to go");


        proteinEditText.setText(proteinString);
        carbohydratesEditText.setText(carbohydrateString);
        fatEditText.setText(fatString);


        SimpleDateFormat month_date = new SimpleDateFormat("MMM", Locale.US);
        String month_name = month_date.format(tempDate.getTime());


        textView.setText(tempDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US) + ", " + month_name + " " + (tempDate.get(Calendar.DAY_OF_MONTH)) + ", "
                + tempDate.get(Calendar.YEAR));
    }

    private void prepareProductsList(ListView listView, ArrayList<RowModelSearch> list, Calendar date) {
        addElements(list, date);
        arrayAdapter.notifyDataSetChanged();
        setHeader();
    }

    private void addElements(ArrayList<RowModelSearch> list, Calendar date) {
        wholeProteins = wholeCarbohydrates = wholeFats = wholeCalories = 0;
        String productPointsFileName = date.get(Calendar.DAY_OF_MONTH) + "_" + (tempDate.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
        showeatenProducts = new ArrayList<ShowEatenProduct>();
        list.clear();

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
            float protein, carbohydrates, fats, calories;
            long time;
            int amount;
            String name;
            boolean isFood;
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
                //    isFood = Boolean.parseBoolean(array[6].trim());
                    name = array[6];
                    for (int i = 7; i < array.length; i++)
                    {
                        name += " " + array[i];
                    }



                    ShowEatenProduct product = new ShowEatenProduct(amount, protein, carbohydrates, fats, calories, time,  name);
                    showeatenProducts.add(product);

                    list.add(new RowModelSearch(product.getName()+"", product.getCalories() + "", product.getAmount() + "", product.getAmount() + "" , product.getAmount() + ""));
                    wholeProteins += product.getProtein();
                    wholeCarbohydrates += product.getCarbohydrates();
                    wholeFats += product.getFat();
                    wholeCalories += product.getCalories();







                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            wholeProteins = wholeCarbohydrates = wholeFats = wholeCalories = 0;
            e.printStackTrace();
        }
    }

    public RowModelSearch getModel(int position) {
        return (((RowAdapter) productsListView.getAdapter()).getItem(position));
    }



    private void showAlertDialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_question).setCancelable(false).setPositiveButton(R.string.positive_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct(index);


            }
        }).setNegativeButton(R.string.negative_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private synchronized void deleteProduct(final int index) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(showeatenProducts.get(index).getTime());
        productsList.remove(index);
        showeatenProducts.remove(index);
        arrayAdapter.notifyDataSetChanged();

        wholeProteins = wholeCarbohydrates = wholeFats = wholeCalories = 0;
        for (int i = 0; i < showeatenProducts.size(); i++) {
            ShowEatenProduct product = showeatenProducts.get(i);
            wholeProteins += product.getProtein();
            wholeCarbohydrates += product.getCarbohydrates();
            wholeFats += product.getFat();
            wholeCalories += product.getCalories();
        }

        try {
            String productPointsFileName = date.get(Calendar.DAY_OF_MONTH) + "_" + (tempDate.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
            FileOutputStream fos = openFileOutput(productPointsFileName, Context.MODE_PRIVATE);
            PrintWriter out = new PrintWriter(fos);

            ShowEatenProduct product;
            for (int i = 0; i < showeatenProducts.size(); i++) {
                product = showeatenProducts.get(i);
                out.print(product.getAmount() + " ");
                out.print(product.getProtein() + " ");
                out.print(product.getCarbohydrates() + " ");
                out.print(product.getFat() + " ");
                out.print(product.getCalories() + " ");
                out.print(product.getTime() + " ");
              //  out.print(product.isFood() + " ");
                out.println(product.getName());
            }

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        setHeader();
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(System.currentTimeMillis());

                mYear = date.get(Calendar.YEAR);
                mMonth = date.get(Calendar.MONTH);
                mDay = date.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);


        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.calendar) {
// Menu to show another date
            showDialog(DATE_DIALOG_ID);
            return true;
        }

        if (id == R.id.editdiet) {
// Menu to change diet settings
            Intent intent = new Intent(Dashboard.this, EditPlanActivity.class);
            startActivity(intent);
            return true;
        }


        if (id == R.id.editprofile) {
// Menu to edit user's profile
            Intent intent = new Intent(Dashboard.this, UserActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab12:

// FAB to add consumed food.

                Intent intent = new Intent(Dashboard.this, ManualProductSearchActivity.class);
                startActivity(intent);

                break;

            case R.id.fab22:

// FAB to add new product to db.
                Intent intent2 = new Intent(Dashboard.this, AddProductActivity.class);
                intent2.putExtra(PRODUCT_ID, -1);
                startActivity(intent2);

                break;

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


    }


    private class RowAdapter extends ArrayAdapter<RowModelSearch> {
        Activity context;

        RowAdapter(Activity context, ArrayList<RowModelSearch> list) {
            super(context, R.layout.item_row, list);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewWrapper wrapper;

            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.item_row, null);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (ViewWrapper) row.getTag();
            }

            RowModelSearch model = getModel(position);
            wrapper.getLabel().setText(model.getLabel());
            wrapper.getDetails().setText("• " + model.getDetails() + " cal • " + model.getDetails2() + " gr portion");

            return (row);
        }
    }
    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double time) {
            int hours = (int) (time / 3600);
            time %= 3600;
            int minutes = (int) (time / 60);
            int seconds = (int) (time % 60);
            StringBuilder sb = new StringBuilder();
            if (hours < 10) {
                sb.append(0);
            }
            sb.append(hours).append(":");
            if (minutes < 10) {
                sb.append(0);
            }
            sb.append(minutes).append(":");
            if (seconds < 10) {
                sb.append(0);
            }
            sb.append(seconds);
            return sb.toString();
        }
    };
}
