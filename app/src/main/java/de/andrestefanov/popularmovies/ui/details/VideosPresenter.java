package de.andrestefanov.popularmovies.ui.details;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class VideosPresenter extends MvpBasePresenter<MvpLceView<List<Video>>> {

    private int movieId;

    VideosPresenter(int movieId) {
        this.movieId = movieId;
    }

    void loadData(final boolean pullToRefresh) {
        if (getView() != null) {
            getView().showLoading(pullToRefresh);
        }

        PopularMoviesApp.dataManager().loadMovieVideos(movieId, new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (getView() != null) {
                    getView().setData(response.body());
                    getView().showContent();
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                if (getView() != null) {
                    getView().showError(t, pullToRefresh);
                }
            }
        });
    }
}
