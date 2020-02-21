package com.example.drugtest.base;

import com.example.drugtest.model.Cozaui;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {
    @GET("points.json")
    Observable<Cozaui> getImformation();
}
