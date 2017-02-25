package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import de.andrestefanov.popularmovies.data.network.model.Review;

public class ReviewsFragment extends Fragment {

    private static final String TAG = "ReviewsFragment";

    @BindView(R.id.contentView)
    LinearLayout reviewsContainer;

    private MovieDetails movie;

    public static ReviewsFragment createInstance(MovieDetails movie) {
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        reviewsFragment.movie = movie;
        return reviewsFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, rootView);

        for (Review review : movie.getReviews()) {
            View videoView = getActivity().getLayoutInflater().inflate(R.layout.view_review, reviewsContainer, false);
            ((TextView) videoView.findViewById(R.id.review_title)).setText(review.getAuthor());
            ((TextView) videoView.findViewById(R.id.review_content)).setText(review.getContent());
            reviewsContainer.addView(videoView);
        }

        return rootView;
    }

}
