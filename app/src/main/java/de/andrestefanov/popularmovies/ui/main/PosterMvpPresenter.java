package de.andrestefanov.popularmovies.ui.main;

import de.andrestefanov.popularmovies.ui.base.MvpPresenter;

public interface PosterMvpPresenter<V extends PosterMvpView> extends MvpPresenter<V> {

    void setPosterPath(String posterPath);

}
