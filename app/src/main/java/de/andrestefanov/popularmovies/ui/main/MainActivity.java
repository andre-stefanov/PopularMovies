package de.andrestefanov.popularmovies.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ncapdevi.fragnav.FragNavController;

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

public class MainActivity extends AppCompatActivity implements OnMovieSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    FragNavController tabFragController;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<Fragment> rootFragments = new ArrayList<>(3);
        rootFragments.add(new PopularMoviesGridFragment());
        rootFragments.add(new TopRatedMoviesGridFragment());
        rootFragments.add(new FavoritesFragment());

        tabFragController = new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.fragment_container, rootFragments, 0);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemReselectedListener(this);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (!tabFragController.isRootFragment()) {
            tabFragController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (tabFragController != null) {
            tabFragController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (findViewById(R.id.fragment_movie) == null)
            tabFragController.pushFragment(MovieDetailsFragment.createInstance(movie.getId()));
        else
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie, MovieDetailsFragment.createInstance(movie.getId()))
                .commit();
    }

    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not
     * be selected. Consider setting non-selectable items as disabled preemptively to
     * make them appear non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!tabFragController.isRootFragment()) {
            tabFragController.popFragment();
        }

        switch (item.getItemId()) {
            case R.id.nav_most_popular:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.POPULAR);
                tabFragController.switchTab(FragNavController.TAB1);
                break;
            case R.id.nav_top_rated:
                PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.TOP_RATED);
                tabFragController.switchTab(FragNavController.TAB2);
                break;
            case R.id.nav_favorites:
                tabFragController.switchTab(FragNavController.TAB3);
                break;
        }
        return true;
    }

    /**
     * Called when the currently selected item in the bottom navigation menu is selected again.
     *
     * @param item The selected item
     */
    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        if (!tabFragController.isRootFragment()) {
            tabFragController.popFragment();
        }
    }
}
