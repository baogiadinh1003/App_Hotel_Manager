package com.example.lab2.Retrofit;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("api/users/add")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("name") String name
                                    );

    @POST("api/login")
    @FormUrlEncoded
    Observable<String> loginUsers(@Field("email") String email,
                                    @Field("password") String password
    );
}
