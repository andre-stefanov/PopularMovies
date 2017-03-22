package de.andrestefanov.popularmovies.ui.favorites;

import android.app.Activity;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;

import de.andrestefanov.popularmovies.data.db.AppContentProvider;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.BaseMoviesGridFragment;
import de.andrestefanov.popularmovies.ui.base.PosterGridAdapter;

public class FavoritesFragment extends BaseMoviesGridFragment<FavoritesFragment, FavoritesPresenter> {

    private ContentObserver observer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        observer = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadData(true);
            }
        };

        getContext().getContentResolver().registerContentObserver(AppContentProvider.Favorites.CONTENT_URI, true, observer);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().getContentResolver().unregisterContentObserver(observer);
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

    @Override
    protected PosterGridAdapter createAdapter() {
        return new PosterGridAdapter() {
            @Override
            public void setData(List<Movie> data) {
                getData().clear();
                getData().addAll(data);
                notifyDataSetChanged();
            }
        };
    }
}
