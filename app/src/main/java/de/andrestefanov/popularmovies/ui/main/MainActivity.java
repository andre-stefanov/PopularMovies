package de.andrestefanov.popularmovies.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.base.OnMovieSelectedListener;
import de.andrestefanov.popularmovies.ui.details.MovieDetailsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMovieSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    ActionBarDrawerToggle toggle;

    public static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);

        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            fragment = fragmentManager.getFragment(savedInstanceState, TAG_RETAINED_FRAGMENT);

            toolbar.setVisibility(View.INVISIBLE);
        } else {
            fragment = new MovieGridFragment();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.fragment_container, fragment, TAG_RETAINED_FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, TAG_RETAINED_FRAGMENT, fragment);
        outState.putBoolean("toolbar-visibility", toolbar.getVisibility() == View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        revealToolbar();
    }

    private void revealToolbar() {
        AlphaAnimation anim = new AlphaAnimation(0, 1);
        anim.setDuration(300);
        anim.setFillAfter(true);
        toolbar.startAnimation(anim);
    }

    private void hideToolbar() {
        AlphaAnimation anim = new AlphaAnimation(1, 0);
        anim.setDuration(300);
        anim.setFillAfter(true);
        toolbar.startAnimation(anim);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_most_popular) {
            PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.POPULAR);
        } else if (id == R.id.nav_top_rated) {
            PopularMoviesApp.dataManager().setMovieFilter(MoviesFilter.TOP_RATED);
        } else if (id == R.id.nav_favorites) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        hideToolbar();
        Fragment fragment = MovieDetailsFragment.createInstance(movie.getId());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
