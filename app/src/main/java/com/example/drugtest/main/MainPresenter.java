package com.example.drugtest.main;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.drugtest.base.BasePresenter;
import com.example.drugtest.base.MapService;
import com.example.drugtest.base.RetrofitManager;
import com.example.drugtest.model.Cozaui;
import com.example.drugtest.model.Feature;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainContract.View> {
    private MapService mapService=
            RetrofitManager.getInstance().getMapService();
    private List<Feature> cozauiList;
    private ProgressDialog mProgressDialog;
    public void getMapInformation(){
        getView().showProgressDialog("請等待載入");
        Observable.interval(1, 5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mapService.getImformation()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Cozaui>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Cozaui value) {

                                cozauiList=value.getFeatures();
                                Log.d("222", "onResponse: "+value.getFeatures().get(0).getProperties().getMask_child());
                                for (Feature feature :  value.getFeatures()){

                                    Log.d("222", "onResponse: "+feature.getProperties().getMask_adult());
                                }
                                Log.d("77", "onResponse: "+cozauiList);

                                getView().onGetList(cozauiList);
                                getView().dismissProgressDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("ffffff", "onError: ");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });


    }
}
