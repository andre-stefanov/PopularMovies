package de.andrestefanov.popularmovies.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.andrestefanov.popularmovies.R;
import de.andrestefanov.popularmovies.data.network.model.Movie;

public abstract class PosterGridFragment<V extends MvpLceView<List<Movie>>, P extends MvpPresenter<V>> extends MvpLceFragment<RecyclerView, List<Movie>, V, P> {

    private PosterGridAdapter adapter;

    @BindView(R.id.contentView)
    RecyclerView recyclerView;

    public PosterGridFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);
        ButterKnife.bind(this, rootView);

        this.adapter = new PosterGridAdapter();
        this.recyclerView.setAdapter(this.adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData(false);
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

    public void setOnPosterClickListener(PosterGridAdapter.OnPosterClickListener clickListener) {
        this.adapter.setOnPosterClickListener(clickListener);
    }
}
