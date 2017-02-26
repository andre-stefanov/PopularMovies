package de.andrestefanov.popularmovies.data.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;

@ContentProvider(authority = FavoritesProvider.AUTHORITY, database = FavoritesDatabase.class)
public final class FavoritesProvider {

    public static final String AUTHORITY = "de.andrestefanov.popularmovies.data.db.FavoritesProvider";

    public static class Favorites {

        @ContentUri(
                path = "favorites",
                type = "vnd.android.cursor.dir/list")
        public static final Uri FAVORITES = Uri.parse("content://" + AUTHORITY + "/favorites");

    }

}
