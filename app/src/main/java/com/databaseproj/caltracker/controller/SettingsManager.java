package com.databaseproj.caltracker.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class SettingsManager {
    public static final String SCANNED_BARCODE_KEY = "scanned_barcode_key";
    public static final String PREFERENCES_CALORIES_REQUIREMENT = "calories_requirement";
    public static final String PREFERENCES_PROTEIN_REQUIREMENT = "protein_requirement";
    public static final String PREFERENCES_CARBOHYDRATES_REQUIREMENT = "carbohydrates_requirement";
    public static final String PREFERENCES_FAT_REQUIREMENT = "fat_requirement";
    public static final String PREFERENCES_USER_ID_KEY = "0";
    public static final String PREFERENCES_USER_NAME_KEY = "user_name_key";
    public static final String PREFERENCES_USER_EMAIL_KEY = "user_name_email";
    public static final String PREFERENCES_USER_AGE_KEY = "user_age_key";
    public static final String PREFERENCES_USER_EU_WEIGHT_KEY = "user_eu_weight_key";
    public static final String PREFERENCES_USER_US_WEIGHT_KEY = "user_us_weight_key";
    public static final String PREFERENCES_USER_US_HEIGHT_KEY = "user_us_height_key";
    public static final String PREFERENCES_USER_EU_HEIGHT_KEY = "user_eu_height_key";
    public static final String PREFERENCES_USER_SEX_KEY = "user_sex_key";
    public static final String PREFERENCES_USER_EXERCISE_KEY = "user_exercise_key";
    public static final String PREFERENCES_ACTIVITY_TYPE_KEY = "activity_type_key";
    public static final String PREFERENCES_UNITS_TYPE_KEY = "units_type_key";
    public static final String PREFERENCES_AUTOPAUSE_KEY = "autopause_key";
    public static final String PREFERENCES_GPS_REFRESH_RATE_KEY = "gps_refresh_rate_key";
    public static final String PREFERENCES_NOTIFICATION_TYPE_KEY = "notification_type_key";
    public static final String PREFERENCES_NOTIFICATION_EU_DISTANCE_KEY = "notification_distance_in_meters_key";
    public static final String PREFERENCES_NOTIFICATION_EU_DISTANCE_IN_RACE_KEY = "notification_distance_in_meters_in_race_key";
    public static final String PREFERENCES_NOTIFICATION_US_DISTANCE_KEY = "notification_distance_in_miles_key";
    public static final String PREFERENCES_NOTIFICATION_US_DISTANCE_IN_RACE_KEY = "notification_distance_in_miles_in_race_key";
    public static final String PREFERENCES_NOTIFICATION_PERIOD_KEY = "notification_period_key";
    public static final String PREFERENCES_NOTIFICATION_PERIOD_IN_RACE_KEY = "notification_period_in_race_key";
    public static final String PREFERENCES_NOTIFICATION_SHOW_SPEED_INSTEAD_OF_TEMPO_KEY = "notification_speed_or_tempo_key";
    public static final String PREFERENCES_NOTIFICATION_SHOW_BURNED_CALORIES_KEY = "notification_burned_calories_key";
    public static final String PREFERENCES_VOICE_NOTIFICATION_KEY = "notification_voice_notification_key";
    public static final String AGREEMENT_TAG = "OK";
    public static final String CANCEL_TAG = "CANCEL";

    public static final String UNITS_PROMPT = "Choose units type:";
    public static final String AUTOPAUSE_PROMPT = "Choose autopause interval:";
    public static final String GPS_REFRESH_RATE_PROMPT = "Choose GPS refresh rate:";
    public static final String EU_UNITS = "km / kg";
    public static final String US_UNITS = "mile / pound";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final Integer SEDENTARY = 1;
    public static final Integer LIGHT = 2;
    public static final Integer MODERATE = 3;
    public static final Integer HEAVY = 4;
    public static final Integer VERYHEAVY = 5;
    private static final double POUNDS_TO_KILOGRAM = 2.20462262;
    private static final float GRAMS_PER_POUND = new Float(453.59237);

    private static final String EU_WEIGHT_UNIT = "kg";
    private static final String US_WEIGHT_UNIT = "pounds";
    private static final String EU_LENGTH_UNIT = "km";
    private static final String US_LENGTH_UNIT = "mile";
    private static SettingsManager instance = null;
    private static ArrayList<String> mainTagsList;
    private static String ageValuesTable[];
    private static String EUWeightValuesTable[];
    private static String USWeightValuesTable[];
    private static String USHeightStringTable[];
    private static String EUHeightStringTable[];
    private static int EUHeightValuesTable[];
    private static int USHeightValuesTable[];
    private static String sexValuesTable[] = {MALE, FEMALE};
    private static String unitsValuesTable[] = {EU_UNITS, US_UNITS};

    static {
        mainTagsList = new ArrayList<String>();
        mainTagsList.add("User");
        mainTagsList.add("Activity");
        mainTagsList.add("Units");
        mainTagsList.add("Autopause");
        mainTagsList.add("GPS");
        mainTagsList.add("Notifications");

        ageValuesTable = new String[100];
        for (int i = 0; i < 100; i++) {
            ageValuesTable[i] = Integer.toString(i + 5);
        }

        EUWeightValuesTable = new String[151];
        for (int i = 0; i < 151; i++) {
            EUWeightValuesTable[i] = Integer.toString(i + 30);
        }

        USWeightValuesTable = new String[331];
        for (int i = 0; i < 331; i++) {
            USWeightValuesTable[i] = Integer.toString(i + 88);
        }


        USHeightStringTable = new String[50];
        for (int i = 4; i < 8; i++) {
            USHeightStringTable[(i - 4) * 12] = Integer.toString(i) + "ft";
            USHeightStringTable[(i - 4) * 12 + 1] = Integer.toString(i) + "ft " + Integer.toString(0) + ",5" + "in";

        }


        EUHeightStringTable = new String[130];
        for (int i = 0; i < 130; i++) {
            EUHeightStringTable[i] = Integer.toString(i + 100);
        }

        EUHeightValuesTable = new int[130];
        for (int i = 0; i < 130; i++) {
            EUHeightValuesTable[i] = i + 100;
        }

        USHeightValuesTable = new int[144];
        for (int i = 0; i < 144; i++) {
            Double value = new Double(2 * 0.3048);
            value += i * (0.3048 / 24);
            USHeightValuesTable[i] = value.intValue();
        }
    }

    private SharedPreferences sharedPreferences;

    private SettingsManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }//Singleton

    public static String[] getUnitsValuesTable() {
        return unitsValuesTable;
    }


    public int getMainTagsListSize() {
        return mainTagsList.size();
    }

    public String getMainTag(int index) {
        if (index < mainTagsList.size()) {
            return (mainTagsList.get(index));
        } else {
            return null;
        }
    }

    public int getID() {
        int id = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_ID_KEY, 0);
        return id;
    }

    public String getName() {
        String name = sharedPreferences.getString(SettingsManager.PREFERENCES_USER_NAME_KEY, "your name");
        return name;
    }

    public String getEmail() {
        String email = sharedPreferences.getString(SettingsManager.PREFERENCES_USER_EMAIL_KEY, "your email");
        return email;
    }

    public int getAge() {
        int age = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_AGE_KEY, 20);
        return age;
    }

    public int getWeight() {
        int weight;
        if (getUnits().equals(US_UNITS)) {
            weight = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_US_WEIGHT_KEY, 130);
        } else {
            weight = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_EU_WEIGHT_KEY, 60);
        }
        return weight;
    }

    public int getHeight() {
        int height;

        if (getUnits().equals(US_UNITS)) {
            height = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_US_HEIGHT_KEY, 69);

        } else {
            height = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_EU_HEIGHT_KEY, 175);

        }
        return height;
    }


    public String getGender() {
        String gender = sharedPreferences.getString(SettingsManager.PREFERENCES_USER_SEX_KEY, "male");
        return gender;
    }

    public Integer getExercise() {
        Integer exercise = sharedPreferences.getInt(SettingsManager.PREFERENCES_USER_EXERCISE_KEY, 0);
        return exercise;
    }

    public String getActivity() {
        String activity = sharedPreferences.getString(SettingsManager.PREFERENCES_ACTIVITY_TYPE_KEY, "running");
        return activity;
    }

    public String getUnits() {
        String units = sharedPreferences.getString(SettingsManager.PREFERENCES_UNITS_TYPE_KEY, "km / kg");
        return units;
    }

    public String getAutoPause() {
        String autoPause = sharedPreferences.getString(SettingsManager.PREFERENCES_AUTOPAUSE_KEY, "never");
        return autoPause;
    }

    public String getGpsRefreshRate() {
        String gpsRefreshRate = sharedPreferences.getString(SettingsManager.PREFERENCES_GPS_REFRESH_RATE_KEY, "1 second");
        return gpsRefreshRate;
    }


    public String[] getSexValuesTable() {
        return sexValuesTable;
    }

    public void setActivity(String activity, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_ACTIVITY_TYPE_KEY, activity);
        editor.apply();
    }

    public void setUnits(String units, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_UNITS_TYPE_KEY, units);
        editor.apply();
    }

    public void setAutoPause(String autoPause, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_AUTOPAUSE_KEY, autoPause);
        editor.apply();
    }

    public void setGPSRefreshRate(String gpsRefreshRate, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_GPS_REFRESH_RATE_KEY, gpsRefreshRate);
        editor.apply();
    }

    public void setID(int id, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putInt(SettingsManager.PREFERENCES_USER_ID_KEY, id);
        editor.apply();
    }

    public void setName(String name, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_USER_NAME_KEY, name);
        editor.apply();
    }

    public void setEmail(String email, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_USER_EMAIL_KEY, email);
        editor.apply();
    }

    public void setAge(Integer age, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putInt(SettingsManager.PREFERENCES_USER_AGE_KEY, age);
        editor.apply();
    }

    public void setWeight(Integer weight, Context context) {
        if (weight != getWeight()) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Editor editor = sharedPreferences.edit();

            if (getUnits().equals(EU_UNITS)) {
                editor.putInt(SettingsManager.PREFERENCES_USER_EU_WEIGHT_KEY, weight);
                editor.putInt(SettingsManager.PREFERENCES_USER_US_WEIGHT_KEY, (int) (POUNDS_TO_KILOGRAM * weight));
            } else {
                editor.putInt(SettingsManager.PREFERENCES_USER_EU_WEIGHT_KEY, (int) (weight / POUNDS_TO_KILOGRAM));
                editor.putInt(SettingsManager.PREFERENCES_USER_US_WEIGHT_KEY, weight);
            }

            editor.apply();
        }
    }

    public void setHeight(Integer height, Context context) {
        if (height != getHeight()) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Editor editor = sharedPreferences.edit();

            if (getUnits().equals(EU_UNITS)) {
                editor.putInt(SettingsManager.PREFERENCES_USER_EU_HEIGHT_KEY, height);

            } else {
                editor.putInt(SettingsManager.PREFERENCES_USER_US_HEIGHT_KEY, height);
            }

            editor.apply();
        }
    }

    public void setGender(String sex, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(SettingsManager.PREFERENCES_USER_SEX_KEY, sex);
        editor.apply();
    }


    public void setExercise(Integer exercise, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putInt(SettingsManager.PREFERENCES_USER_EXERCISE_KEY, exercise);
        editor.apply();
    }


    public String getWeightUnit() {
        if (getUnits().equals(EU_UNITS)) {
            return EU_WEIGHT_UNIT;
        } else {
            return US_WEIGHT_UNIT;
        }
    }


    public String getUserDetails() {
        String userDetails = getAge() + " years / " + getWeight() + " " + getWeightUnit() + " / " + getHeight() + " / " + getGender();
        return userDetails;
    }


    public void setCaloriesRequirement(float calories, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putFloat(SettingsManager.PREFERENCES_CALORIES_REQUIREMENT, calories);
        editor.apply();
    }

    public void setFatRequirement(float fat, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putFloat(SettingsManager.PREFERENCES_FAT_REQUIREMENT, fat);
        editor.apply();
    }

    public void setProteinRequirement(float protein, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putFloat(SettingsManager.PREFERENCES_PROTEIN_REQUIREMENT, protein);
        editor.apply();
    }

    public void setCarbohydratesRequirement(float carbohydrates, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putFloat(SettingsManager.PREFERENCES_CARBOHYDRATES_REQUIREMENT, carbohydrates);
        editor.apply();
    }

    public float getCaloriesRequirement() {
        return sharedPreferences.getFloat(SettingsManager.PREFERENCES_CALORIES_REQUIREMENT, 1600);
    }

    public float getFatRequirement() {
        return sharedPreferences.getFloat(SettingsManager.PREFERENCES_FAT_REQUIREMENT, 120);
    }

    public float getProteinRequirement() {
        return sharedPreferences.getFloat(SettingsManager.PREFERENCES_PROTEIN_REQUIREMENT, 120);
    }

    public float getCarbohydratesRequirement() {
        return sharedPreferences.getFloat(SettingsManager.PREFERENCES_CARBOHYDRATES_REQUIREMENT, 360);
    }


}
