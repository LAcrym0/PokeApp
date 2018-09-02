package com.android.pokeapp.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class DetailedPokemon extends RealmObject {
    private int weight;

    private int height;

    private RealmList<Move> moves;

    private RealmList<DetailedType> types;

    private String url;

    @Index
    @PrimaryKey
    private String id;

    private String name;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public RealmList<Move> getMoves() {
        return moves;
    }

    public void setMoves(RealmList<Move> moves) {
        this.moves = moves;
    }

    public RealmList<DetailedType> getDetailedTypes() {
        return types;
    }

    public void setDetailedTypes(RealmList<DetailedType> types) {
        this.types = types;
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

    private String toUpperName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
