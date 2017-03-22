package de.andrestefanov.popularmovies.ui.rated;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.BaseMoviesPresenter;
import retrofit2.Callback;

public class TopRatedMoviesPresenter extends BaseMoviesPresenter<TopRatedMoviesGridFragment> {

    @Override
    protected void loadData(int page, Callback<List<Movie>> callback) {
        PopularMoviesApp.dataManager().loadTopRatedMovies(page, callback);
    }

}
