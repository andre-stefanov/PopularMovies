package de.andrestefanov.popularmovies.data.prefs;

import android.content.SharedPreferences;

import static de.andrestefanov.popularmovies.data.prefs.MoviesFilter.MOVIES_FILTER_POPULAR;

public class PreferencesHelperImpl implements PreferencesHelper {

    private static final String KEY_MOVIES_FILTER_KEY = "movie_filter";

    private SharedPreferences sharedPreferences;

    public PreferencesHelperImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void setMovieFilter(MoviesFilter filter) {
        sharedPreferences.edit().putString(KEY_MOVIES_FILTER_KEY, filter.getValue()).apply();
    }

    @Override
    public MoviesFilter getMovieFilter() {
        return MoviesFilter.fromString(sharedPreferences.getString(KEY_MOVIES_FILTER_KEY, MOVIES_FILTER_POPULAR));
    }
}
