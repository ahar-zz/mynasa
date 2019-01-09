package com.nasa.app.helper;

public class UrlFormatterUtil {
    private final static String NASA_URL =
            "https://api.nasa.gov/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=DEMO_KEY";

    private final static UrlFormatterUtil instance = new UrlFormatterUtil();

    public UrlFormatterUtil() {
    }

    public static UrlFormatterUtil getInstance() {
        return instance;
    }

    /**
     * Method to format http query string.
     *
     * @param date - date in string format which represents date for request
     */
    public String createURLString(String date) {
        return String.format(NASA_URL,
                date, date);
    }
}
