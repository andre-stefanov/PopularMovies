package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.example.android.popularmovies.data.TMDBClient;
import com.example.android.popularmovies.model.Movie;
import com.github.clans.fab.FloatingActionMenu;

import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_POPULAR;
import static com.example.android.popularmovies.Preferences.MOVIES_FILTER_TOP_RATED;

public class MoviesGridActivity extends AppCompatActivity {

    private static final String TAG = "MoviesGridActivity";

    private SharedPreferences sharedPreferences;

    private FloatingActionMenu fabMenu;

    private MoviesGridAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        TMDBClient tmdbClient = new TMDBClient(this);

        this.sharedPreferences = getSharedPreferences(Preferences.PREFS_FILE_NAME, MODE_PRIVATE);

        RecyclerView mMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(mMoviesAdapter.getItemViewType(position)){
                    case MoviesGridAdapter.VIEW_TYPE_MOVIE:
                        return 1;
                    case MoviesGridAdapter.VIEW_TYPE_PROGRESS:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
        mMoviesRecyclerView.setLayoutManager(mLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        this.mMoviesAdapter = new MoviesGridAdapter(this, tmdbClient, new View.OnClickListener() {
            @Override
            public void onClick(View posterView) {
                Movie movie = (Movie) posterView.getTag();

                Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.MOVIE_PARCELABLE_EXTRA, movie);

                View decor = getWindow().getDecorView();
                View statusBar = decor.findViewById(android.R.id.statusBarBackground);
                View navBar = decor.findViewById(android.R.id.navigationBarBackground);

                Pair<View, String> p1 = Pair.create(posterView, "poster");
                Pair<View, String> p2 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                Pair<View, String> p3 = Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MoviesGridActivity.this,
                        p1, p2, p3);

                fabMenu.hideMenuButton(true);

                startActivity(intent, options.toBundle());
            }
        });
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        this.fabMenu = (FloatingActionMenu) findViewById(R.id.fab);
        this.fabMenu.setClosedOnTouchOutside(true);
        this.fabMenu.setIconAnimated(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fabMenu.showMenuButton(true);
    }

    public void showMostPopular(View view) {
        fabMenu.setMenuButtonColorNormalResId(R.color.colorPopularFab);
        fabMenu.setMenuButtonColorPressedResId(R.color.colorPopularFab);
        fabMenu.getMenuIconView().setImageResource(R.drawable.ic_whatshot);
        fabMenu.close(true);

        setCurrentFilter(MOVIES_FILTER_POPULAR);
    }

    public void showTopRated(View view) {
        fabMenu.setMenuButtonColorNormalResId(R.color.colorTopRatedFab);
        fabMenu.setMenuButtonColorPressedResId(R.color.colorTopRatedFab);
        fabMenu.getMenuIconView().setImageResource(R.drawable.ic_star_filled);
        fabMenu.close(true);

        setCurrentFilter(MOVIES_FILTER_TOP_RATED);
    }

    private void setCurrentFilter(String filter) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Preferences.KEY_MOVIES_FILTER_KEY, filter);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (this.fabMenu.isOpened())
            this.fabMenu.close(true);
        else
            super.onBackPressed();
    }
}
