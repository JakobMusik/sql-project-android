package com.databaseproj.caltracker.view;

import android.view.View;
import android.widget.TextView;

import com.databaseproj.caltracker.R;


public class ViewWrapper {
    View base;
    TextView details = null;
    TextView details1 = null;
    TextView details2 = null;
    TextView details3 = null;
    TextView label = null;

    public ViewWrapper(View base) {
        this.base = base;
    }

    public TextView getDetails() {
        if (details == null) {
            details = (TextView) base.findViewById(R.id.row_text_info);
        }
        return (details);
    }

    public TextView getDetails1() {
        if (details1 == null) {
            details1 = (TextView) base.findViewById(R.id.row_text_info1);
        }
        return (details1);
    }

    public TextView getDetails2() {
        if (details2 == null) {
            details2 = (TextView) base.findViewById(R.id.row_text_info2);
        }
        return (details2);
    }

    public TextView getDetails3() {
        if (details3 == null) {
            details3 = (TextView) base.findViewById(R.id.row_text_info3);
        }
        return (details3);
    }


    public TextView getLabel() {
        if (label == null) {
            label = (TextView) base.findViewById(R.id.row_text_label);
        }
        return (label);
    }
}
