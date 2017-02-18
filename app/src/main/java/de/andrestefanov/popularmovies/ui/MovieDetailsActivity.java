package de.andrestefanov.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.network.model.Video;
import de.andrestefanov.popularmovies.ui.details.MovieBackdropMvpView;
import de.andrestefanov.popularmovies.ui.details.MovieBackdropPresenter;
import de.andrestefanov.popularmovies.ui.details.MovieOverviewMvpView;
import de.andrestefanov.popularmovies.ui.details.MovieOverviewPresenter;
import de.andrestefanov.popularmovies.ui.details.MovieVideosMvpView;
import de.andrestefanov.popularmovies.ui.details.VideosPresenter;

public class MovieDetailsActivity extends AppCompatActivity implements MovieBackdropMvpView {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieDetailsActivity";

    public static final String ARG_MOVIE = "movie";

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.backdrop_imageview)
    ImageView backdropImageView;

    @BindView(R.id.backdrop_progress)
    ContentLoadingProgressBar progressBackdrop;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Movie movie = extras.getParcelable(ARG_MOVIE);

            viewPager.setAdapter(new MovieDetailsPagerAdapter(getSupportFragmentManager(), this, movie));

            if (movie != null) {
                MovieBackdropPresenter<MovieBackdropMvpView> backdropPresenter = new MovieBackdropPresenter<>(movie.getBackdropPath());
                backdropPresenter.onAttach(this);

                toolbar.setTitle(movie.getTitle());
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public ImageView getBackdropImageView() {
        return backdropImageView;
    }

    @Override
    public void showLoading() {
        progressBackdrop.show();
    }

    @Override
    public void hideLoading() {
        progressBackdrop.hide();
    }

    public static class MovieDetailsPagerAdapter extends FragmentPagerAdapter {

        static final String[] TAB_TITLES = new String[]{
                "Overview",
                "Videos"
        };

        private final Context context;

        private final Movie movie;

        MovieDetailsPagerAdapter(FragmentManager fm, Context context, Movie movie) {
            super(fm);
            this.movie = movie;
            this.context = context;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return MovieOverviewFragment.createInstance(new MovieOverviewPresenter<>(movie));
                case 1:
                    return MovieVideosFragment.createInstance(new VideosPresenter<>(movie.getId()));
            }
            return null;
        }

        @Override
        public int getCount() {
            return TAB_TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return context.getString(R.string.movie_overview);
                case 1:
                    return context.getString(R.string.movie_videos);
                default:
                    return "NO TITLE";
            }
        }
    }

    public static class MovieOverviewFragment extends Fragment implements MovieOverviewMvpView {

        @BindView(R.id.imageview_movie_poster)
        ImageView moviePoster;

        @BindView(R.id.textview_movie_overview)
        TextView movieOverview;

        @BindView(R.id.textview_movie_release)
        TextView movieReleaseDate;

        @BindView(R.id.textview_votes)
        TextView movieVotes;

        MovieOverviewPresenter<MovieOverviewMvpView> presenter;

        public static MovieOverviewFragment createInstance(MovieOverviewPresenter<MovieOverviewMvpView> presenter) {
            MovieOverviewFragment fragment = new MovieOverviewFragment();
            fragment.presenter = presenter;
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
            ButterKnife.bind(this, rootView);

            presenter.onAttach(this);

            return rootView;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

        }

        @Override
        public ImageView getPosterImageView() {
            return moviePoster;
        }

        @Override
        public void showMovie(Movie movie) {
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
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {
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
    }

    public static class MovieVideosFragment extends Fragment implements MovieVideosMvpView {

        @BindView(R.id.videos_container)
        LinearLayout videosContainer;

        private VideosPresenter<MovieVideosMvpView> presenter;

        public static MovieVideosFragment createInstance(VideosPresenter<MovieVideosMvpView> presenter) {
            MovieVideosFragment fragment = new MovieVideosFragment();
            fragment.presenter = presenter;
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_videos, container, false);
            ButterKnife.bind(this, rootView);

            presenter.onAttach(this);

            return rootView;
        }

        @Override
        public void showVideos(List<Video> videos) {
            for (Video video : videos) {
                View videoView = getActivity().getLayoutInflater().inflate(R.layout.view_movie_video, videosContainer, false);
                ((TextView) videoView.findViewById(R.id.video_title)).setText(video.getName());
                presenter.loadVideoImage(video.getKey(), (ImageView) videoView.findViewById(R.id.video_thumbnail));
                videoView.setTag("http://www.youtube.com/watch?v=" + video.getKey());

                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag())));
                    }
                });

                videosContainer.addView(videoView);
            }
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }
    }
}
