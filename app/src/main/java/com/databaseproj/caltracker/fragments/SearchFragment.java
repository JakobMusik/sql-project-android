package com.databaseproj.caltracker.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.view.FeatureActivity1;
import com.databaseproj.caltracker.view.FeatureActivity2;
import com.databaseproj.caltracker.view.FeatureActivity3;
import com.databaseproj.caltracker.view.FeatureActivity4;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton searchBtn1;
    private ImageButton searchBtn2;
    private ImageButton searchBtn3;
    private ImageButton searchBtn4;
    private TextView searchTxt_test;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.searchfragment, container, false);

        searchBtn1 = view.findViewById(R.id.searchBtn1);
        searchBtn2 = view.findViewById(R.id.searchBtn2);
        searchBtn3 = view.findViewById(R.id.searchBtn3);
        searchBtn4 = view.findViewById(R.id.searchBtn4);

        searchTxt_test = view.findViewById(R.id.featureText1);

        searchBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeatureActivity1.class);
                startActivityForResult(intent, 1);
            }
        });

        searchBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeatureActivity2.class);
                startActivityForResult(intent, 2);
            }
        });

        searchBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeatureActivity3.class);
                startActivityForResult(intent, 3);
            }
        });

        searchBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeatureActivity4.class);
                startActivityForResult(intent, 4);
            }
        });


        return view;
    }
}