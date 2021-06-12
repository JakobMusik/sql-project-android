package com.databaseproj.caltracker.model;

public class ShowEatenProduct {
    private String name;

    private float calories;
    private float fat;
    private float protein;
    private float carbohydrates;
    private int amount;
    private long time;

    public ShowEatenProduct(int amount, float protein, float carbohydrates, float fat, float calories, long time,   String name) {
        this.name = name;

        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.amount = amount;
        this.time = time;
    }

    public String getName() {
        return name;
    }



    public float getCalories() {
        return calories;
    }

    public float getFat() {
        return fat;
    }

    public float getProtein() {
        return protein;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public int getAmount() {
        return amount;
    }

    public long getTime() {
        return time;
    }
}
