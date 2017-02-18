package de.andrestefanov.popularmovies.ui.main;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.base.MvpPresenter;

public interface MoviesMvpPresenter<V extends MoviesMvpView> extends MvpPresenter<V> {

    void requestMoreData();

    void onMovieClicked(Movie movie);

    void onFilterChange(MoviesFilter filter);

}
