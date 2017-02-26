package de.andrestefanov.popularmovies.ui.favorites;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.PosterGridFragment;

public class FavoritesFragment extends PosterGridFragment<FavoritesFragment, FavoritesPresenter> {



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

    public void addData(Movie movie) {
        getAdapter().addData(movie);
    }
}
