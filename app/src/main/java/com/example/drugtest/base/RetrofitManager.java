package com.example.drugtest.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
        private static RetrofitManager mInstance=new RetrofitManager();
        private MapService mapService;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        private RetrofitManager(){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/kiang/pharmacies/master/json/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mapService=retrofit.create(MapService.class);
        }

        public  static  RetrofitManager getInstance(){
            return mInstance;
        }
        public MapService getMapService(){
            return mapService;
        }

    }
