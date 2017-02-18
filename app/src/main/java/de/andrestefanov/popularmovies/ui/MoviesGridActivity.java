package de.andrestefanov.popularmovies.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.main.MoviesMvpPresenter;
import de.andrestefanov.popularmovies.ui.main.MoviesMvpView;
import de.andrestefanov.popularmovies.ui.main.MoviesPresenter;
import de.andrestefanov.popularmovies.utils.DefaultAnimationListener;

public class MoviesGridActivity extends AppCompatActivity implements MoviesMvpView, MoviesAdapter.OnMovieClickListener, MoviesAdapter.OnMoreDataRequestListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MoviesGridActivity";

    MoviesAdapter adapter;

    @BindView(R.id.recycler_view_movies)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionMenu fabMenu;

    private MoviesMvpPresenter<MoviesMvpView> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        ButterKnife.bind(this);

        presenter = new MoviesPresenter<>();

        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MoviesAdapter(this, this);
        this.recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        this.fabMenu.setClosedOnTouchOutside(true);
        this.fabMenu.setIconAnimated(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fabMenu.showMenuButton(true);

        presenter.onAttach(this);
    }

    private void initFab(MoviesFilter filter) {
        switch (filter) {
            case POPULAR:
                fabMenu.setMenuButtonColorNormalResId(R.color.colorPopularFab);
                fabMenu.setMenuButtonColorPressedResId(R.color.colorPopularFab);
                fabMenu.getMenuIconView().setImageResource(R.drawable.ic_whatshot);
                fabMenu.close(true);
                break;
            case TOP_RATED:
                fabMenu.setMenuButtonColorNormalResId(R.color.colorTopRatedFab);
                fabMenu.setMenuButtonColorPressedResId(R.color.colorTopRatedFab);
                fabMenu.getMenuIconView().setImageResource(R.drawable.ic_star_filled);
                fabMenu.close(true);
                break;
        }
    }

    public void showMostPopular(View view) {
        presenter.onFilterChange(MoviesFilter.POPULAR);
    }

    public void showTopRated(View view) {
        presenter.onFilterChange(MoviesFilter.TOP_RATED);
    }

    @Override
    public void onBackPressed() {
        if (this.fabMenu.isOpened())
            this.fabMenu.close(true);
        else
            super.onBackPressed();
    }

    @Override
    public void onClick(ImageView imageView, Movie movie) {
        final Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.ARG_MOVIE, movie);

        ArrayList<Pair> pList = new ArrayList<>();
        pList.add(Pair.create((View) imageView, "poster"));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            View decor = getWindow().getDecorView();
            View statusBar = decor.findViewById(android.R.id.statusBarBackground);
            View navBar = decor.findViewById(android.R.id.navigationBarBackground);

            if (statusBar != null) {
                pList.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
            }

            if (navBar != null) {
                pList.add(Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
            }
        }

        Pair[] pairs = new Pair[pList.size()];
        pList.toArray(pairs);

        @SuppressWarnings("unchecked")
        final ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MoviesGridActivity.this,
                pairs);

        Animation animation = AnimationUtils.loadAnimation(MoviesGridActivity.this, com.github.clans.fab.R.anim.fab_scale_down);
        animation.setDuration(50);
        animation.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                startActivity(intent, options.toBundle());
            }
        });
        fabMenu.setMenuButtonHideAnimation(animation);
        fabMenu.hideMenuButton(true);
    }

    @Override
    public void showMoreData(List<Movie> movies) {
        adapter.addAll(movies);
    }

    @Override
    public void showFilter(MoviesFilter filter) {
        initFab(filter);
    }

    @Override
    public void clear() {
        adapter.clear();
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading() called");
    }

    @Override
    public void onRequestMoreData() {
        presenter.requestMoreData();
    }
}
