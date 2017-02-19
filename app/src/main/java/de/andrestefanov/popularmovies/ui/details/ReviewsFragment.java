package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Review;

public class ReviewsFragment extends MvpLceFragment<LinearLayout, List<Review>, MvpLceView<List<Review>>, ReviewsPresenter> {

    @BindView(R.id.contentView)
    LinearLayout reviewsContainer;

    private int movieId;

    public static ReviewsFragment createInstance(int movieId) {
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.movieId = movieId;
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData(false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public ReviewsPresenter createPresenter() {
        return new ReviewsPresenter(movieId);
    }

    @Override
    public void setData(List<Review> data) {
        for (Review review : data) {
            View videoView = getActivity().getLayoutInflater().inflate(R.layout.view_review, reviewsContainer, false);
            ((TextView) videoView.findViewById(R.id.review_title)).setText(review.getAuthor());
            ((TextView) videoView.findViewById(R.id.review_content)).setText(review.getContent());
            reviewsContainer.addView(videoView);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadReviews(pullToRefresh);
    }
}
