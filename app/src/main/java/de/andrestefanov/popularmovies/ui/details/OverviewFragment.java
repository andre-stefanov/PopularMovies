package de.andrestefanov.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.MovieDetails;

public class OverviewFragment extends Fragment {

    private static final String TAG = "OverviewFragment";

    private MovieDetails movie;

    @BindView(R.id.imageview_movie_poster)
    ImageView moviePoster;

    @BindView(R.id.textview_movie_synopsis)
    TextView movieOverview;

    @BindView(R.id.textview_movie_release)
    TextView movieReleaseDate;

    @BindView(R.id.textview_votes)
    TextView movieVotes;

    public static OverviewFragment createInstance(MovieDetails movie) {
        OverviewFragment fragment = new OverviewFragment();
        fragment.movie = movie;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
        ButterKnife.bind(this, rootView);

        movieOverview.setText(movie.getOverview());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat localFormat = android.text.format.DateFormat.getLongDateFormat(getContext());
        Date releaseDate = null;
        try {
            releaseDate = dateFormat.parse(movie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        movieReleaseDate.setText(localFormat.format(releaseDate));

        movieVotes.setText(String.format(Locale.ENGLISH, "%s/10 (%s)", movie.getVoteAverage(), movie.getVoteCount()));

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

        return rootView;
    }
}
