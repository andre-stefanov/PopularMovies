package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.DefaultAnimationListener;
import com.example.android.popularmovies.utils.EndlessRecyclerViewScrollListener;
import com.github.clans.fab.FloatingActionMenu;

import static com.example.android.popularmovies.MoviesGridAdapter.VIEW_TYPE_PROGRESS;
import static com.example.android.popularmovies.Preferences.KEY_MOVIES_FILTER_KEY;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_POPULAR;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_TOP_RATED;

public class MoviesGridActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MoviesGridActivity";

    private SharedPreferences sharedPreferences;

    private FloatingActionMenu fabMenu;

    private MoviesGridAdapter moviesAdapter;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        TMDBClient tmdbClient = new TMDBClient(this);

        this.sharedPreferences = getSharedPreferences(Preferences.PREFS_FILE_NAME, MODE_PRIVATE);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        RecyclerView moviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);
        moviesRecyclerView.setHasFixedSize(true);
        moviesRecyclerView.getItemAnimator().setChangeDuration(0);

        this.moviesAdapter = new MoviesGridAdapter(this, tmdbClient, new View.OnClickListener() {
            @Override
            public void onClick(View posterView) {
                Movie movie = (Movie) posterView.getTag();

                final Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.MOVIE_PARCELABLE_EXTRA, movie);

                View decor = getWindow().getDecorView();
                View statusBar = decor.findViewById(android.R.id.statusBarBackground);
                View navBar = decor.findViewById(android.R.id.navigationBarBackground);

                Pair<View, String> p1 = Pair.create(posterView, "poster");
                Pair<View, String> p2 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                Pair<View, String> p3 = Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);

                @SuppressWarnings("unchecked")
                final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MoviesGridActivity.this,
                        p1, p2, p3);

                Animation animation = AnimationUtils.loadAnimation(MoviesGridActivity.this, com.github.clans.fab.R.anim.fab_scale_down);
                animation.setAnimationListener(new DefaultAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        startActivity(intent, options.toBundle());
                    }
                });
                fabMenu.setMenuButtonHideAnimation(animation);
                fabMenu.hideMenuButton(true);
            }
        });
        moviesRecyclerView.setAdapter(moviesAdapter);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (moviesAdapter.getItemViewType(position)) {
                    case VIEW_TYPE_PROGRESS:
                        return getResources().getInteger(R.integer.grid_columns);
                    default:
                        return 1;
                }
            }
        });
        moviesRecyclerView.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                Log.d(TAG, "onLoadMore() called with: page = [" + page + "], totalItemsCount = [" + totalItemsCount + "]");
                moviesAdapter.loadMoreMovies(page);
            }
        };
        moviesRecyclerView.addOnScrollListener(scrollListener);

        this.fabMenu = (FloatingActionMenu) findViewById(R.id.fab);
        this.fabMenu.setClosedOnTouchOutside(true);
        this.fabMenu.setIconAnimated(false);

        switch (sharedPreferences.getString(KEY_MOVIES_FILTER_KEY, Preferences.MOVIES_FILTER_POPULAR)) {
            case MOVIES_FILTER_POPULAR:
                showMostPopular(null);
                break;
            case MOVIES_FILTER_TOP_RATED:
                showTopRated(null);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fabMenu.showMenuButton(true);
    }

    public void showMostPopular(View view) {
        fabMenu.setMenuButtonColorNormalResId(R.color.colorPopularFab);
        fabMenu.setMenuButtonColorPressedResId(R.color.colorPopularFab);
        fabMenu.getMenuIconView().setImageResource(R.drawable.ic_whatshot);
        fabMenu.close(true);

        setCurrentFilter(MOVIES_FILTER_POPULAR);
    }

    public void showTopRated(View view) {
        fabMenu.setMenuButtonColorNormalResId(R.color.colorTopRatedFab);
        fabMenu.setMenuButtonColorPressedResId(R.color.colorTopRatedFab);
        fabMenu.getMenuIconView().setImageResource(R.drawable.ic_star_filled);
        fabMenu.close(true);

        setCurrentFilter(MOVIES_FILTER_TOP_RATED);
    }

    @SuppressLint("CommitPrefEdits")
    private void setCurrentFilter(String filter) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MOVIES_FILTER_KEY, filter);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        if (this.fabMenu.isOpened())
            this.fabMenu.close(true);
        else
            super.onBackPressed();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_MOVIES_FILTER_KEY)) {
            Log.d(TAG, "onSharedPreferenceChanged: reset scrollListener state");
            scrollListener.resetState();
        }
    }
}
