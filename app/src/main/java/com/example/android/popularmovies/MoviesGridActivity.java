package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MoviesPage;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesGridActivity extends AppCompatActivity implements TMDBMoviesGridAdapter.OnDataEventsListener, Callback<MoviesPage> {

    private static final String TAG = "MoviesGridActivity";

    private SharedPreferences sharedPreferences;

    private RecyclerView mMoviesRecyclerView;
    private TMDBMoviesGridAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinearLayout mProgressView;

    private TMDBClient tmdbClient;

    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        this.sharedPreferences = getSharedPreferences(Preferences.PREFS_FILE_NAME, MODE_PRIVATE);

        this.movies = new ArrayList<>();

        this.mProgressView = (LinearLayout) findViewById(R.id.progress_view);

        this.mMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);
        this.mMoviesRecyclerView.setHasFixedSize(true);

        int columns = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 4;
        }

        this.mLayoutManager = new GridLayoutManager(this, columns);
        this.mMoviesRecyclerView.setLayoutManager(mLayoutManager);

        this.mMoviesAdapter = new TMDBMoviesGridAdapter(movies);
        this.mMoviesAdapter.setOnDataEventsListener(this);
        this.mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        this.tmdbClient = new TMDBClient(this);
    }

    @Override
    public void onRequestMoreData() {
        loadMoreMovies();
    }

    @Override
    public void onRequestMoviePoster(Movie movie, TMDBMoviesGridAdapter.MovieImageViewHolder viewHolder) {
        tmdbClient.loadPoster(movie.getPosterPath(), viewHolder.mImageView);
    }

    private void loadMoreMovies() {
        mProgressView.setVisibility(View.VISIBLE);

        int nextPageToLoad = movies.size() / 20 + 1;

        switch (getCurrentFilter()) {
            case Preferences.MOVIES_FILTER_POPULAR:
                tmdbClient.loadPopularMoviesPage(nextPageToLoad, this, Locale.getDefault().getLanguage());
                break;
            case Preferences.MOVIES_FILTER_TOP_RATED:
                tmdbClient.loadTopRatedMoviesPage(nextPageToLoad, this, Locale.getDefault().getLanguage());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grid_filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sortByMostPopular();
                        break;
                    case 1:
                        sortByTopRated();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });

        return true;
    }

    public void sortByMostPopular() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Preferences.KEY_MOVIES_FILTER_KEY, Preferences.MOVIES_FILTER_POPULAR);
        editor.apply();

        movies.clear();
        mMoviesAdapter.notifyDataSetChanged();

        loadMoreMovies();
    }

    public void sortByTopRated() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Preferences.KEY_MOVIES_FILTER_KEY, Preferences.MOVIES_FILTER_TOP_RATED);
        editor.apply();

        movies.clear();
        mMoviesAdapter.notifyDataSetChanged();

        loadMoreMovies();
    }

    private String getCurrentFilter() {
        return sharedPreferences.getString(Preferences.KEY_MOVIES_FILTER_KEY, Preferences.MOVIES_FILTER_POPULAR);
    }

    @Override
    public void onResponse(Call<MoviesPage> call, Response<MoviesPage> response) {
        movies.addAll(response.body().getResults());
        mMoviesAdapter.notifyDataSetChanged();

        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Call<MoviesPage> call, Throwable t) {
        mProgressView.setVisibility(View.GONE);
    }
}
