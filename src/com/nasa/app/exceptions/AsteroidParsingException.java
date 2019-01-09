package com.nasa.app.exceptions;

/**
 * Exception thrown when there is a problem deserialize jsonObject of Asteroid
 */
public class AsteroidParsingException extends Exception {

    public AsteroidParsingException(String message) {
        super(message);
    }

    public AsteroidParsingException(String message,
                                    Throwable cause) {
        super(message, cause);
    }
}
