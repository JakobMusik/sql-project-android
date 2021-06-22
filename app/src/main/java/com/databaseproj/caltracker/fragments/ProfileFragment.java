package com.databaseproj.caltracker.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.databaseproj.caltracker.helper.UpdateRemoteDB;
import com.databaseproj.caltracker.view.EditPlanActivity;
import com.databaseproj.caltracker.view.LicenseActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.LabelledSpinner;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EU_UNITS = "km / kg";
    public static final String US_UNITS = "mile / pound";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    private LabelledSpinner exercise_spinner;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Button startButton;
    private EditText nameET, emailET, ageET, weightET, heightET;
    private SettingsManager settingsManager;
    private TextView termsTextView;
    private CheckBox imperial, metric, man, woman;
    private boolean CheckStatus_1 = true;
    private boolean CheckStatus_2 = true;

    private ProductsDatabaseManager manager;
    View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.profilefragment, container, false);

        settingsManager = SettingsManager.getInstance(getActivity());

        exercise_spinner = (LabelledSpinner) view.findViewById(R.id.exercise_spinner);

        startButton = (Button) view.findViewById(R.id.activity_hello_button_start);

        termsTextView = (TextView) view.findViewById(R.id.helloactivity_textview_terms);

        nameET = (EditText) view.findViewById(R.id.helloactivity_name);

        emailET = (EditText) view.findViewById(R.id.helloactivity_email);

        ageET = (EditText) view.findViewById(R.id.activity_hello_age);

        weightET = (EditText) view.findViewById(R.id.activity_hello_weight);

        heightET = (EditText) view.findViewById(R.id.activity_hello_height);

        exercise_spinner.setItemsArray(R.array.helloactivity_daily_exercise);


        manager = new ProductsDatabaseManager(getActivity());
        manager.open();




        imperial = (CheckBox) view.findViewById(R.id.imperial);
        imperial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    metric.setChecked(false);

                    TextInputLayout til = (TextInputLayout) view.findViewById(R.id.til);
                    TextInputLayout til2 = (TextInputLayout) view.findViewById(R.id.til2);
                    til.setHint("Height in inches");
                    til2.setHint("Weight in lbs");



                    CheckStatus_1 = true;
                }
                else {
                    CheckStatus_1 = false;
                }

            }
        });


        metric = (CheckBox) view.findViewById(R.id.metric);
        metric.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {


                    TextInputLayout til = (TextInputLayout) view.findViewById(R.id.til);
                    TextInputLayout til2 = (TextInputLayout) view.findViewById(R.id.til2);
                    til.setHint("Height in cm");
                    til2.setHint("Weight in kg");

                    imperial.setChecked(false);

                    CheckStatus_1 = true;
                }
                else {
                    CheckStatus_1 = false;
                }

            }
        });


        man = (CheckBox) view.findViewById(R.id.man);
        man.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    //    settingsManager.setUnits(table[choosenPosition], UserActivity.this);

                    woman.setChecked(false);

                    CheckStatus_2 = true;
                }
                else {
                    CheckStatus_2 = false;
                }

            }
        });


        woman = (CheckBox) view.findViewById(R.id.woman);
        woman.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    man.setChecked(false);

                    CheckStatus_2 = true;
                }
                else {
                    CheckStatus_2 = false;
                }

            }
        });


        final Drawable pinkArrow = ContextCompat.getDrawable(getContext(), R.drawable.ic_navigate_next_pink_24px);
        pinkArrow.setBounds(0, 0, 60, 60);
        startButton.setCompoundDrawables(null, null, pinkArrow, null);

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LicenseActivity.class);
                startActivity(intent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onStartClicked();
            }
        });


        informationFillIn();


        return view;
    }

    private void informationFillIn()
    {

        SharedPreferences prefs = getActivity().getSharedPreferences("useractivity", MODE_PRIVATE);
        if (prefs.getBoolean("filldata", false)) {

            //      prefs.edit().putBoolean("firstrun", false).apply();

            DecimalFormat df = new DecimalFormat("##");
            int height = (settingsManager.getHeight());
            int weight = (settingsManager.getWeight());
            String age = df.format(settingsManager.getAge());
            String name = settingsManager.getName();
            String email = settingsManager.getEmail();

            heightET.setText(String.valueOf(height));
            weightET.setText(String.valueOf(weight));
            ageET.setText(age);
            nameET.setText(name);
            emailET.setText(email);
            exercise_spinner.setSelection(settingsManager.getExercise());

        } else {

            prefs.edit().putBoolean("filldata", true).apply();

            heightET.setText("");
            weightET.setText("");
            ageET.setText("");
            nameET.setText("");
            emailET.setText("");
            exercise_spinner.setSelection(0);
            //	backupDatabase();
            //  copyDataBase();

        }
        //Checkboxes fill in
        settingsManager = SettingsManager.getInstance(getActivity());
        if (settingsManager.getUnits().equals(SettingsManager.EU_UNITS)) {
            //  USunits = false;

            imperial.setChecked(false);
            metric.setChecked(true);
            TextInputLayout til = (TextInputLayout) view.findViewById(R.id.til);
            TextInputLayout til2 = (TextInputLayout) view.findViewById(R.id.til2);
            til.setHint("Height in cm");
            til2.setHint("Weight in kg");

        } else {
            //  USunits = true;

            imperial.setChecked(true);
            metric.setChecked(false);
            TextInputLayout til = (TextInputLayout) view.findViewById(R.id.til);
            TextInputLayout til2 = (TextInputLayout) view.findViewById(R.id.til2);
            til.setHint("Height in Inches");
            til2.setHint("Weight in lbs");

        }

        boolean isMan = true;
        if (settingsManager.getSex().equals(settingsManager.getSexValuesTable()[1])) {
            isMan = false;
        }

        if (isMan) {

            // Male
            man.setChecked(true);
            woman.setChecked(false);

        } else {

            // female
            man.setChecked(false);
            woman.setChecked(true);
        }
        //End of Checkboxes fill in

    }

    public void onStartClicked() {

        if (nameET.getText() == null || emailET.getText() == null || ageET.getText() == null
                || heightET.getText() == null || weightET.getText() == null
                || exercise_spinner.getSpinner().getSelectedItemPosition() == 0
                || !CheckStatus_1 || !CheckStatus_2) {
            Toast.makeText(getContext(), "Don't leave blank plz.", Toast.LENGTH_SHORT).show();
            return;
        }

        settingsManager.setSex(man.isChecked() ? MALE : FEMALE, getActivity());
        settingsManager.setUnits(imperial.isChecked() ? US_UNITS : EU_UNITS, getActivity());
        settingsManager.setName(nameET.getText().toString(), getActivity());
        settingsManager.setEmail(emailET.getText().toString(), getActivity());
        settingsManager.setAge(Integer.parseInt(ageET.getText().toString()), getActivity());
        settingsManager.setHeight(Integer.parseInt(heightET.getText().toString()), getActivity());
        settingsManager.setWeight(Integer.parseInt(weightET.getText().toString()), getActivity());
        settingsManager.setExercise(exercise_spinner.getSpinner().getSelectedItemPosition(), getActivity());


        UpdateRemoteDB.update(getContext());
        Toast.makeText(getContext(), "Saving to server database...", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getActivity(), EditPlanActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1)
        {
            informationFillIn();
        }
    }


}
