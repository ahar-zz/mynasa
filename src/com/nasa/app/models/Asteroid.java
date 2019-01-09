package com.nasa.app.models;

import com.nasa.app.helper.JsonFormatterUtil;

import javax.json.JsonObject;

/**
 * Asteroid models.
 */
public class Asteroid {

    private double estimatedDiameter = 0;
    private double distanceToEarth = 0;
    private JsonObject asteroidJsonObject;

    public Asteroid(double estimatedDiameter, double distanceToEarth, JsonObject asteroidJsonObject) {
        this.estimatedDiameter = estimatedDiameter;
        this.distanceToEarth = distanceToEarth;
        this.asteroidJsonObject = asteroidJsonObject;
    }

    public double getEstimatedDiameter() {
        return estimatedDiameter;
    }

    public double getDistanceToEarth() {
        return distanceToEarth;
    }

    @Override
    public String toString() {
        return JsonFormatterUtil.getInstance().format(asteroidJsonObject);
    }
}
