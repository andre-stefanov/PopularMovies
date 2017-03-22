package de.andrestefanov.popularmovies.ui.popular;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.BaseMoviesPresenter;
import retrofit2.Callback;

public class PopularMoviesPresenter extends BaseMoviesPresenter<PopularMoviesGridFragment> {

    @Override
    protected void loadData(int page, Callback<List<Movie>> callback) {
        PopularMoviesApp.dataManager().loadPopularMovies(page, callback);
    }

}
