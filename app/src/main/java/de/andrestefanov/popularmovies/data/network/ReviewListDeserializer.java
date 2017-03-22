package de.andrestefanov.popularmovies.data.network;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.andrestefanov.popularmovies.data.network.model.Review;

class ReviewListDeserializer implements JsonDeserializer<List<Review>> {

    private Gson gson = new Gson();

    @Override
    public List<Review> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return gson.fromJson(json.getAsJsonObject().get("results").getAsJsonArray(), new TypeToken<ArrayList<Review>>(){}.getType());
    }
}
