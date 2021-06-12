package com.databaseproj.caltracker.model;

public class EatenProduct {
    private String name;
    private boolean isFood;
    private float calories;
    private float fat;
    private float protein;
    private float carbohydrates;
    private int amount;
    private long time;

    public EatenProduct(int amount, float protein, float carbohydrates, float fat, float calories, long time, boolean isFood,  String name) {
        this.name = name;
        this.isFood = isFood;
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

    public boolean isFood()
    {
        return isFood;
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
