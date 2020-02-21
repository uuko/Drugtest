package com.example.drugtest.base;

import android.content.Intent;


public class BasePresenter<V> implements  Presenter<V>{
    public V mView;

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public void attachView(V view) {
        mView=view;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }
}