package de.andrestefanov.popularmovies.ui.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.base.OnMovieSelectedListener;
import de.andrestefanov.popularmovies.ui.details.MovieDetailsFragment;
import de.andrestefanov.popularmovies.ui.favorites.FavoritesFragment;
import de.andrestefanov.popularmovies.ui.popular.PopularMoviesGridFragment;
import de.andrestefanov.popularmovies.ui.rated.TopRatedMoviesGridFragment;

public class MainActivity extends AppCompatActivity implements OnMovieSelectedListener, OnTabSelectListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    public static final String STATE_BOTTOM_BAR = "STATE_BOTTOM_BAR";

    FragNavController fragmentsController;

    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<Fragment> rootFragments = new ArrayList<>(3);
        rootFragments.add(new PopularMoviesGridFragment());
        rootFragments.add(new TopRatedMoviesGridFragment());
        rootFragments.add(new FavoritesFragment());

        fragmentsController = new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.fragment_container, rootFragments, 0);

        if (savedInstanceState != null) {
            bottomBar.setTranslationY(savedInstanceState.getFloat(STATE_BOTTOM_BAR));
        }

        bottomBar.setOnTabSelectListener(this, false);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (!fragmentsController.isRootFragment()) {
            fragmentsController.popFragment();
            bottomBar.animate().translationY(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fragmentsController != null) {
            fragmentsController.onSaveInstanceState(outState);
        }

        outState.putFloat(STATE_BOTTOM_BAR, bottomBar.getTranslationY());
    }

    @Override
    public void onMovieSelected(Movie movie) {
        fragmentsController.pushFragment(MovieDetailsFragment.createInstance(movie.getId()));
        bottomBar.animate().translationY(bottomBar.getHeight() * 2);
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
                fragmentsController.switchTab(FragNavController.TAB1);
                break;
            case R.id.tab_top_rated:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.TOP_RATED);
                fragmentsController.switchTab(FragNavController.TAB2);
                break;
            case R.id.tab_favorites:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.POPULAR);
                fragmentsController.switchTab(FragNavController.TAB3);
                break;
            case R.id.tab_settings:
                break;
        }
    }
}
