package de.andrestefanov.popularmovies.ui.base;

import android.support.v7.widget.RecyclerView;

import de.andrestefanov.popularmovies.utils.Constants;

public class EndlessMoviesAdapter extends PosterGridAdapter {

    private boolean loading = false;

    private OnMoreDataRequestListener dataRequestListener;

    public EndlessMoviesAdapter(OnMoreDataRequestListener moreDataRequestListener) {
        this.dataRequestListener = moreDataRequestListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (!loading && position > getData().size() - Constants.TMDB_API_MOVIES_PER_PAGE) {
            loading = true;
            dataRequestListener.onRequestMoreData();
        }
    }

    @Override
    protected void onDataChanged() {
        loading = false;
    }

    public interface OnMoreDataRequestListener {

        void onRequestMoreData();

    }
}
