package de.andrestefanov.popularmovies.ui.popular;

import android.support.annotation.NonNull;

import de.andrestefanov.popularmovies.ui.base.BaseMoviesGridFragment;
import de.andrestefanov.popularmovies.ui.base.EndlessMoviesAdapter;
import de.andrestefanov.popularmovies.ui.base.PosterGridAdapter;

public class PopularMoviesGridFragment extends BaseMoviesGridFragment<PopularMoviesGridFragment, PopularMoviesPresenter> implements EndlessMoviesAdapter.OnMoreDataRequestListener {

    @NonNull
    @Override
    public PopularMoviesPresenter createPresenter() {
        return new PopularMoviesPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadMovies(pullToRefresh);
    }

    @Override
    protected PosterGridAdapter createAdapter() {
        return new EndlessMoviesAdapter(this);
    }

    @Override
    public void onRequestMoreData() {
        loadData(false);
    }
}
