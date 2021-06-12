package com.databaseproj.caltracker.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.databaseproj.caltracker.model.Product;

import java.util.ArrayList;

public class ProductsDatabaseManager {
    public static final String MAIN_KEY_ID = "_id";
    public static final String MAIN_KEY_NAME = "product_name";
    public static final String MAIN_KEY_IS_FOOD = "is_food";
    public static final String MAIN_KEY_CALORIES = "calories";
    public static final String MAIN_KEY_BARCODE = "barcode";
    public static final String MAIN_KEY_FAT = "fat";
    public static final String MAIN_KEY_PROTEIN = "protein";
    public static final String MAIN_KEY_CARBOHYDRATES = "carbohydrates";
    private static final String MAIN_DATABASE_NAME = "products.db";
    private static final String MAIN_DATABASE_TABLE = "products";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase database;
    private toDoDBOpenHelper databaseHelper;


    /* Constructor */
    public ProductsDatabaseManager(Context _context) {
        this.context = _context;
        databaseHelper = new toDoDBOpenHelper(context, MAIN_DATABASE_NAME, null, DATABASE_VERSION);


    }


    /* Close Database */
    public void close() {
        database.close();
    }

    /* Open Database to read and write */
    public void open() throws SQLiteException {
        try {
            database = databaseHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            database = databaseHelper.getReadableDatabase();
        }
    }

    /* Insert a new point */
    public long insertWorkout(Product product) {
        ContentValues newProductValues = new ContentValues();

        newProductValues.put(MAIN_KEY_NAME, product.getName());
        newProductValues.put(MAIN_KEY_IS_FOOD, product.isFood());
        newProductValues.put(MAIN_KEY_CALORIES, product.getCalories());
        newProductValues.put(MAIN_KEY_BARCODE, product.getBarcode());
        newProductValues.put(MAIN_KEY_FAT, product.getFat());
        newProductValues.put(MAIN_KEY_PROTEIN, product.getProtein());
        newProductValues.put(MAIN_KEY_CARBOHYDRATES, product.getCarbohydrates());

        return database.insert(MAIN_DATABASE_TABLE, null, newProductValues);
    }

    public void removeWorkoutByBarcode(final String barcode) {
        database.delete(MAIN_DATABASE_TABLE, MAIN_KEY_BARCODE + "=" + barcode, null);
    }

    public void removeWorkoutById(final int id) {
        database.delete(MAIN_DATABASE_TABLE, MAIN_KEY_ID + "=" + id, null);
    }

    /* Returns all workouts from Database */
    public Cursor getAllProductsCursor() {
        Cursor result = database.query(true, MAIN_DATABASE_TABLE, new String[]{MAIN_KEY_ID, MAIN_KEY_NAME, MAIN_KEY_IS_FOOD, MAIN_KEY_CALORIES, MAIN_KEY_BARCODE,
                MAIN_KEY_FAT, MAIN_KEY_PROTEIN, MAIN_KEY_CARBOHYDRATES}, null, null, null, null, null, null);

        if ((result.getCount() == 0) || !result.moveToFirst()) {
            throw new SQLException("No products found!");
        }

        return result;
    }

    public Product getProductById(int id) throws SQLException {
        Cursor cursor = database.query(true, MAIN_DATABASE_TABLE, new String[]{MAIN_KEY_ID, MAIN_KEY_NAME, MAIN_KEY_IS_FOOD, MAIN_KEY_CALORIES, MAIN_KEY_BARCODE,
                MAIN_KEY_FAT, MAIN_KEY_PROTEIN, MAIN_KEY_CARBOHYDRATES}, MAIN_KEY_ID + "=" + id, null, null, null, null, null);

        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No product found!");
        }

        Product product;
        String name = cursor.getString(cursor.getColumnIndex(MAIN_KEY_NAME));
        int tempBool = cursor.getInt(cursor.getColumnIndex(MAIN_KEY_IS_FOOD));
        boolean foodOrLiquid;
        if (tempBool == 0) {
            foodOrLiquid = false;
        } else {
            foodOrLiquid = true;
        }
        float calories = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CALORIES));
        String _barcode = cursor.getString(cursor.getColumnIndex(MAIN_KEY_BARCODE));
        float fat = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_FAT));
        float protein = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_PROTEIN));
        float carbohydrates = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CARBOHYDRATES));

        product = new Product(name, foodOrLiquid, calories, _barcode, fat, protein, carbohydrates);
        product.setId(cursor.getInt(cursor.getColumnIndex(MAIN_KEY_ID)));
        return product;
    }

    public Product getProductByBarcode(String barcode) throws SQLException {
        Cursor cursor = database.query(true, MAIN_DATABASE_TABLE, new String[]{MAIN_KEY_ID, MAIN_KEY_NAME, MAIN_KEY_IS_FOOD, MAIN_KEY_CALORIES, MAIN_KEY_BARCODE,
                MAIN_KEY_FAT, MAIN_KEY_PROTEIN, MAIN_KEY_CARBOHYDRATES}, MAIN_KEY_BARCODE + "=" + barcode, null, null, null, null, null);

        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No product found!");
        }

        Product product;
        String name = cursor.getString(cursor.getColumnIndex(MAIN_KEY_NAME));
        int tempBool = cursor.getInt(cursor.getColumnIndex(MAIN_KEY_IS_FOOD));
        boolean foodOrLiquid;
        if (tempBool == 0) {
            foodOrLiquid = false;
        } else {
            foodOrLiquid = true;
        }
        float calories = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CALORIES));
        String _barcode = cursor.getString(cursor.getColumnIndex(MAIN_KEY_BARCODE));
        float fat = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_FAT));
        float protein = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_PROTEIN));
        float carbohydrates = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CARBOHYDRATES));

        product = new Product(name, foodOrLiquid, calories, _barcode, fat, protein, carbohydrates);
        product.setId(cursor.getInt(cursor.getColumnIndex(MAIN_KEY_ID)));
        return product;
    }

    public ArrayList<Product> getProductsByName(String productName) throws SQLException {
        Cursor cursor = database.query(true, MAIN_DATABASE_TABLE, new String[]{MAIN_KEY_ID, MAIN_KEY_NAME, MAIN_KEY_IS_FOOD, MAIN_KEY_CALORIES, MAIN_KEY_BARCODE,
                MAIN_KEY_FAT, MAIN_KEY_PROTEIN, MAIN_KEY_CARBOHYDRATES}, MAIN_KEY_NAME + " LIKE '%" + productName + "%'", null, null, null, null, null);

        ArrayList<Product> productsList = new ArrayList<Product>();

        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No product found!");
        } else {
            cursor.moveToFirst();
            Product product;
            do {
                String name = cursor.getString(cursor.getColumnIndex(MAIN_KEY_NAME));
                int tempBool = cursor.getInt(cursor.getColumnIndex(MAIN_KEY_IS_FOOD));
                boolean foodOrLiquid;
                if (tempBool == 0) {
                    foodOrLiquid = false;
                } else {
                    foodOrLiquid = true;
                }
                float calories = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CALORIES));
                String _barcode = cursor.getString(cursor.getColumnIndex(MAIN_KEY_BARCODE));
                float fat = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_FAT));
                float protein = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_PROTEIN));
                float carbohydrates = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CARBOHYDRATES));

                product = new Product(name, foodOrLiquid, calories, _barcode, fat, protein, carbohydrates);
                product.setId(cursor.getInt(cursor.getColumnIndex(MAIN_KEY_ID)));
                productsList.add(product);

            } while (cursor.moveToNext());
        }
        return productsList;
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> productsList = new ArrayList<Product>();
        Cursor cursor = database.query(true, MAIN_DATABASE_TABLE, new String[]{MAIN_KEY_ID, MAIN_KEY_NAME, MAIN_KEY_IS_FOOD, MAIN_KEY_CALORIES, MAIN_KEY_BARCODE,
                MAIN_KEY_FAT, MAIN_KEY_PROTEIN, MAIN_KEY_CARBOHYDRATES}, null, null, null, null, null, null);

        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No product found!");
        } else {
            cursor.moveToFirst();
            Product product;
            do {
                String name = cursor.getString(cursor.getColumnIndex(MAIN_KEY_NAME));
                int tempBool = cursor.getInt(cursor.getColumnIndex(MAIN_KEY_IS_FOOD));
                boolean foodOrLiquid;
                if (tempBool == 0) {
                    foodOrLiquid = false;
                } else {
                    foodOrLiquid = true;
                }
                float calories = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CALORIES));
                String _barcode = cursor.getString(cursor.getColumnIndex(MAIN_KEY_BARCODE));
                float fat = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_FAT));
                float protein = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_PROTEIN));
                float carbohydrates = cursor.getFloat(cursor.getColumnIndex(MAIN_KEY_CARBOHYDRATES));

                product = new Product(name, foodOrLiquid, calories, _barcode, fat, protein, carbohydrates);
                product.setId(cursor.getInt(cursor.getColumnIndex(MAIN_KEY_ID)));
                productsList.add(product);

            } while (cursor.moveToNext());
        }
        return productsList;
    }

    // ******************************************************************************************************************
    private static class toDoDBOpenHelper extends SQLiteOpenHelper {

        // SQL Statement to create a new database.
        private static final String DATABASE_CREATE = "create table if not exists " + MAIN_DATABASE_TABLE + " (" + MAIN_KEY_ID + " integer primary key autoincrement, " + MAIN_KEY_NAME
                + " text, " + MAIN_KEY_IS_FOOD + " integer, " + MAIN_KEY_CALORIES + " real, " + MAIN_KEY_BARCODE + " text, " + MAIN_KEY_FAT + " real, "
                + MAIN_KEY_PROTEIN + " real, " + MAIN_KEY_CARBOHYDRATES + " real);";

        public toDoDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion + " to " + _newVersion + ", which will destroy all old data");

            // Drop the old table.
            _db.execSQL("DROP TABLE IF EXISTS " + MAIN_DATABASE_TABLE);
            // Create a new one.
            onCreate(_db);
        }


    }
}
