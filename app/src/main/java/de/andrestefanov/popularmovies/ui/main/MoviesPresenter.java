package de.andrestefanov.popularmovies.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.DataManager;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MoviesPresenter extends MvpBasePresenter<MoviesView> {

    private int pagesRequested = 0;

    private DataManager dataManager = PopularMoviesApp.dataManager();

    void loadMovies(boolean refresh) {
        pagesRequested = refresh ? 1 : pagesRequested + 1;
        switch (PopularMoviesApp.dataManager().getMovieFilter()) {
            case TOP_RATED:
                PopularMoviesApp.dataManager().loadTopRatedMovies(
                        pagesRequested,
                        new MoreDataRequestCallback());
                break;
            default:
                PopularMoviesApp.dataManager().loadPopularMovies(
                        pagesRequested,
                        new MoreDataRequestCallback());
        }
    }

    void setFilter(MoviesFilter filter) {
        if (dataManager.getMovieFilter().equals(filter))
            return;

        PopularMoviesApp.dataManager().setMovieFilter(filter);
        if (getView() != null) {
            getView().clear();
            
            getView().showLoading(false);

            loadMovies(true);
        }
    }

    private class MoreDataRequestCallback implements Callback<List<Movie>> {

        @Override
        public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
            if (getView() != null) {
                getView().addData(response.body());
                getView().showContent();
            }
        }

        @Override
        public void onFailure(Call<List<Movie>> call, Throwable t) {
            if (getView() != null) {
                getView().showError(t, false);
            }
        }
    }

}
