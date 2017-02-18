package de.andrestefanov.popularmovies.ui.details;

import com.squareup.picasso.Callback;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.ui.base.MvpPresenter;

public class MovieBackdropPresenter<V extends MovieBackdropMvpView> implements MvpPresenter<V> {

    private String backdropPath;

    public MovieBackdropPresenter(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public void onAttach(final V view) {
        view.showLoading();
        PopularMoviesApp.dataManager().loadBackdrop(backdropPath, view.getBackdropImageView(), new Callback() {
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
