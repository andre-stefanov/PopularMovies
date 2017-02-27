package de.andrestefanov.popularmovies.data.prefs;

import android.content.SharedPreferences;

import static de.andrestefanov.popularmovies.data.prefs.PrefConstants.*;

public class PreferencesHelperImpl implements PreferencesHelper {

    private SharedPreferences sharedPreferences;

    public PreferencesHelperImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
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
