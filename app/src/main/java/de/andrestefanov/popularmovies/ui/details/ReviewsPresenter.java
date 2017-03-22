package de.andrestefanov.popularmovies.ui.details;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.ArrayList;
import java.util.List;

import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.data.network.model.Review;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ReviewsPresenter extends MvpBasePresenter<MvpLceView<List<Review>>> {

    private int movieId;

    private List<Review> data = new ArrayList<>();

    ReviewsPresenter(int movieId) {
        this.movieId = movieId;
    }

    void loadData(final boolean pullToRefresh) {
        if (getView() != null) {
            getView().showLoading(pullToRefresh);
        }

        int pageToLoad = data.size() / 20 + 1;

        PopularMoviesApp.dataManager().loadMovieReviews(movieId, pageToLoad, new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (getView() != null) {
                    data.addAll(response.body());
                    getView().setData(data);
                    getView().showContent();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                if (getView() != null) {
                    getView().showError(t, pullToRefresh);
                }
            }
        });
    }

}
