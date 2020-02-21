package com.example.drugtest.main;

import androidx.annotation.StringRes;

import com.example.drugtest.model.Feature;

import java.util.List;

public interface MainContract {
    interface  View{
        void onGetList(List<Feature> mlist);
        void showProgressDialog(String text);

        void dismissProgressDialog();
    }
}
