package de.andrestefanov.popularmovies.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;

public class MovieGridFragment extends MvpLceFragment<RecyclerView, List<Movie>, MoviesView, MoviesPresenter> implements MoviesView, MoviesAdapter.OnMovieClickListener, MoviesAdapter.OnMoreDataRequestListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieGridFragment";

    MoviesAdapter adapter;

    @BindView(R.id.contentView)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(adapter);

        this.recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_columns));
        this.recyclerView.setLayoutManager(layoutManager);

        if (adapter.getItemCount() <= 0)
            loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public MoviesPresenter createPresenter() {
        return new MoviesPresenter();
    }

    public void onFilterChanged() {
        clear();
        loadData(true);
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
    public void setData(List<Movie> data) {
        adapter.setData(data);
    }

    @Override
    public void loadData(boolean refresh) {
        getPresenter().loadMovies(refresh);
    }

    @Override
    public void onRequestMoreData() {
        loadData(false);
    }

    @Override
    public void onClick(ImageView imageView, Movie movie) {
        if (getActivity() instanceof OnMovieClickListener)
            ((OnMovieClickListener)getActivity()).onMovieClick(imageView, movie);
        else
            throw new IllegalStateException("Activity has to implement OnMovieClickListener");
    }

    public interface OnMovieClickListener {

        void onMovieClick(ImageView posterView, Movie movie);

    }
}
