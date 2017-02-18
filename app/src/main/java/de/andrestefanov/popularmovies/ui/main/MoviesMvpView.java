package de.andrestefanov.popularmovies.ui.main;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.base.MvpView;

public interface MoviesMvpView extends MvpView {

    void showMoreData(List<Movie> movies);

    void showFilter(MoviesFilter filter);

    void clear();

}
