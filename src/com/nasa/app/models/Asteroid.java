package com.nasa.app.models;

import com.nasa.app.helper.JsonFormatterUtil;

import javax.json.JsonObject;

/**
 * Asteroid models.
 */
public class Asteroid {

    private double estimatedDiameter = 0;
    private double distanceToEarth = 0;
    transient JsonObject asteroidJsonObject;

    public Asteroid() {
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

    public void setAsteroidJsonObject(JsonObject asteroidJsonObject) {
        this.asteroidJsonObject = asteroidJsonObject;
    }

    public void setEstimatedDiameter(double estimatedDiameter) {
        this.estimatedDiameter = estimatedDiameter;
    }

    public void setDistanceToEarth(double distanceToEarth) {
        this.distanceToEarth = distanceToEarth;
    }
}
