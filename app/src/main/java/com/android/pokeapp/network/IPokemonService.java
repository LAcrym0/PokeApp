package com.android.pokeapp.network;

import com.android.pokeapp.model.DetailedPokemon;
import com.android.pokeapp.model.Pokemon;
import com.android.pokeapp.model.ResultList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */
interface IPokemonService {

    @GET("api/v2/pokemon/{id}")
    @Headers("Content-Type: application/json")
    Call<DetailedPokemon> getPokemon(@Path("id") String id);

    @GET("api/v2/pokemon/?limit=20")
    @Headers("Content-Type: application/json")
    Call<ResultList> getPokemonsList(@Query("offset") int offset);
}
