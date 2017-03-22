package de.andrestefanov.popularmovies.data.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import de.andrestefanov.popularmovies.data.network.model.Movie;

public class DbHelperImpl implements DbHelper {

    private ContentResolver contentResolver;

    public DbHelperImpl(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public Cursor getFavorites() {
        return contentResolver.query(AppContentProvider.Favorites.CONTENT_URI, null, null, null, null);
    }

    @Override
    public boolean isFavorite(Movie movie) {
        return count(AppContentProvider.Favorites.CONTENT_URI, "_id = ?", new String[]{String.valueOf(movie.getId())}) == 1;
    }

    @Override
    public boolean toggleFavorite(Movie movie) {
        if (!isFavorite(movie)) {
            ContentValues cv = new ContentValues();
            cv.put(FavoriteColumns._ID, movie.getId());
            cv.put(FavoriteColumns.POSTER, movie.getPosterPath());
            
            contentResolver.insert(AppContentProvider.Favorites.CONTENT_URI, cv);
            return true;
        } else {
            contentResolver.delete(AppContentProvider.Favorites.CONTENT_URI, "_id = ?", new String[]{String.valueOf(movie.getId())});
            return false;
        }
    }

    private int count(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = contentResolver.query(uri,new String[] {"count(*)"},
                selection, selectionArgs, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return 0;
        } else {
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
    }
}
