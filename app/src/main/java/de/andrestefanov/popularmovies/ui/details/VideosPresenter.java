package de.andrestefanov.popularmovies.ui.details;

import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.ui.base.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideosPresenter<V extends MovieVideosMvpView> implements MvpPresenter<V> {

    private final int movieId;

    public VideosPresenter(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public void onAttach(final V view) {
        view.showLoading();
        PopularMoviesApp.dataManager().loadMovieVideos(movieId, new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                view.hideLoading();
                view.showVideos(response.body());
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDetach() {

    }

    public void loadVideoImage(String videoKey, ImageView imageView) {
        PopularMoviesApp.dataManager().loadVideoImage(videoKey, imageView);
    }
}
