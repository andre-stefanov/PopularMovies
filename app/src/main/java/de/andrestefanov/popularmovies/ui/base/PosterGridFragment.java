package de.andrestefanov.popularmovies.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;
import de.andrestefanov.popularmovies.ui.main.MoviesView;

public abstract class PosterGridFragment<V extends MvpLceView<List<Movie>>, P extends MvpPresenter<V>>
        extends MvpLceViewStateFragment<RecyclerView, List<Movie>, V, P>
        implements OnMovieSelectedListener, MoviesView {

    private static final String TAG = "PosterGridFragment";

    PosterGridAdapter adapter;

    @BindView(R.id.contentView)
    RecyclerView recyclerView;

    Unbinder unbinder;

    public PosterGridFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public LceViewState<List<Movie>, V> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Movie> getData() {
        return adapter.getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.adapter = createAdapter();
        this.adapter.setOnMovieSelectedListener(this);

        this.recyclerView.setAdapter(this.adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_columns));
        this.recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (adapter.getData() == null || adapter.getData().isEmpty()) {
            loadData(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        unbinder.unbind();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(List<Movie> data) {
        this.adapter.setData(data);
    }

    protected PosterGridAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        try {
            ((OnMovieSelectedListener)getActivity()).onMovieSelected(movie);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity has to implement OnMovieSelectedListener");
        }
    }

    @Override
    public void addData(List<Movie> movies) {
        this.adapter.addData(movies);
    }

    @Override
    public void clear() {
        this.adapter.clear();
    }

    protected PosterGridAdapter createAdapter() {
        return new PosterGridAdapter();
    }
}
