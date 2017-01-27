package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MoviesPage;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

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

    private Fab fab;
    private MaterialSheetFab<Fab> materialSheetFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        this.sharedPreferences = getSharedPreferences(Preferences.PREFS_FILE_NAME, MODE_PRIVATE);

        this.movies = new ArrayList<>();

        this.fab = (Fab) findViewById(R.id.fab);

        this.mProgressView = (LinearLayout) findViewById(R.id.progress_view);

        this.mMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);
        this.mMoviesRecyclerView.setHasFixedSize(true);

        int columns = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 4;
        }

        this.mLayoutManager = new GridLayoutManager(this, columns);
        this.mMoviesRecyclerView.setLayoutManager(mLayoutManager);

        this.mMoviesAdapter = new TMDBMoviesGridAdapter(this, movies, new View.OnClickListener() {
            @Override
            public void onClick(View posterView) {
                Movie movie = (Movie) posterView.getTag();

                Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.MOVIE_PARCELABLE_EXTRA, movie);

                View decor = getWindow().getDecorView();
                View statusBar = decor.findViewById(android.R.id.statusBarBackground);
                View navBar = decor.findViewById(android.R.id.navigationBarBackground);

                Pair<View, String> p1 = Pair.create(posterView, "poster");
                Pair<View, String> p2 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                Pair<View, String> p3 = Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                Pair<View, String> p4 = Pair.create(findViewById(R.id.fab), "fab");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MoviesGridActivity.this,
                        p1, p2, p3, p4);

                startActivity(intent, options.toBundle());
            }
        });
        this.mMoviesAdapter.setOnDataEventsListener(this);
        this.mMoviesRecyclerView.setAdapter(mMoviesAdapter);


        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.dim_overlay);
        int sheetColor = getColor(R.color.colorPrimaryDark);
        int fabColor = getColor(R.color.colorAccent);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        this.tmdbClient = new TMDBClient(this);

        switch (getCurrentFilter()) {
            case Preferences.MOVIES_FILTER_POPULAR:
                showMostPopular(null);
                break;
            case Preferences.MOVIES_FILTER_TOP_RATED:
                showTopRated(null);
                break;
        }
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

    public void showMostPopular(View view) {
        fab.setImageResource(R.drawable.ic_whatshot);
        if (materialSheetFab.isSheetVisible())
            materialSheetFab.hideSheet();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Preferences.KEY_MOVIES_FILTER_KEY, Preferences.MOVIES_FILTER_POPULAR);
        editor.apply();

        movies.clear();
        mMoviesAdapter.notifyDataSetChanged();

        loadMoreMovies();
    }

    public void showTopRated(View view) {
        fab.setImageResource(R.drawable.ic_star_filled);
        if (materialSheetFab.isSheetVisible())
            materialSheetFab.hideSheet();

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

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }
}
