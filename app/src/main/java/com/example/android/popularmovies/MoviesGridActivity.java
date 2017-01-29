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
import com.example.android.popularmovies.model.MoviesPage;
import com.example.android.popularmovies.utils.DefaultAnimationListener;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.FooterAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.items.ProgressItem;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.popularmovies.Preferences.KEY_MOVIES_FILTER_KEY;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_POPULAR;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_TOP_RATED;

public class MoviesGridActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Callback<MoviesPage> {

    private static final String TAG = "MoviesGridActivity";

    @BindView(R.id.recycler_view_movies)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionMenu fabMenu;

    private FastItemAdapter<MovieGridItem> adapter;

    private FooterAdapter<ProgressItem> footerAdapter;

    private SharedPreferences sharedPreferences;

    private TMDBClient tmdbClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        ButterKnife.bind(this);

        this.tmdbClient = new TMDBClient(this);

        this.sharedPreferences = getSharedPreferences(Preferences.PREFS_FILE_NAME, MODE_PRIVATE);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        recyclerView.setItemAnimator(new AlphaCrossFadeAnimator());
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(20) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreMovies(currentPage);
            }
        });

        this.adapter = new FastItemAdapter<>();
        this.footerAdapter = new FooterAdapter<>();
        recyclerView.setAdapter(footerAdapter.wrap(adapter));

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (footerAdapter.getItemViewType(position)) {
                    case R.id.movie_grid_item_id:
                        return 1;
                    default:
                        return 2;
                }
            }
        });

        footerAdapter.add(new ProgressItem().withEnabled(true));

        adapter.withSelectable(true);
        adapter.withOnClickListener(new FastAdapter.OnClickListener<MovieGridItem>() {
            @Override
            public boolean onClick(View v, IAdapter<MovieGridItem> adapter, MovieGridItem item, int position) {
                final Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.MOVIE_PARCELABLE_EXTRA, item.movie);

                View decor = getWindow().getDecorView();
                View statusBar = decor.findViewById(android.R.id.statusBarBackground);
                View navBar = decor.findViewById(android.R.id.navigationBarBackground);

                Pair<View, String> p1 = Pair.create(v, "poster");
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
                return true;
            }
        });

        loadMoreMovies(1);

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
            adapter.clear();
        }
    }

    public void loadMoreMovies(int page) {

        String currentFilter = sharedPreferences.getString(KEY_MOVIES_FILTER_KEY, MOVIES_FILTER_POPULAR);

        if (currentFilter.equals(MOVIES_FILTER_POPULAR)) {
            tmdbClient.loadPopularMoviesPage(page, this, Locale.getDefault().getLanguage());
        } else if (currentFilter.equals(MOVIES_FILTER_TOP_RATED)) {
            tmdbClient.loadTopRatedMoviesPage(page, this, Locale.getDefault().getLanguage());
        }
    }

    @Override
    public void onResponse(Call<MoviesPage> call, Response<MoviesPage> response) {
        List<MovieGridItem> results = new ArrayList<>();
        for (Movie movie : response.body().getResults()) {
            results.add(new MovieGridItem(movie));
        }
        adapter.add(results);
    }

    @Override
    public void onFailure(Call<MoviesPage> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
    }
}
