package de.andrestefanov.popularmovies.ui.main;

import android.support.annotation.NonNull;

import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.base.PosterGridAdapter;
import de.andrestefanov.popularmovies.ui.base.PosterGridFragment;

public class MovieGridFragment extends PosterGridFragment<MoviesView, MoviesPresenter> implements EndlessMoviesAdapter.OnMoreDataRequestListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieGridFragment";

    @NonNull
    @Override
    public MoviesPresenter createPresenter() {
        return new MoviesPresenter();
    }

    public void onFilterChanged(MoviesFilter filter) {
        getPresenter().setFilter(filter);
    }

    @Override
    public void loadData(boolean refresh) {
        getPresenter().loadMovies(refresh);
    }

    @Override
    public void onRequestMoreData() {
        loadData(false);
    }

    @Override
    protected PosterGridAdapter createAdapter() {
        return new EndlessMoviesAdapter(this);
    }
}
