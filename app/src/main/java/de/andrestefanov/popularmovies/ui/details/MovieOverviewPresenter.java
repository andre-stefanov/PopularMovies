package de.andrestefanov.popularmovies.ui.details;

import com.squareup.picasso.Callback;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.MvpPresenter;

public class MovieOverviewPresenter<V extends MovieOverviewMvpView> implements MvpPresenter<V> {

    private Movie movie;

    public MovieOverviewPresenter(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onAttach(final V view) {
        view.showLoading();
        view.showMovie(movie);
        PopularMoviesApp.dataManager().loadPoster(movie.getPosterPath(), view.getPosterImageView(), new Callback() {
            @Override
            public void onSuccess() {
                view.hideLoading();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onDetach() {

    }
}
