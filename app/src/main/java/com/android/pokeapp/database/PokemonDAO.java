package com.android.pokeapp.database;

import android.util.Log;

import com.android.pokeapp.model.DetailedPokemon;
import com.android.pokeapp.model.Pokemon;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PokemonDAO {
    private Realm realm;

    public PokemonDAO() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        this.realm = Realm.getDefaultInstance();
    }

    public void savePokemons(final List<Pokemon> pokemonList) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(pokemonList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("POKEMONSAVE","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("POKEMONSAVE", error.toString());
            }
        });
    }

    public List<Pokemon> getPokemonsList(int offset) {
        List<Pokemon> pokemons = realm.where(Pokemon.class).findAll();
        if (pokemons.size() > offset)
            return pokemons.subList(offset, pokemons.size() > offset + 20 ? offset + 20 : pokemons.size() - 1);
        else
            return null;
    }

    public void saveDetailedPokemon(final DetailedPokemon detailedPokemon) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(detailedPokemon);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("DETAILEDPOKEMONSAVE","SUCCESS");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("DETAILEDPOKEMONSAVE", error.toString());
            }
        });
    }

    public DetailedPokemon getDetailedPokemon(String id) {
        return realm.where(DetailedPokemon.class).equalTo("id", id).findFirst();
    }
}
