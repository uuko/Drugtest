package com.example.drugtest.base;

import android.content.Intent;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;

public interface Presenter<V> {
    void onCreate();

    void onStart();//暂时没用到

    void onStop();

    void pause();//暂时没用到

    V  getView();
    void attachView(V view);

    void attachIncomingIntent(Intent intent);//暂时没用到

}
