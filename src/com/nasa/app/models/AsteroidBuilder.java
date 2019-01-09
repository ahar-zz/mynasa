package com.nasa.app.models;

import javax.json.JsonObject;

/*
 *
 * Builder to build Asteroid object
 *
 */
public class AsteroidBuilder {
    private Asteroid asteroid;

    public AsteroidBuilder() {
        this.asteroid = new Asteroid();
    }

    public AsteroidBuilder withJsonObject(JsonObject jsonObject) {
        this.asteroid.setAsteroidJsonObject(jsonObject);
        return this;
    }

    public AsteroidBuilder withDiameter(double diameter) {
        this.asteroid.setEstimatedDiameter(diameter);
        return this;
    }

    public AsteroidBuilder withDistance(double distance) {
        this.asteroid.setDistanceToEarth(distance);
        return this;
    }

    public Asteroid build() {
        return asteroid;
    }
}
