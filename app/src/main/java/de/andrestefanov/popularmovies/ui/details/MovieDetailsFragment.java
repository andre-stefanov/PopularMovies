package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;

public class MovieDetailsFragment extends MvpLceViewStateFragment<LinearLayout, MovieDetails, MvpLceView<MovieDetails>, MoviePresenter> {

    private static final String TAG = "MovieDetailsFragment";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.backdrop_imageview)
    ImageView backdropImageView;

    @BindView(R.id.backdrop_progress)
    ContentLoadingProgressBar progressBackdrop;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private int movieId;

    private MovieDetails data;

    public static MovieDetailsFragment createInstance(int movieId) {
        Log.d(TAG, "createInstance() called with: movieId = [" + movieId + "]");
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.movieId = movieId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        CoordinatorLayout view = (CoordinatorLayout) inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, view);

        if (!getResources().getBoolean(R.bool.large_layout)) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFabChecked(PopularMoviesApp.dataManager().toggleFavorite(data.toMovie()));
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
        return new MoviePresenter();
    }

    @Override
    public void setData(MovieDetails movieDetails) {
        this.data = movieDetails;
        setFabChecked(PopularMoviesApp.dataManager().isFavorite(data.toMovie()));

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

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_overview, OverviewFragment.createInstance(movieDetails))
                .commit();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_videos, VideosFragment.createInstance(movieId))
                .commit();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_reviews, ReviewsFragment.createInstance(movieId))
                .commit();
    }

    private void setFabChecked(boolean checked) {
        if (checked)
            fab.setImageResource(R.drawable.ic_favorite);
        else
            fab.setImageResource(R.drawable.ic_favorite_border);
    }

    @NonNull
    @Override
    public LceViewState<MovieDetails, MvpLceView<MovieDetails>> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
    }

    @Override
    public MovieDetails getData() {
        return data;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadMovieDetails(movieId, pullToRefresh);
    }

}
