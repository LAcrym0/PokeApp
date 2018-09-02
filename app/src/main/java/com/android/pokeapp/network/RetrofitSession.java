package com.android.pokeapp.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */
public class RetrofitSession {
    //API BASE URL
    private static final String BASE_URL = "http://pokeapi.co/";
    private static Gson gson;
    private Retrofit retrofit;
    //init once with private constructor
    private static final RetrofitSession INSTANCE = new RetrofitSession();
    private PokemonService pokemonService;

    private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setLenient().create();
        }
        return gson;
    }

    //method to call to get the instance of RetrofitSession
    public static RetrofitSession getInstance() {
        return INSTANCE;
    }

    //retrofit init
    public void initRetrofitClient(Context context) {
        //clean old retrofit client if exists
        if (retrofit != null) {
            retrofit = null;
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)//set URL
                .addConverterFactory(GsonConverterFactory.create(getGson()))//add json util
                .client(new OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build());//add logs

        retrofit = retrofitBuilder.build();
        pokemonService = new PokemonService(retrofit, context);
    }

    public PokemonService getPokemonService() {
        return pokemonService;
    }
}