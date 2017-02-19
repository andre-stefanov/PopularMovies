package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieDetailsActivity";

    public static final String ARG_MOVIE = "movie";

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.backdrop_imageview)
    ImageView backdropImageView;

    @BindView(R.id.backdrop_progress)
    ContentLoadingProgressBar progressBackdrop;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Movie movie = extras.getParcelable(ARG_MOVIE);

            viewPager.setAdapter(new MovieDetailsPagerAdapter(getSupportFragmentManager(), movie));

            // workaround for viewpager repeatedly calling onCreateView on each fragment
            viewPager.setOffscreenPageLimit(2);

            if (movie != null) {
                progressBackdrop.show();
                PopularMoviesApp.dataManager().loadBackdrop(movie.getBackdropPath(), backdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBackdrop.hide();
                    }

                    @Override
                    public void onError() {

                    }
                });


                toolbar.setTitle(movie.getTitle());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class MovieDetailsPagerAdapter extends FragmentPagerAdapter {

        private final Movie movie;

        MovieDetailsPagerAdapter(FragmentManager fm, Movie movie) {
            super(fm);
            this.movie = movie;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return OverviewFragment.createInstance(movie.getId());
                case 1:
                    return VideosFragment.createInstance(movie.getId());
                case 2:
                    return ReviewsFragment.createInstance(movie.getId());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.movie_overview);
                case 1:
                    return getString(R.string.movie_videos);
                case 2:
                    return getString(R.string.movie_reviews);
                default:
                    return "NO TITLE";
            }
        }
    }

}
