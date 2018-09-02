package com.android.pokeapp.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */

public class Pokemon extends RealmObject {
    private String url;

    @Index
    @PrimaryKey
    private String name;

    public Pokemon() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return toUpperName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPokemonId() {
        String[] splitted = this.url.split("/");
        return splitted[splitted.length - 1];
    }

    private String toUpperName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
