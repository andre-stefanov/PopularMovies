package de.andrestefanov.popularmovies.ui.details;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MoviePresenter extends MvpBasePresenter<MvpLceView<MovieDetails>> {

    void loadMovieDetails(int movieId, final boolean pullToRefresh) {
        if (getView() != null) {
            getView().showLoading(pullToRefresh);
            PopularMoviesApp.dataManager().loadMovie(movieId, new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    if (getView() != null) {
                        getView().setData(response.body());
                        getView().showContent();
                    }
                }

                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {
                    getView().showError(t, pullToRefresh);
                }
            });
        }
    }

}
