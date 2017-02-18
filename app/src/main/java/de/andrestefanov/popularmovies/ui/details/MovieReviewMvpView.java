package de.andrestefanov.popularmovies.ui.details;

import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Review;
import de.andrestefanov.popularmovies.ui.base.MvpView;

public interface MovieReviewMvpView extends MvpView {

    void showReviews(List<Review> reviews);

}
