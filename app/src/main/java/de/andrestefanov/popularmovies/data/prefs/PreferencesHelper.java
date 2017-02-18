package de.andrestefanov.popularmovies.data.prefs;

public interface PreferencesHelper {

    void setMovieFilter(MoviesFilter filter);

    MoviesFilter getMovieFilter();

}
