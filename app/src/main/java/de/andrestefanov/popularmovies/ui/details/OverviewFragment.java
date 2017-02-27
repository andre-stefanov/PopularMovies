package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class OverviewFragment extends Fragment {

    private static final String TAG = "OverviewFragment";

    private MovieDetails movie;

    @BindView(R.id.imageview_movie_poster)
    ImageView moviePoster;

    @BindView(R.id.textview_movie_synopsis)
    TextView movieOverview;

    @BindView(R.id.movie_rating_indicator)
    MaterialRatingBar ratingBar;

    @BindView(R.id.movie_rating_text)
    TextView ratingText;

    public static OverviewFragment createInstance(MovieDetails movie) {
        OverviewFragment fragment = new OverviewFragment();
        fragment.movie = movie;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        ButterKnife.bind(this, rootView);

        SpannableString spannableString = new SpannableString(movie.getOverview());
        spannableString.setSpan(
                new LeadingMargin(
                        (getResources().getDimensionPixelSize(R.dimen.overview_poster_height) + getResources().getDimensionPixelSize(R.dimen.text_margin)) / movieOverview.getLineHeight(),
                        getResources().getDimensionPixelSize(R.dimen.overview_poster_width) + getResources().getDimensionPixelSize(R.dimen.text_margin)),
                0,
                spannableString.length(),
                0);

        movieOverview.setText(spannableString);

        PopularMoviesApp.dataManager().loadPoster(movie.getPosterPath(), moviePoster, new Callback() {
            @Override
            public void onSuccess() {
                moviePoster.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                movieOverview.getViewTreeObserver().removeOnPreDrawListener(this);
                                getActivity().supportStartPostponedEnterTransition();
                                return true;
                            }
                        });
            }

            @Override
            public void onError() {

            }
        });

        ratingBar.setRating(movie.getVoteAverage().floatValue() / 2.0f);
        ratingText.setText(movie.getVoteAverage() + " / 10 (" + movie.getVoteCount() + ")");

        return rootView;
    }
}
