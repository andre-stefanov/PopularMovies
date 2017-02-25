package de.andrestefanov.popularmovies.data.db;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.*;

public interface FavoriteColumns {

    @DataType(INTEGER)
    @PrimaryKey
    String _ID = "_id";

}
