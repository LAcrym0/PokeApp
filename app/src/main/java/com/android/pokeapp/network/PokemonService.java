package com.android.pokeapp.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.pokeapp.R;
import com.android.pokeapp.model.DetailedPokemon;
import com.android.pokeapp.model.Pokemon;
import com.android.pokeapp.model.ResultList;
import com.android.pokeapp.utils.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */
public class PokemonService {
    private IPokemonService pokemonService;
    private Context context;

    //HTTP return codes
    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;
    private static final int HTTP_204 = 204;

    PokemonService(Retrofit retrofit, Context context) {
        this.pokemonService = retrofit.create(IPokemonService.class);
        this.context = context;
    }

    //---------------
    // POKEMON DISPLAY
    //---------------

    /**
     * Method used to get pokemon by id
     * @param id the id string
     * @param callback the callback that returns the pokemon for a success or the return code + message for a failure
     */
    public void getPokemon(String id, final ApiResult<DetailedPokemon> callback) {
        if (Network.isConnectionAvailable(this.context)) {
            Call<DetailedPokemon> call = this.pokemonService.getPokemon(id);
            call.enqueue(new Callback<DetailedPokemon>() {
                @Override
                public void onResponse(@NonNull Call<DetailedPokemon> call, @NonNull Response<DetailedPokemon> response) {
                    int statusCode = response.code();
                    Log.d("getPokemon","Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got pokemon");
                        DetailedPokemon value = response.body();
                        callback.success(value);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DetailedPokemon> call, @NonNull Throwable t) {
                    Log.e("PokemonService", "Error while calling the 'getPokemon' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    //---------------
    // POKEMONS LIST DISPLAY
    //---------------

    /**
     * Method used to get the list of pokemons
     * @param callback the callback that returns the pokemon's list for a success or the return code + message for a failure
     */
    public void getPokemonsList(int offset, final ApiResult<ResultList> callback) {
        if (Network.isConnectionAvailable(this.context)) {
            Call<ResultList> call = this.pokemonService.getPokemonsList(offset);
            call.enqueue(new Callback<ResultList>() {
                @Override
                public void onResponse(@NonNull Call<ResultList> call, @NonNull Response<ResultList> response) {
                    int statusCode = response.code();
                    Log.d("getPokemonsList","Return code : " + statusCode);
                    if (statusCode == HTTP_200) {
                        Log.d(getClass().getSimpleName(), "Return content : " + response.body());
                        Log.d(getClass().getSimpleName(), "Got pokemon");
                        ResultList resultValue = response.body();
                        callback.success(resultValue);
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultList> call, @NonNull Throwable t) {
                    Log.e("CommentService", "Error while calling the 'getPokemonsList' method !", t);
                    callback.error(-1, t.getLocalizedMessage());
                }
            });
        } else {
            onConnectionError(callback);
        }
    }

    private void onConnectionError(final ApiResult callback) {
        String error = this.context.getString(R.string.error_network_not_available);
        Log.d(getClass().getSimpleName(), error);
        callback.error(-17, error);
    }
}
