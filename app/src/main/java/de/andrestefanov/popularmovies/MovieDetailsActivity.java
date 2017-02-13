package de.andrestefanov.popularmovies;

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
import android.support.v4.widget.NestedScrollView;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.data.TMDBClient;
import de.andrestefanov.popularmovies.data.YouTubeClient;
import de.andrestefanov.popularmovies.model.Movie;
import de.andrestefanov.popularmovies.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

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
        tabLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TMDBClient tmdbClient = new TMDBClient(this);

//        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Movie movie = extras.getParcelable(ARG_MOVIE);


            viewPager.setAdapter(new MovieDetailsPagerAdapter(getSupportFragmentManager(), this, movie));

            if (movie != null) {
                toolbar.setTitle(movie.getTitle());

                tmdbClient.loadBackdrop(movie.getBackdropPath(), backdropImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBackdrop.hide();
                    }

                    @Override
                    public void onError() {

                    }
                });

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

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            Fragment fragment = null;

            switch (i) {
                case 0:
                    fragment = new MovieOverviewFragment();
                    break;
                case 1:
                    fragment = new MovieVideosFragment();
                    break;
            }

            if (fragment != null) {
                Bundle args = new Bundle();
                args.putParcelable(ARG_MOVIE, movie);
                fragment.setArguments(args);
            }

            return fragment;
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

    public static class MovieOverviewFragment extends Fragment {

        @BindView(R.id.imageview_movie_poster)
        ImageView moviePoster;

        @BindView(R.id.textview_movie_overview)
        TextView movieOverview;

        @BindView(R.id.textview_movie_release)
        TextView movieReleaseDate;

        @BindView(R.id.textview_votes)
        TextView movieVotes;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_movie_overview, container, false);
            ButterKnife.bind(this, rootView);

            Movie movie = getArguments().getParcelable(ARG_MOVIE);

            if (movie != null) {
                movieOverview.setText(movie.getOverview());

                TMDBClient tmdbClient = new TMDBClient(getContext());
                tmdbClient.loadPoster(movie.getPosterPath(), moviePoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

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

            return rootView;
        }
    }

    public static class MovieVideosFragment extends Fragment {

        TMDBClient tmdbClient;
        YouTubeClient youTubeClient;

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            tmdbClient = new TMDBClient(getContext());
            youTubeClient = new YouTubeClient(getContext());

            final NestedScrollView rootView = (NestedScrollView) inflater.inflate(R.layout.fragment_movie_videos, container, false);
            final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.videos_container);

            Movie movie = getArguments().getParcelable(ARG_MOVIE);

            if (movie != null) {
                tmdbClient.loadMovieVideos(movie.getId(), new Callback<Video.Page>() {
                    @Override
                    public void onResponse(Call<Video.Page> call, Response<Video.Page> response) {
                        for (Video video : response.body().getResults()) {
                            View videoView = inflater.inflate(R.layout.view_movie_video, container, false);
                            TextView tv = (TextView) videoView.findViewById(R.id.video_title);
                            tv.setText(video.getName());
                            ImageView iv = (ImageView) videoView.findViewById(R.id.video_thumbnail);
                            youTubeClient.loadVideoImage(video.getKey(), iv, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                            videoView.setTag("http://www.youtube.com/watch?v=" + video.getKey());
                            videoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag())));
                                }
                            });
                            linearLayout.addView(videoView);
                        }
                    }

                    @Override
                    public void onFailure(Call<Video.Page> call, Throwable t) {

                    }
                });
            }

            return rootView;
        }

    }
}
