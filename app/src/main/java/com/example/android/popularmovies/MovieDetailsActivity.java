package com.example.android.popularmovies;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.MovieDetails;

import java.text.ParseException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity implements Callback<MovieDetails> {

    private static final String TAG = "MovieDetailsActivity";

    public static final String MOVIE_ID_EXTRA = "movie_id";
    public static final String MOVIE_TITLE_EXTRA = "movie_title";

    private TMDBClient tmdbClient;

    private MovieDetails movieDetails;

    private Toolbar toolbar;
    private ImageView backdropImageView;
    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView overviewTextView;
    private TextView yearTextView;
    private TextView durationTextView;
    private TextView votesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backdropImageView = (ImageView) findViewById(R.id.main_backdrop);
        titleTextView = (TextView) findViewById(R.id.textview_title);
        posterImageView = (ImageView) findViewById(R.id.imageview_poster);
        overviewTextView = (TextView) findViewById(R.id.textview_overview);
        yearTextView = (TextView) findViewById(R.id.textview_release);
        durationTextView = (TextView) findViewById(R.id.textview_duration);
        votesTextView = (TextView) findViewById(R.id.textview_votes);

        tmdbClient = new TMDBClient(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int movieId = extras.getInt(MOVIE_ID_EXTRA);
            tmdbClient.loadMovieDetails(movieId, this);
        }
    }

    @Override
    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
        movieDetails = response.body();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormat.parse(movieDetails.getReleaseDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tmdbClient.loadBackdrop(movieDetails.getBackdropPath(), backdropImageView);
        tmdbClient.loadPoster(movieDetails.getPosterPath(), posterImageView);
        titleTextView.setText(String.format(Locale.ENGLISH, "%s (%s)", movieDetails.getTitle(), String.valueOf(calendar.get(Calendar.YEAR))));
        overviewTextView.setText(movieDetails.getOverview());
        yearTextView.setText(movieDetails.getReleaseDate());
        durationTextView.setText(String.format(Locale.ENGLISH, "%d Min", movieDetails.getRuntime()));
        votesTextView.setText(String.format(Locale.ENGLISH, "%s/10 (%s)", movieDetails.getVoteAverage(), movieDetails.getVoteCount()));
    }

    @Override
    public void onFailure(Call<MovieDetails> call, Throwable t) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
