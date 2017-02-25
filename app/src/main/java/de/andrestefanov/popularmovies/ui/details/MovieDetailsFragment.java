package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;

public class MovieDetailsFragment extends MvpLceFragment<LinearLayout, MovieDetails, MvpLceView<MovieDetails>, MoviePresenter> {

    private static final String TAG = "MovieDetailsFragment";

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

    private int movieId;

    public static MovieDetailsFragment createInstance(int movieId) {
        Log.d(TAG, "createInstance() called with: movieId = [" + movieId + "]");
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.movieId = movieId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CoordinatorLayout view = (CoordinatorLayout) inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);

        tabLayout.setupWithViewPager(viewPager);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public MoviePresenter createPresenter() {
        Log.d(TAG, "createPresenter() called");
        return new MoviePresenter();
    }

    @Override
    public void setData(MovieDetails movieDetails) {
        Log.d(TAG, "setData() called with: movieDetails = [" + movieDetails + "]");

        progressBackdrop.show();
        PopularMoviesApp.dataManager().loadBackdrop(movieDetails.getBackdropPath(), backdropImageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBackdrop.hide();
            }

            @Override
            public void onError() {

            }
        });

        toolbar.setTitle(movieDetails.getTitle());

        MovieDetailsPagerAdapter pagerAdapter = new MovieDetailsPagerAdapter(getChildFragmentManager(), movieDetails);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void showContent() {
        Log.d(TAG, "showContent() called");
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Log.d(TAG, "loadData() called with: pullToRefresh = [" + pullToRefresh + "]");
        getPresenter().loadMovieDetails(movieId, pullToRefresh);
    }

    public class MovieDetailsPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        MovieDetailsPagerAdapter(FragmentManager fm, MovieDetails movie) {
            super(fm);

            fragments = new Fragment[] {
                    OverviewFragment.createInstance(movie),
                    VideosFragment.createInstance(movie),
                    ReviewsFragment.createInstance(movie)
            };
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return fragments.length;
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