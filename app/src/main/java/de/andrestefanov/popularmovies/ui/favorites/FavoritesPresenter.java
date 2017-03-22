package de.andrestefanov.popularmovies.ui.favorites;

import android.database.Cursor;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Movie;

class FavoritesPresenter extends MvpBasePresenter<FavoritesFragment> {
    
    void loadFavorites() {
        Cursor cursor = PopularMoviesApp.dataManager().getFavorites();

        ArrayList<Movie> result = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(0));
            movie.setPosterPath(cursor.getString(1));
            result.add(movie);
        }

        if (getView() != null) {
            getView().setData(result);
            getView().showContent();
        }
    }

}
