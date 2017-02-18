package de.andrestefanov.popularmovies.ui.details;

import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Review;
import de.andrestefanov.popularmovies.data.network.model.ReviewsPage;
import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.ui.base.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewsPresenter<V extends MovieReviewMvpView> implements MvpPresenter<V> {

    private static final String TAG = "ReviewsPresenter";

    private final int movieId;

    private int page;

    private V view;

    public ReviewsPresenter(int movieId) {
        this.movieId = movieId;
        this.page = 0;
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
        loadMoreReviews();
    }

    private void loadMoreReviews() {
        Log.d("!!!", "loadMoreReviews() called");
        view.showLoading();
        PopularMoviesApp.dataManager().loadMovieReviews(movieId, ++page, new Callback<ReviewsPage>() {
            @Override
            public void onResponse(Call<ReviewsPage> call, Response<ReviewsPage> response) {
                Log.d(TAG, "onResponse: " + response.body());
                view.hideLoading();
                view.showReviews(response.body().getReviews());
            }

            @Override
            public void onFailure(Call<ReviewsPage> call, Throwable t) {
                Log.d("!!!", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
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
