package com.nasa.app.helper;

import com.nasa.app.constants.AppConstants;

import javax.json.JsonArray;
import javax.json.JsonObject;

/*
 *
 * Parser for diameter and distance from JsonObject
 */
public class AsteroidParseHelper {

    public double getDiameter(JsonObject obj) {
        return (getDiameter(obj, AppConstants.MAX_ESTIMATED_DIAMETER) +
                getDiameter(obj, AppConstants.MIN_ESTIMATED_DIAMETER)) / 2;
    }

    public double getDistance(JsonObject obj) {
        double distance = 0d;
        JsonArray distanceArray = obj.getJsonArray(AppConstants.CLOSE_APPROACH_DATA);

        if (distanceArray.size() > 0) {
            String distanceStr = distanceArray
                    .getJsonObject(0)
                    .getJsonObject(AppConstants.MISS_DISTANCE)
                    .getString(AppConstants.ASTRONOMICAL);
            distance = Double.parseDouble(distanceStr);
        }

        return distance;
    }


    private double getDiameter(JsonObject obj, String key) {
        return obj
                .getJsonObject(AppConstants.ESTIMATED_DIAMETER)
                .getJsonObject(AppConstants.KILOMETERS)
                .getJsonNumber(key)
                .doubleValue();
    }
}
