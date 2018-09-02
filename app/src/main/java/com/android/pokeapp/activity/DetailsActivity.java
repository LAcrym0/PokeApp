package com.android.pokeapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pokeapp.R;
import com.android.pokeapp.database.PokemonDAO;
import com.android.pokeapp.model.DetailedPokemon;
import com.android.pokeapp.network.ApiResult;
import com.android.pokeapp.network.RetrofitSession;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRAPOKEMONURL = "POKEMONURL";
    private String pokemonUrl;
    private boolean isContentAvailable = false;
    private DetailedPokemon pokemon;
    private PokemonDAO pokemonDAO = new PokemonDAO();

    @BindView(R.id.iv_picture)
    public ImageView ivPokemon;

    @BindView(R.id.tv_height)
    public TextView tvHeight;

    @BindView(R.id.tv_weight)
    public TextView tvWeight;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.tv_types)
    public TextView tvTypes;

    @BindView(R.id.tv_moves)
    public TextView tvMoves;

    @BindView(R.id.pb_details)
    public ProgressBar pbDetails;

    @BindView(R.id.tb_back)
    public ImageView ivBack;

    @BindView(R.id.tb_title)
    public TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent = getIntent();
        if (intent != null) {
            this.pokemonUrl = intent.getStringExtra(EXTRAPOKEMONURL);
            RetrofitSession retrofitSession = new RetrofitSession();
            retrofitSession.initRetrofitClient(this);
            retrofitSession.getPokemonService().getPokemon(getPokemonId(this.pokemonUrl), new ApiResult<DetailedPokemon>() {
                @Override
                public void success(DetailedPokemon res) {
                    pokemon = res;
                    pokemon.setUrl(pokemonUrl);
                    pokemon.setId(getPokemonId(pokemonUrl));
                    updateUI();
                    pokemonDAO.saveDetailedPokemon(pokemon);
                }

                @Override
                public void error(int code, String message) {
                    if (code == -17) {
                        pokemon = pokemonDAO.getDetailedPokemon(getPokemonId(pokemonUrl));
                        if (pokemon != null)
                            updateUI();
                        else
                            Toast.makeText(DetailsActivity.this, R.string.impossible_to_load_offline, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(DetailsActivity.this, message + " " + code, Toast.LENGTH_SHORT).show();
                    pbDetails.setVisibility(View.GONE);
                }
            });
        }
    }

    private void updateUI() {
        Glide.with(DetailsActivity.this).load(getString(R.string.picture_url, getPokemonId(pokemon.getUrl()))).into(ivPokemon);
        tvTitle.setText(pokemon.getName());
        tvHeight.setText(getString(R.string.height, pokemon.getHeight() * 10));
        tvWeight.setText(getString(R.string.weight, (double) (pokemon.getWeight()) / 10));
        if (pokemon.getDetailedTypes().size() == 1)
            tvTypes.setText(getResources().getQuantityString(R.plurals.types, pokemon.getDetailedTypes().size(), pokemon.getDetailedTypes().size(), pokemon.getDetailedTypes().get(0).getType().getName()));
        else
            tvTypes.setText(getResources().getQuantityString(R.plurals.types, pokemon.getDetailedTypes().size(), pokemon.getDetailedTypes().size(), pokemon.getDetailedTypes().get(0).getType().getName(), pokemon.getDetailedTypes().get(1).getType().getName()));
        tvMoves.setText(getResources().getQuantityString(R.plurals.moves, pokemon.getMoves().size(), pokemon.getMoves().size()));
        pbDetails.setVisibility(View.GONE);
        isContentAvailable = true;
    }

    private String getPokemonId(String url) {
        String[] splitted = url.split("/");
        return splitted[splitted.length - 1];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        if (isContentAvailable) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share, pokemon.getName(), pokemon.getUrl()));

            startActivity(Intent.createChooser(share, getString(R.string.short_share, pokemon.getName())));
        }
    }
}
