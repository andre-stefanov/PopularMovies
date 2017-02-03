package de.andrestefanov.popularmovies;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Callback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.data.TMDBClient;
import de.andrestefanov.popularmovies.model.Movie;
import de.andrestefanov.popularmovies.utils.NestedScrollViewBehavior;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailsActivity";

    public static final String MOVIE_PARCELABLE_EXTRA = "movie";

    private TMDBClient tmdbClient;

    @BindView(R.id.main_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.linearlayout_content_container)
    LinearLayout container;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_backdrop)
    ImageView backdropImageView;

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

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private boolean showFabPending = true;

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

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (appBarLayout.getHeight() / 2 < -verticalOffset) {
                    fab.hide(true);
                } else if (verticalOffset != 0) {
                    fab.show(true);
                }
            }
        });

        tmdbClient = new TMDBClient(this);

        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Movie movie = extras.getParcelable(MOVIE_PARCELABLE_EXTRA);

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

//        Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
//        sharedElementEnterTransition.addListener(new TransitionPort.TransitionListenerAdapter() {
//            @Override
//            public void onTransitionEnd(@NonNull Transition transition) {
//                if (showFabPending) {
//                    fab.show(true);
//                    showFabPending = false;
//                }
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fab.hide(true);
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
                        fab.show(true);
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        fab.hide(true);
        super.onBackPressed();
    }
}
