package de.andrestefanov.popularmovies.ui.favorites;

import android.support.annotation.NonNull;

import de.andrestefanov.popularmovies.ui.base.BaseMoviesGridFragment;

public class FavoritesFragment extends BaseMoviesGridFragment<FavoritesFragment, FavoritesPresenter> {

    public static FavoritesFragment createInstance() {
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

}
