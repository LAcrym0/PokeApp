package com.android.pokeapp.application;

import android.app.Application;

import com.android.pokeapp.network.RetrofitSession;

import io.realm.Realm;

/**
 * Created by Rémi OLLIVIER on 30/08/2018.
 */
public class PokemonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //RealmManager.initRealmManager();
        new RetrofitSession().initRetrofitClient(getApplicationContext());
    }


}
