package de.andrestefanov.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.model.Movie;
import de.andrestefanov.popularmovies.utils.DefaultAnimationListener;

import static de.andrestefanov.popularmovies.Preferences.KEY_MOVIES_FILTER_KEY;
import static de.andrestefanov.popularmovies.Preferences.MOVIES_FILTER_POPULAR;
import static de.andrestefanov.popularmovies.Preferences.MOVIES_FILTER_TOP_RATED;

public class MoviesGridActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, MoviesAdapter.OnMovieClickListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MoviesGridActivity";

    @BindView(R.id.recycler_view_movies)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionMenu fabMenu;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        ButterKnife.bind(this);

        this.sharedPreferences = getSharedPreferences(Preferences.PREFS_FILE_NAME, MODE_PRIVATE);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        recyclerView.setLayoutManager(layoutManager);

        MoviesAdapter adapter = new MoviesAdapter(this);
        this.recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        this.fabMenu.setClosedOnTouchOutside(true);
        this.fabMenu.setIconAnimated(false);
        initFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fabMenu.showMenuButton(true);
    }

    private void initFab() {
        switch (sharedPreferences.getString(KEY_MOVIES_FILTER_KEY, MOVIES_FILTER_POPULAR)) {
            case MOVIES_FILTER_POPULAR:
                fabMenu.setMenuButtonColorNormalResId(R.color.colorPopularFab);
                fabMenu.setMenuButtonColorPressedResId(R.color.colorPopularFab);
                fabMenu.getMenuIconView().setImageResource(R.drawable.ic_whatshot);
                fabMenu.close(true);
                break;
            case MOVIES_FILTER_TOP_RATED:
                fabMenu.setMenuButtonColorNormalResId(R.color.colorTopRatedFab);
                fabMenu.setMenuButtonColorPressedResId(R.color.colorTopRatedFab);
                fabMenu.getMenuIconView().setImageResource(R.drawable.ic_star_filled);
                fabMenu.close(true);
                break;
        }
    }

    public void showMostPopular(View view) {
        saveCurrentFilter(MOVIES_FILTER_POPULAR);
    }

    public void showTopRated(View view) {
        saveCurrentFilter(Preferences.MOVIES_FILTER_TOP_RATED);
    }

    @SuppressLint("CommitPrefEdits")
    private void saveCurrentFilter(String filter) {
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
            initFab();
        }
    }

    @Override
    public void onClick(ImageView imageView, Movie movie) {
        final Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.MOVIE_PARCELABLE_EXTRA, movie);

        Pair[] pairs;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            View decor = getWindow().getDecorView();
            View statusBar = decor.findViewById(android.R.id.statusBarBackground);
            View navBar = decor.findViewById(android.R.id.navigationBarBackground);

            Pair<View, String> p1 = Pair.create((View) imageView, "poster");
            Pair<View, String> p2 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> p3 = Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);

            pairs = new Pair[]{p1, p2, p3};
        } else {
            Pair<View, String> p1 = Pair.create((View) imageView, "poster");
            pairs = new Pair[]{p1};
        }

        @SuppressWarnings("unchecked")
        final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MoviesGridActivity.this,
                pairs);

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
}
