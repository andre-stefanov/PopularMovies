package com.example.android.popularmovies;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Callback;

import java.text.ParseException;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";

    public static final String MOVIE_PARCELABLE_EXTRA = "movie";

    private TMDBClient tmdbClient;

    private ImageView backdropImageView;
    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView overviewTextView;
    private TextView yearTextView;
    private TextView votesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        final Fab fab = (Fab) findViewById(R.id.fab);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        backdropImageView = (ImageView) findViewById(R.id.main_backdrop);
        titleTextView = (TextView) findViewById(R.id.textview_title);
        posterImageView = (ImageView) findViewById(R.id.imageview_poster);
        overviewTextView = (TextView) findViewById(R.id.textview_overview);
        yearTextView = (TextView) findViewById(R.id.textview_release);
        votesTextView = (TextView) findViewById(R.id.textview_votes);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged(): " + verticalOffset);
                if (appBarLayout.getHeight() / 2 < -verticalOffset) {
                    Log.d(TAG, "onOffsetChanged: hide");
                    fab.hide();
                } else if (verticalOffset != 0) {
                    fab.show(0,0);
                    Log.d(TAG, "onOffsetChanged: show");
                }
            }
        });

        tmdbClient = new TMDBClient(this);

        postponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Movie movie = extras.getParcelable(MOVIE_PARCELABLE_EXTRA);

            if (movie != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(dateFormat.parse(movie.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                titleTextView.setText(String.format(Locale.ENGLISH, "%s (%s)", movie.getTitle(), String.valueOf(calendar.get(Calendar.YEAR))));
                overviewTextView.setText(movie.getOverview());
                yearTextView.setText(movie.getReleaseDate());
                votesTextView.setText(String.format(Locale.ENGLISH, "%s/10 (%s)", movie.getVoteAverage(), movie.getVoteCount()));

                tmdbClient.loadBackdrop(movie.getBackdropPath(), backdropImageView);
                tmdbClient.loadPoster(movie.getPosterPath(), posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        scheduleStartPostponedTransition(posterImageView);
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
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
