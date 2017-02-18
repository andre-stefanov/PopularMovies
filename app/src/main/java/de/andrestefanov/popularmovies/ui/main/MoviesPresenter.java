package de.andrestefanov.popularmovies.ui.main;

import android.util.Log;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPresenter<V extends MoviesMvpView> implements MoviesMvpPresenter<V> {

    private static final String TAG = "MoviesPresenter";

    private int pagesLoaded = 0;

    private V view;

    @Override
    public void requestMoreData() {
        switch (PopularMoviesApp.dataManager().getMovieFilter()) {
            case TOP_RATED:
                PopularMoviesApp.dataManager().loadTopRatedMovies(++pagesLoaded, new MoreDataRequestCallback());
                break;
            default:
                PopularMoviesApp.dataManager().loadPopularMovies(++pagesLoaded, new MoreDataRequestCallback());
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {

    }

    @Override
    public void onFilterChange(MoviesFilter filter) {
        PopularMoviesApp.dataManager().setMovieFilter(filter);
        view.clear();
        pagesLoaded = 0;
        requestMoreData();
    }

    @Override
    public void onAttach(V view) {
        this.view = view;

        if (pagesLoaded == 0)
            requestMoreData();
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    private class MoreDataRequestCallback implements Callback<List<Movie>> {

        @Override
        public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
            view.showMoreData(response.body());
        }

        @Override
        public void onFailure(Call<List<Movie>> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
        }
    }

}
