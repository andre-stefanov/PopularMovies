package de.andrestefanov.popularmovies.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionMenu;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.PopularMoviesApp;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.data.prefs.MoviesFilter;
import de.andrestefanov.popularmovies.ui.details.MovieDetailsActivity;
import de.andrestefanov.popularmovies.utils.DefaultAnimationListener;

public class MoviesGridActivity extends MvpLceActivity<RecyclerView, List<Movie>, MoviesView, MoviesPresenter> implements MoviesView, MoviesAdapter.OnMovieClickListener, MoviesAdapter.OnMoreDataRequestListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MoviesGridActivity";

    MoviesAdapter adapter;

    @BindView(R.id.contentView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        ButterKnife.bind(this);

        this.recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        this.recyclerView.setLayoutManager(layoutManager);

        this.adapter = new MoviesAdapter(this, this);
        this.recyclerView.setAdapter(adapter);

        this.adapter.setClickListener(this);

        this.fabMenu.setClosedOnTouchOutside(true);
        this.fabMenu.setIconAnimated(false);

        initFab(PopularMoviesApp.dataManager().getMovieFilter());

        loadData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fabMenu.showMenuButton(true);
    }

    @NonNull
    @Override
    public MoviesPresenter createPresenter() {
        return new MoviesPresenter();
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
        presenter.setFilter(MoviesFilter.POPULAR);
        initFab(MoviesFilter.POPULAR);
    }

    public void showTopRated(View view) {
        presenter.setFilter(MoviesFilter.TOP_RATED);
        initFab(MoviesFilter.TOP_RATED);
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
    public void addData(List<Movie> movies) {
        adapter.addData(movies);
    }

    @Override
    public void clear() {
        adapter.clear();
    }

    @Override
    public void onRequestMoreData() {
        presenter.loadMoreMovies();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
    }

    @Override
    public void showContent() {
        super.showContent();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
    }

    @Override
    public void setData(List<Movie> data) {
        adapter.setData(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadMoreMovies();
    }

}
