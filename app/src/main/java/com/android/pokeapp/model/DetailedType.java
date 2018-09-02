package com.android.pokeapp.model;

import io.realm.RealmObject;

public class DetailedType extends RealmObject {
    private int slot;

    private Type type;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
