package com.example.lab2.Retrofit;

import java.net.Inet4Address;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstace(){
        try {
            if(instance == null){
                instance = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.3:3000/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        } catch (Exception e){
            if(instance == null){
                instance = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.3:3000/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }

        return instance;
    }
}
