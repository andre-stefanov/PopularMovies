package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Review;

public class ReviewsFragment extends MvpLceViewStateFragment<RelativeLayout, List<Review>, MvpLceView<List<Review>>, ReviewsPresenter> {

    @SuppressWarnings("unused")
    private static final String TAG = "ReviewsFragment";

    @BindView(R.id.contentView)
    LinearLayout reviewsContainer;

    private int movieId;

    private List<Review> data;

    public static ReviewsFragment createInstance(int movieId) {
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        reviewsFragment.movieId = movieId;
        return reviewsFragment;
    }

    @NonNull
    @Override
    public ReviewsPresenter createPresenter() {
        return new ReviewsPresenter(movieId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @NonNull
    @Override
    public LceViewState<List<Review>, MvpLceView<List<Review>>> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Review> getData() {
        return data;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(List<Review> data) {
        this.data = data;

        for (Review review : data) {
            View videoView = getActivity().getLayoutInflater().inflate(R.layout.view_review, reviewsContainer, false);
            ((TextView) videoView.findViewById(R.id.review_title)).setText(review.getAuthor());
            ((TextView) videoView.findViewById(R.id.review_content)).setText(review.getContent());
            reviewsContainer.addView(videoView);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData(pullToRefresh);
    }
}
