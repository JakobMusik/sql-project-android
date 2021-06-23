package com.databaseproj.caltracker.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.model.EatenProduct;
import com.databaseproj.caltracker.model.ShowEatenProduct;
import com.databaseproj.caltracker.view.AddProductActivity;
import com.databaseproj.caltracker.view.Coolbar;
import com.databaseproj.caltracker.view.ManualProductSearchActivity;
import com.databaseproj.caltracker.view.RowModelSearch;
import com.databaseproj.caltracker.view.ViewWrapper;

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

public class DashboardFragment extends Fragment implements View.OnClickListener {


    public static final String PRODUCT_ID = "product_id";
    static final int DATE_DIALOG_ID = 0;
    static final int SUGGEST_DIALOG = 1;
    private static final float gramsPerPound = new Float(453.59237);
    private Coolbar mCoolbar;
    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout bottomSheetDelete;
    LinearLayout bottomSheetAddRecall;

    FloatingActionMenu fab;
    SharedPreferences prefs = null;
    private ListView productsListView;
    private TextView textView;
    ImageButton btnLeft, btnRight;
    private ProductsDatabaseManager manager;
    private ArrayAdapter<RowModelSearch> arrayAdapter;
    private ArrayList<RowModelSearch> productsList;
    private SettingsManager settingsManager;
    private float totalProtein, totalCarbohydrates, totalFat, totalCalories;
    private ArrayList<EatenProduct> eatenProducts;
    private ArrayList<ShowEatenProduct> showeatenProducts;

    private Calendar tempDate;
    private TextView caloriesEditText, fatEditText, carbohydratesEditText, proteinEditText, remainCalories;
    private int mYear;
    private int mMonth;
    private int mDay;

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            tempDate.set(mYear, mMonth, mDay);
            prepareProductsList(productsListView, productsList, tempDate);
        }
    };

    View view;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dashboardfragment, container, false);

        manager = new ProductsDatabaseManager(getActivity());
        manager.open();

        textView = view.findViewById(R.id.AnalyzeTextView);
        productsListView = view.findViewById(R.id.AnalyzeList);
        settingsManager = SettingsManager.getInstance(getActivity());
        caloriesEditText = view.findViewById(R.id.calorietext);
        proteinEditText = view.findViewById(R.id.proteinValueEditText);
        carbohydratesEditText = view.findViewById(R.id.carbohydratesValueEditText);
        fatEditText = view.findViewById(R.id.fatValueEditText);
        remainCalories = view.findViewById(R.id.calorietext);

        btnLeft = view.findViewById(R.id.btnLeft);
        btnRight = view.findViewById(R.id.btnRight);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        tempDate = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        fab = view.findViewById(R.id.menu1);
        fab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
            }
        });
        fab.setClosedOnTouchOutside(true);

        com.github.clans.fab.FloatingActionButton fab1 = view.findViewById(R.id.fab12);
        com.github.clans.fab.FloatingActionButton fab2 = view.findViewById(R.id.fab22);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        onContentChanged();

        return view;

    }



    public void onContentChanged() {

        TextView empty = view.findViewById(R.id.empty);
        ListView list = view.findViewById(R.id.AnalyzeList);
        list.setEmptyView(empty);


    }



    @Override
    public void onResume() {
        super.onResume();

        totalProtein = totalCarbohydrates = totalFat = totalCalories = 0;
        productsList = new ArrayList<RowModelSearch>();

        tempDate = Calendar.getInstance();
        tempDate.setTimeInMillis(System.currentTimeMillis());

        arrayAdapter = new RowAdapter(getActivity(), productsList);

        productsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
                //show details dialog
            }
        });

        productsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //TODO
                //Temporarily disable delete function..
                //showAlertDialog(arg2);
                return true;
            }

        });

        productsListView.setAdapter(arrayAdapter);
        productsListView.setNestedScrollingEnabled(true);


        //TODO
        prepareProductsList(productsListView, productsList, tempDate);
        setHeader();


        int calorieString = (int) totalCalories;
        int calorieStringTo = (int) settingsManager.getCaloriesRequirement();


        // ProgressLayout progressLayout = (ProgressLayout) findViewById(R.id.progressLayout);
        // progressLayout.setMaxProgress(calorieStringTo);
        //  progressLayout.setCurrentProgress(calorieString);
        //  boolean isPlaying = progressLayout.isPlaying();


        //  arcProgress1 = (ArcProgress) findViewById(R.id.arc_progress);

        //   arcProgress1.setProgress((int) wholeCalories);


        CircularProgressIndicator circularProgressIndicator = view.findViewById(R.id.circular_progress);

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
            totalProtein /= gramsPerPound;
            totalCarbohydrates /= gramsPerPound;
            totalFat /= gramsPerPound;

        } else {
            postfix = "g";
        }

        String proteinString = df.format(totalProtein) + "/" + df.format(settingsManager.getProteinRequirement() * factor) + postfix;// + "\n";
        String carbohydrateString = df.format(totalCarbohydrates) + "/" + df.format(settingsManager.getCarbohydratesRequirement() * factor) + postfix;// + "\n";
        String fatString = df.format(totalFat) + "/" + df.format(settingsManager.getFatRequirement() * factor) + postfix;// + "\n";
        String calorieString = df.format(totalCalories);
        String remaincalorieString = "/" + df.format(settingsManager.getCaloriesRequirement());

        float remainingcalories = (settingsManager.getCaloriesRequirement() - totalCalories);

        String remainkcal = df.format(remainingcalories);


        ProgressBar simpleProgressBar= view.findViewById(R.id.simpleProgressBar); // initiate the progress bar
        simpleProgressBar.setMax((int) settingsManager.getProteinRequirement());
        simpleProgressBar.setProgress((int) totalProtein);

        ProgressBar simpleProgressBar1= view.findViewById(R.id.simpleProgressBar1); // initiate the progress bar
        simpleProgressBar1.setMax((int) settingsManager.getCarbohydratesRequirement());
        simpleProgressBar1.setProgress((int) totalCarbohydrates);


        ProgressBar simpleProgressBar2= view.findViewById(R.id.simpleProgressBar2); // initiate the progress bar
        simpleProgressBar2.setMax((int) settingsManager.getFatRequirement());
        simpleProgressBar2.setProgress((int) totalFat);

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
        totalProtein = totalCarbohydrates = totalFat = totalCalories = 0;
        String productPointsFileName = date.get(Calendar.DAY_OF_MONTH) + "_" + (tempDate.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
        showeatenProducts = new ArrayList<ShowEatenProduct>();
        list.clear();

        try {
            FileInputStream inputStream = getActivity().openFileInput(productPointsFileName);
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
            String[] array;

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
                    totalProtein += product.getProtein();
                    totalCarbohydrates += product.getCarbohydrates();
                    totalFat += product.getFat();
                    totalCalories += product.getCalories();







                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            totalProtein = totalCarbohydrates = totalFat = totalCalories = 0;
            e.printStackTrace();
        }
    }

    public RowModelSearch getModel(int position) {
        return (((RowAdapter) productsListView.getAdapter()).getItem(position));
    }



    private void showAlertDialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.ThemeOverlay_App_MaterialAlertDialog);
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





    protected Dialog onCreateDialog() {
        switch (DashboardFragment.DATE_DIALOG_ID) {
            case DATE_DIALOG_ID:

                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(System.currentTimeMillis());

                mYear = date.get(Calendar.YEAR);
                mMonth = date.get(Calendar.MONTH);
                mDay = date.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(requireActivity(), R.style.ThemeOverlay_App_MaterialAlertDialog, mDateSetListener, mYear, mMonth, mDay);


        }
        return null;
    }


    private class RowAdapter extends ArrayAdapter<RowModelSearch>
    {
        Activity context;

        RowAdapter(Activity context, ArrayList<RowModelSearch> list)
        {
            super(context, R.layout.item_row, list);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            ViewWrapper wrapper;

            if (row == null)
            {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.item_row, null);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
            } else
            {
                wrapper = (ViewWrapper) row.getTag();
            }

            RowModelSearch model = getModel(position);
            wrapper.getLabel().setText(model.getLabel());
            wrapper.getDetails().setText("• " + model.getDetails() + " cal • " + model.getDetails2() + " gr portion");


            return (row);
        }
    }



    private synchronized void deleteProduct(final int index) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(showeatenProducts.get(index).getTime());
        productsList.remove(index);
        showeatenProducts.remove(index);
        arrayAdapter.notifyDataSetChanged();

        totalProtein = totalCarbohydrates = totalFat = totalCalories = 0;
        for (int i = 0; i < showeatenProducts.size(); i++) {
            ShowEatenProduct product = showeatenProducts.get(i);
            totalProtein += product.getProtein();
            totalCarbohydrates += product.getCarbohydrates();
            totalFat += product.getFat();
            totalCalories += product.getCalories();
        }


        try {
            String productPointsFileName = date.get(Calendar.DAY_OF_MONTH) + "_" + (tempDate.get(Calendar.MONTH) + 1) + "_" + date.get(Calendar.YEAR);
            FileOutputStream fos = getActivity().openFileOutput(productPointsFileName, Context.MODE_PRIVATE);
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












    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab12:

// FAB to add consumed food.

                Intent intent = new Intent(getActivity(), ManualProductSearchActivity.class);
                startActivity(intent);

                break;

            case R.id.fab22:


                Intent intent2 = new Intent(getActivity(), AddProductActivity.class);
                intent2.putExtra(PRODUCT_ID, -1);
                startActivity(intent2);

                break;


            case  R.id.btnLeft:

                break;


            case R.id.btnRight:
                onCreateDialog().show();

                break;

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
