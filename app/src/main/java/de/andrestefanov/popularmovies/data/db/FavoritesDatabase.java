package de.andrestefanov.popularmovies.data.db;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = FavoritesDatabase.VERSION)
public final class FavoritesDatabase {

    public static final int VERSION = 1;

    @Table(FavoriteColumns.class)
    public static final String FAVORITES = "favorites";

}
