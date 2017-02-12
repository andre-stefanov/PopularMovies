package de.andrestefanov.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Callback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.data.TMDBClient;
import de.andrestefanov.popularmovies.data.YouTubeClient;
import de.andrestefanov.popularmovies.model.Movie;
import de.andrestefanov.popularmovies.model.Video;
import de.andrestefanov.popularmovies.utils.NestedScrollViewBehavior;
import retrofit2.Call;
import retrofit2.Response;

import static android.view.View.GONE;

public class MovieDetailsActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieDetailsActivity";

    public static final String MOVIE_PARCELABLE_EXTRA = "movie";

    @BindView(R.id.main_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.linearlayout_content_container)
    LinearLayout container;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_backdrop)
    ImageView backdropImageView;

    @BindView(R.id.progress)
    ContentLoadingProgressBar progressBar;

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.imageview_poster)
    ImageView posterImageView;

    @BindView(R.id.textview_overview)
    TextView overviewTextView;

    @BindView(R.id.textview_release)
    TextView releaseTextView;

    @BindView(R.id.textview_votes)
    TextView votesTextView;

    @BindView(R.id.imageview_video_thumbnail)
    ImageView videoThumbnail;

    @BindView(R.id.progress_video_thumbnail)
    ContentLoadingProgressBar videoThumbnailProgress;

    @BindView(R.id.video_thumbnail_container)
    FrameLayout videoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(new NestedScrollViewBehavior());

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TMDBClient tmdbClient = new TMDBClient(this);

        final YouTubeClient youTubeClient = new YouTubeClient(this);

        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final Movie movie = extras.getParcelable(MOVIE_PARCELABLE_EXTRA);

            if (movie != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                DateFormat localFormat = android.text.format.DateFormat.getLongDateFormat(this);
                Date releaseDate = null;
                try {
                    releaseDate = dateFormat.parse(movie.getReleaseDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate);
                String localizedReleaseDate = localFormat.format(releaseDate);

                titleTextView.setText(String.format(Locale.ENGLISH, "%s (%s)", movie.getTitle(), year));
                overviewTextView.setText(movie.getOverview());
                releaseTextView.setText(localizedReleaseDate);
                votesTextView.setText(String.format(Locale.ENGLISH, "%s/10 (%s)", movie.getVoteAverage(), movie.getVoteCount()));

                tmdbClient.loadBackdrop(movie.getBackdropPath(), backdropImageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        progressBar.hide();
                    }

                    @Override
                    public void onError() {

                    }
                });

                tmdbClient.loadPoster(movie.getPosterPath(), posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        scheduleStartPostponedTransition(posterImageView);
                    }

                    @Override
                    public void onError() {

                    }
                });

                tmdbClient.loadMovieVideos(movie.getId(), new retrofit2.Callback<Video.Page>() {
                    @Override
                    public void onResponse(Call<Video.Page> call, Response<Video.Page> response) {
                        if (response.body().getResults().size() > 0) {
                            final Video video = response.body().getResults().get(0);
                            youTubeClient.loadVideoImage(video.getKey(), videoThumbnail, new Callback() {
                                @Override
                                public void onSuccess() {
                                    videoThumbnailProgress.hide();
                                }

                                @Override
                                public void onError() {
                                    Log.d(TAG, "onError() called");
                                    videoContainer.setVisibility(GONE);
                                }
                            });
                            videoThumbnail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
                                }
                            });
                        }

//                        for (Video video : response.body().getResults()) {
//                            View v = inflater.inflate(R.layout.view_movie_video, container, false);
//                            TextView title = (TextView) v.findViewById(R.id.video_title);
//                            title.setText(video.getName());
//                            ImageView imageView = (ImageView) v.findViewById(R.id.video_thumbnail);
//                            youTubeClient.loadVideoImage(video.getKey(), imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                    Log.d(TAG, "onSuccess() called");
//                                }
//
//                                @Override
//                                public void onError() {
//                                    Log.d(TAG, "onError() called");
//                                }
//                            });
//                            container.addView(v);
//                        }
                    }

                    @Override
                    public void onFailure(Call<Video.Page> call, Throwable t) {

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
}
