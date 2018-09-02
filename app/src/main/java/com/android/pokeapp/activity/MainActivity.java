package com.android.pokeapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.pokeapp.R;
import com.android.pokeapp.adapter.PokemonAdapter;
import com.android.pokeapp.database.PokemonDAO;
import com.android.pokeapp.model.Pokemon;
import com.android.pokeapp.model.ResultList;
import com.android.pokeapp.network.ApiResult;
import com.android.pokeapp.network.RetrofitSession;
import com.android.pokeapp.utils.RecyclerTouchListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rémi OLLIVIER on 29/08/2018.
 */
public class MainActivity extends AppCompatActivity {

    private static final String EXTRAPOKEMONURL = "POKEMONURL";

    private int offset = 0;
    private PokemonAdapter adapter;
    private RetrofitSession retrofitSession;
    private PokemonDAO pokemonDAO = new PokemonDAO();

    @BindView(R.id.pb_list)
    public ProgressBar pbList;

    @BindView(R.id.rv_pokemons)
    public RecyclerView rvPokemons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.retrofitSession = new RetrofitSession();
        this.retrofitSession.initRetrofitClient(getApplicationContext());

        this.adapter = new PokemonAdapter(MainActivity.this);

        //init recyclerview
        this.initRecyclerView();

        refreshList(this.offset);
    }

    private void initRecyclerView() {
        this.rvPokemons.setLayoutManager(new LinearLayoutManager(this));
        this.rvPokemons.setAdapter(this.adapter);
        this.rvPokemons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && pbList.getVisibility() != View.VISIBLE) {
                    refreshList(offset);
                }
            }
        });
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        if (dividerDrawable != null) {
            itemDecoration.setDrawable(dividerDrawable);
            this.rvPokemons.addItemDecoration(itemDecoration);
        }
        this.rvPokemons.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), this.rvPokemons, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(EXTRAPOKEMONURL, adapter.getPokemonList().get(position).getUrl());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //do nothing
            }
        }));
    }

    private void refreshList(final int requestOffset) {
        this.pbList.setVisibility(View.VISIBLE);
        this.retrofitSession.getPokemonService().getPokemonsList(requestOffset, new ApiResult<ResultList>() {
            @Override
            public void success(ResultList res) {
                adapter.addToList(res.getResults());
                offset += 20;
                pbList.setVisibility(View.GONE);
                pokemonDAO.savePokemons(res.getResults());
            }

            @Override
            public void error(int code, String message) {
                if (code == -17) {
                    List<Pokemon> pokemons = pokemonDAO.getPokemonsList(offset);
                    if (pokemons != null && !pokemons.isEmpty()) {
                        adapter.addToList(pokemons);
                        offset += 20;
                    } else
                        Toast.makeText(MainActivity.this, R.string.impossible_to_load_offline, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivity.this, message + " " + code, Toast.LENGTH_SHORT).show();
                pbList.setVisibility(View.GONE);
            }
        });
    }
}