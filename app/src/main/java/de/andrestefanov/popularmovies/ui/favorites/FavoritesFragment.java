package de.andrestefanov.popularmovies.ui.favorites;

import android.support.annotation.NonNull;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.PosterGridFragment;

public class FavoritesFragment extends PosterGridFragment<FavoritesFragment, FavoritesPresenter> {

    public FavoritesFragment() {
        super();
    }

    public static FavoritesFragment create() {
        return new FavoritesFragment();
    }

    @NonNull
    @Override
    public FavoritesPresenter createPresenter() {
        return new FavoritesPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadFavorites();
    }

    public void addData(Movie movie) {
        getAdapter().addData(movie);
    }
}
