package com.databaseproj.caltracker.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.model.Product;
import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchAdapter;
import com.lapism.searchview.widget.SearchItem;
import com.lapism.searchview.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ManualProductSearchActivity extends AppCompatActivity  {

    public static final String PRODUCT_ID = "product_id";
    List<SearchItem> suggestionsList;
    private FloatingActionButton fab;
    private ArrayAdapter<RowModel> arrayAdapter;
  //  private ArrayList<RowModel> productsList;
    private ProductsDatabaseManager manager;

    private ArrayAdapter<RowModelSearch> arrayAdapterSearch;
    private ArrayList<RowModelSearch> productsListSearch;
    private ListView productsListView;
    private ArrayList<Product> productsArrayList;
    private SearchView mSearchView;
    private SearchAdapter mSearchAdapter;
    private String keyword;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.manual_search);

        mSearchView = (SearchView) findViewById(R.id.searchView);

        mSearchAdapter = new SearchAdapter(this);

        mSearchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(CharSequence query) {


                keyword = query.toString();
                search(keyword);

                return false;
            }

            @Override
            public void onQueryTextChange(CharSequence newText) {

                keyword = newText.toString();
                search(keyword);

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ManualProductSearchActivity.this, AddProductActivity.class);
                intent.putExtra(PRODUCT_ID, -1);
                startActivity(intent);

            }
        });

        productsListView = (ListView) findViewById(R.id.foundProductsListView);


        productsListSearch = new ArrayList<RowModelSearch>();
        arrayAdapterSearch = new RowAdapterSearch(this, productsListSearch);

        productsListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int index, long arg3) {
                Intent intent = new Intent(ManualProductSearchActivity.this, ShowProductActivity.class);
                intent.putExtra(PRODUCT_ID, productsArrayList.get(index).getId());
                startActivity(intent);
            }
        });

        productsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                showAlertDialog(arg2);
                return true;
            }
        });

        productsListView.setAdapter(arrayAdapterSearch);
        manager = new ProductsDatabaseManager(this);
        manager.open();


        search("");


    }

    private void search(String typedInString) {
        productsListSearch.clear();
        productsArrayList = new ArrayList<Product>();
        String[] stringToSearchArray;

        try {
            stringToSearchArray = typedInString.split("");
            productsArrayList.addAll(searchProductByName(typedInString));

            if (stringToSearchArray.length > 1) {
                for (int i = 0; i < stringToSearchArray.length; i++) {
                    try {
                        productsArrayList.addAll(searchProductByName(stringToSearchArray[i]));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ManualProductSearchActivity.this, R.string.no_product_found, duration);
            toast.show();
            arrayAdapterSearch.notifyDataSetChanged();
        }

        for (int i = 0; i < productsArrayList.size(); i++) {
            Product product = productsArrayList.get(i);
            productsListSearch.add(new RowModelSearch(product.getName(), product.getCalories() + "", product.getProtein() + "", product.getCarbohydrates() + "", product.getFat() + "")); //per ile gram/ml
            arrayAdapterSearch.notifyDataSetChanged();
        }
    }

    private ArrayList<Product> searchProductByName(String productName) throws SQLException {
        ArrayList<Product> productsList = new ArrayList<Product>();
        productsList.addAll(manager.getProductsByName(productName));
        return productsList;
    }

    public void onDestroy() {
        super.onDestroy();
        manager.close();
    }

    private RowModelSearch getModelSearch(int position) {
        return (((RowAdapterSearch) productsListView.getAdapter()).getItem(position));
    }

    private RowModel getModel(int position) {
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

    private void deleteProduct(final int index) {
        productsListSearch.remove(index);
        manager.removeWorkoutById(productsArrayList.get(index).getId());
      //  productsListSearch.clear();
        arrayAdapterSearch.notifyDataSetChanged();
    }









    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



    private class RowAdapterSearch extends ArrayAdapter<RowModelSearch> {
        Activity context;

        RowAdapterSearch(Activity context, ArrayList<RowModelSearch> list) {
            super(context, R.layout.manual_search_row, list);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewWrapper wrapper;

            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.manual_search_row, null);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (ViewWrapper) row.getTag();
            }

            RowModelSearch model = getModelSearch(position);
            wrapper.getLabel().setText(model.getLabel());
            wrapper.getDetails().setText(model.getDetails() + " kCal");
            wrapper.getDetails1().setText(model.getDetails1() + " gr");
            wrapper.getDetails2().setText(model.getDetails2() + " gr");
            wrapper.getDetails3().setText(model.getDetails3() + " gr");


            return (row);
        }
    }

    private class RowAdapter extends ArrayAdapter<RowModel> {
        Activity context;

        RowAdapter(Activity context, ArrayList<RowModel> list) {
            super(context, R.layout.manual_search_row, list);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewWrapper wrapper;

            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.manual_search_row, null);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (ViewWrapper) row.getTag();
            }


            return (row);
        }
    }



    @CallSuper
    protected void getData(String text, int position) {


        Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
    }
}
