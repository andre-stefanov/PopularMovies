package de.andrestefanov.popularmovies.ui.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseMoviesPresenter<V extends BaseMoviesGridFragment> extends MvpBasePresenter<V> implements Callback<List<Movie>> {

    @SuppressWarnings("unused")
    private static final String TAG = "MoviesPresenter";

    private List<Movie> data = new ArrayList<>();

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    public void loadMovies(boolean refresh) {
        if (refresh)
            data.clear();

        if (getView() != null && refresh) {
            getView().showLoading(true);
        }

        int nextPage = data.size() / Constants.TMDB_API_MOVIES_PER_PAGE + 1;
        loadData(nextPage, this);
    }

    protected abstract void loadData(int page, Callback<List<Movie>> callback);

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
