package com.nasa.app.factory;

import com.nasa.app.helper.AsteroidParseHelper;
import com.nasa.app.models.Asteroid;

import javax.json.JsonObject;

/*
 *
 * Builder to build Asteroid object
 *
 */
public class AsteroidFactory {
    private final AsteroidParseHelper asteroidParseHelper;

    public AsteroidFactory() {
        this.asteroidParseHelper = new AsteroidParseHelper();
    }

    public Asteroid create(JsonObject jsonObject) {
        return new Asteroid(asteroidParseHelper.getDiameter(jsonObject),
                asteroidParseHelper.getDistance(jsonObject), jsonObject);
    }
}
