package de.andrestefanov.popularmovies.data.prefs;

import android.content.SharedPreferences;

public interface PreferencesHelper {

    SharedPreferences getSharedPreferences();

    void setMovieFilter(MoviesFilter filter);

    MoviesFilter getMovieFilter();

}
