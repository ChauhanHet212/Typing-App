package com.example.typingapp.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Retro {
    public String url = "https://random-word-api.herokuapp.com/";
    public static Retro instance;
    public RetroAPI retroAPI;

    public Retro(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retroAPI = retrofit.create(RetroAPI.class);
    }

    public static Retro getInstance(){
        if (instance == null){
            instance = new Retro();
        }

        return instance;
    }

    public interface RetroAPI {
        @GET("all")
        Call<String[]> getAllWords();
    }
}
