package com.nasa.app.services;

import com.nasa.app.constants.AppConstants;
import com.nasa.app.exceptions.AsteroidParsingException;
import com.nasa.app.factory.AsteroidFactory;
import com.nasa.app.models.Asteroid;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import javax.json.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AsteroidService {
    private JsonObject jsonObject;
    private String formattedToday;
    private int elementCount;
    private List<Asteroid> asteroidList = new ArrayList<>();

    private final static AsteroidFactory asteroidFactory = new AsteroidFactory();

    public void load(byte[] content, String date) {
        this.formattedToday = date;

        jsonObject = null;
        // Read response from stream to json object.
        try (JsonReader jsonReader = Json.createReader(new ByteInputStream(content, content.length))) {
            jsonObject = jsonReader.readObject();
        } catch (JsonException e) {
            throw e;
        }

        try {
            parseJsonObject();
        } catch (AsteroidParsingException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonObject() throws AsteroidParsingException {
        this.elementCount = jsonObject.getInt(AppConstants.ELEMENT_COUNT, 0);
        JsonArray asteroidsJsonArray = jsonObject.getJsonObject(
                AppConstants.NEAR_EARTH_OBJECTS).getJsonArray(formattedToday);
        if (asteroidsJsonArray.size() < 1) {
            throw new AsteroidParsingException(String.format(
                    "Unknown error, read 0 Asteroid objects, while expected %d\n",
                    elementCount));
        }


        asteroidsJsonArray.forEach(jsonValue -> {
            Asteroid currAsteroid = asteroidFactory.create(jsonValue.asJsonObject());
            if (currAsteroid == null) {
                return;
            }

            asteroidList.add(currAsteroid);
        });

    }

    public int getElementCount() {
        return elementCount;
    }

    public Asteroid getBiggest() {
        asteroidList.sort(Comparator.comparing(Asteroid::getEstimatedDiameter).reversed());

        return asteroidList.get(0);
    }

    public Asteroid getClosest() {
        asteroidList.sort(Comparator.comparing(Asteroid::getEstimatedDiameter).reversed());

        return asteroidList.get(0);
    }
}
