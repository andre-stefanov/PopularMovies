package de.andrestefanov.popularmovies.ui.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.base.OnMovieSelectedListener;
import de.andrestefanov.popularmovies.ui.details.MovieDetailsFragment;

public class MainActivity extends AppCompatActivity implements OnMovieSelectedListener, OnTabSelectListener, FragmentManager.OnBackStackChangedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    public static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    public static final String STATE_BOTTOM_BAR = "STATE_BOTTOM_BAR";

    Fragment fragment;

    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            fragment = fragmentManager.getFragment(savedInstanceState, TAG_RETAINED_FRAGMENT);

            bottomBar.setTranslationY(savedInstanceState.getFloat(STATE_BOTTOM_BAR));
        } else {
            fragment = new MovieGridFragment();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.fragment_container, fragment, TAG_RETAINED_FRAGMENT)
                    .commit();
        }

        bottomBar.setOnTabSelectListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, TAG_RETAINED_FRAGMENT, fragment);
        outState.putFloat(STATE_BOTTOM_BAR, bottomBar.getTranslationY());
    }

    @Override
    public void onMovieSelected(Movie movie) {
        Fragment fragment = MovieDetailsFragment.createInstance(movie.getId());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * The method being called when currently visible {@link BottomBarTab} changes.
     * <p>
     * This listener is fired for the first time after the items have been set and
     * also after a configuration change, such as when screen orientation changes
     * from portrait to landscape.
     *
     * @param tabId the new visible {@link BottomBarTab}
     */
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_popular:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.POPULAR);
                break;
            case R.id.tab_top_rated:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.TOP_RATED);
                break;
            case R.id.tab_favorites:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.POPULAR);
                break;
            case R.id.tab_settings:

                break;
        }
    }

    /**
     * Called whenever the contents of the back stack change.
     */
    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            bottomBar.animate().translationY(bottomBar.getHeight());
        } else {
            bottomBar.animate().translationY(0);
        }
    }
}
