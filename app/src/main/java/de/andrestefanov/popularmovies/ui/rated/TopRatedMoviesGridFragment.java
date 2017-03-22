package de.andrestefanov.popularmovies.ui.rated;

import android.support.annotation.NonNull;

import de.andrestefanov.popularmovies.ui.base.BaseMoviesGridFragment;
import de.andrestefanov.popularmovies.ui.base.EndlessMoviesAdapter;
import de.andrestefanov.popularmovies.ui.base.PosterGridAdapter;

public class TopRatedMoviesGridFragment extends BaseMoviesGridFragment<TopRatedMoviesGridFragment, TopRatedMoviesPresenter> implements EndlessMoviesAdapter.OnMoreDataRequestListener {

    @NonNull
    @Override
    public TopRatedMoviesPresenter createPresenter() {
        return new TopRatedMoviesPresenter();
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
