package de.andrestefanov.popularmovies.ui.details;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Review;
import de.andrestefanov.popularmovies.data.network.model.ReviewsPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ReviewsPresenter extends MvpBasePresenter<MvpLceView<List<Review>>> {

    private final int movieId;

    private int page;

    ReviewsPresenter(int movieId) {
        this.movieId = movieId;
        this.page = 0;
    }

    void loadReviews(final boolean pullToRefresh) {
        if (getView() != null) {
            getView().showLoading(pullToRefresh);
            PopularMoviesApp.dataManager().loadMovieReviews(movieId, ++page, new Callback<ReviewsPage>() {
                @Override
                public void onResponse(Call<ReviewsPage> call, Response<ReviewsPage> response) {
                    getView().setData(response.body().getReviews());
                    getView().showContent();
                }

                @Override
                public void onFailure(Call<ReviewsPage> call, Throwable t) {
                    getView().showError(t, pullToRefresh);
                }
            });
        }
    }

}
