package de.andrestefanov.popularmovies.ui.main;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.base.BaseMoviesGridFragment;
import de.andrestefanov.popularmovies.ui.base.BaseMoviesPresenter;
import de.andrestefanov.popularmovies.ui.base.PosterGridAdapter;

public class MovieGridFragment extends BaseMoviesGridFragment<MvpLceView<List<Movie>>, BaseMoviesPresenter> implements EndlessMoviesAdapter.OnMoreDataRequestListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieGridFragment";

    @Override
    public void onRequestMoreData() {
        loadData(false);
    }

    @Override
    protected PosterGridAdapter createAdapter() {
        return new EndlessMoviesAdapter(this);
    }

    @NonNull
    @Override
    public BaseMoviesPresenter createPresenter() {
        return new BaseMoviesPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadMovies(pullToRefresh);
    }
}
