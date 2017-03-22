package de.andrestefanov.popularmovies.data.db;

import android.database.Cursor;

import de.andrestefanov.popularmovies.data.network.model.Movie;

public interface DbHelper {

    Cursor getFavorites();

    boolean isFavorite(Movie movie);

    boolean toggleFavorite(Movie movie);

}
