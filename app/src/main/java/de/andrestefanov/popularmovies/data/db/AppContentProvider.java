package de.andrestefanov.popularmovies.data.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@net.simonvt.schematic.annotation.ContentProvider(authority = AppContentProvider.AUTHORITY, database = FavoritesDatabase.class)
public final class AppContentProvider {

    public static final String AUTHORITY = "de.andrestefanov.popularmovies.data.db.AppContentProvider";

    @TableEndpoint(table = FavoritesDatabase.FAVORITES)
    public static class Favorites {

        @ContentUri(
                path = "favorites",
                type = "vnd.android.cursor.dir/list")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + FavoritesDatabase.FAVORITES);

    }

}
