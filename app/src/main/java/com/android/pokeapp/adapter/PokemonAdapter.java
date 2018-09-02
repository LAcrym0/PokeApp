package com.android.pokeapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pokeapp.R;
import com.android.pokeapp.model.Pokemon;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */
public class PokemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Pokemon> pokemonList = new ArrayList<>();

    public PokemonAdapter(Context context) {
        this.context = context;
    }

    public void addToList(List<Pokemon> pokemons) {
        this.pokemonList.addAll(pokemons);
        notifyDataSetChanged();
    }

    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PokemonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pokemon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PokemonViewHolder pokemonViewHolder = (PokemonViewHolder) holder;
        pokemonViewHolder.tvName.setText(pokemonList.get(position).getName());
        Glide.with(this.context).load(context.getString(R.string.picture_url, pokemonList.get(position).getPokemonId())).into(pokemonViewHolder.ivPicture);
        this.animate(holder);
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation insertFromBottom = AnimationUtils.loadAnimation(context, R.anim.decelerate_interpolator);
        viewHolder.itemView.setAnimation(insertFromBottom);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    private static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivPicture;

        PokemonViewHolder(View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tv_name);
            this.ivPicture = itemView.findViewById(R.id.iv_picture);
        }
    }
}
