package de.andrestefanov.popularmovies.ui.base;

import android.content.SharedPreferences;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.ArrayList;
import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.DataManager;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.PrefConstants;
import de.andrestefanov.popularmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseMoviesPresenter extends MvpBasePresenter<MvpLceView<List<Movie>>> implements SharedPreferences.OnSharedPreferenceChangeListener, Callback<List<Movie>> {

    @SuppressWarnings("unused")
    private static final String TAG = "MoviesPresenter";

    private List<Movie> data = new ArrayList<>();

    private DataManager dataManager = PopularMoviesApp.dataManager();

    @Override
    public void attachView(MvpLceView<List<Movie>> view) {
        super.attachView(view);
        dataManager.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        dataManager.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void loadMovies(boolean refresh) {
        if (refresh)
            data.clear();

        int nextPage = data.size() / Constants.TMDB_API_MOVIES_PER_PAGE + 1;
        switch (PopularMoviesApp.dataManager().getMovieFilter()) {
            case TOP_RATED:
                PopularMoviesApp.dataManager().loadTopRatedMovies(
                        nextPage,
                        this);
                break;
            default:
                PopularMoviesApp.dataManager().loadPopularMovies(
                        nextPage,
                        this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PrefConstants.KEY_MOVIES_FILTER_KEY.equals(key) && getView() != null) {
            getView().showLoading(false);
            loadMovies(true);
        }
    }

    @Override
    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
        if (getView() != null) {
            data.addAll(response.body());
            getView().setData(data);
            getView().showContent();
        }
    }

    @Override
    public void onFailure(Call<List<Movie>> call, Throwable t) {
        if (getView() != null) {
            getView().showError(t, false);
        }
    }

}
