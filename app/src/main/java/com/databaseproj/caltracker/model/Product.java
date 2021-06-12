package com.databaseproj.caltracker.model;

public class Product {
    private String name;
    private boolean isFood;
    private float calories;
    private String barcode;
    private float fat;
    private float protein;
    private float carbohydrates;
    private int id;

    public Product(String name, boolean isFood, float calories, String barcode, float fat, float protein, float carbohydrates) {
        this.name = name;
        this.isFood = isFood;
        this.calories = calories;
        this.barcode = barcode;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
    }

    public String getName() {
        return name;
    }

    public boolean isFood() {
        return isFood;
    }

    public float getCalories() {
        return calories;
    }

    public String getBarcode() {
        return barcode;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
