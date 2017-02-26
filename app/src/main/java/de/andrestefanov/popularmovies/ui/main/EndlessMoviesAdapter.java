package de.andrestefanov.popularmovies.ui.main;

import android.support.v7.widget.RecyclerView;

import de.andrestefanov.popularmovies.ui.base.PosterGridAdapter;
import de.andrestefanov.popularmovies.utils.Constants;

class EndlessMoviesAdapter extends PosterGridAdapter {

    @SuppressWarnings("FieldCanBeLocal")
    private static int THRESHOLD = Constants.TMDB_API_MOVIES_PER_PAGE;

    private boolean loading = false;

    private OnMoreDataRequestListener dataRequestListener;

    EndlessMoviesAdapter(OnMoreDataRequestListener moreDataRequestListener) {
        this.dataRequestListener = moreDataRequestListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (!loading && position > getData().size() - THRESHOLD) {
            loading = true;
            dataRequestListener.onRequestMoreData();
        }
    }

    @Override
    protected void onDataChanged() {
        loading = false;
    }

    interface OnMoreDataRequestListener {

        void onRequestMoreData();

    }
}
