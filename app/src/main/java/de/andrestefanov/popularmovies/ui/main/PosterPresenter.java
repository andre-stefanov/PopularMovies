package de.andrestefanov.popularmovies.ui.main;

import com.squareup.picasso.Callback;

import de.andrestefanov.popularmovies.PopularMoviesApp;

public class PosterPresenter<V extends PosterMvpView> implements PosterMvpPresenter<V> {

    private final V view;

    public PosterPresenter(V view) {
        this.view = view;
    }

    @Override
    public void setPosterPath(String posterPath) {
        view.showLoading();
        PopularMoviesApp.dataManager().loadPoster(posterPath, view.getImageView(), new Callback() {
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
    public void onAttach(V view) {

    }

    @Override
    public void onDetach() {

    }
}
