package com.nasa.app;

import com.nasa.app.constants.AppConstants;
import com.nasa.app.models.Asteroid;
import com.nasa.app.models.AsteroidBuilder;
import com.nasa.app.exceptions.AsteroidParsingException;
import com.nasa.app.helper.AsteroidParseHelper;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import javax.json.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class NEOApp {

    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Method to format http query string.
     *
     * @param date - date in string format which represents date for request
     */
    private static String createURLString(String date) {
        return String.format("https://api.nasa.gov/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=DEMO_KEY",
                date, date);
    }

    /*
     *
     * Retrieves page content and transfer it to bytes for future work
     *
     * @param url - url object
     */
    private static byte[] getPageContent(URL url) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = url.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }

    /**
     * Method to print out url response.
     *
     * @param content - byte array to print.
     */
    private static void printURLResponce(byte[] content) {
        System.out.println("/***** URL Content *****\n");
        System.out.println(new String(content));
    }

    /**
     * Method to print out url response.
     *
     * @param jsonObject - JsonObject which represent Asteroid object
     */
    private static Asteroid createAsteroid(JsonObject jsonObject) {
        AsteroidBuilder builder = new AsteroidBuilder()
                .withJsonObject(jsonObject)
                .withDiameter(AsteroidParseHelper.getDiameter(jsonObject))
                .withDistance(AsteroidParseHelper.getDistance(jsonObject));

        return builder.build();
    }

    /**
     * Method to trust all certificates, to prevent error connected with URL connection to https.
     */
    private static void trustAllCerts() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }
    }


    /**
     * Main method for retrieving data and printing it out.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("\n\nFinished work.")));

            trustAllCerts();

            String formattedToday = format.format(Calendar.getInstance().getTime());

            JsonObject jsonObject;

            byte[] content = getPageContent(new URL(createURLString(formattedToday)));

            // Print the raw responce.
            printURLResponce(content);

            // Read response from stream to json object.
            try (JsonReader jsonReader = Json.createReader(new ByteInputStream(content, content.length))) {
                jsonObject = jsonReader.readObject();
            } catch (JsonException e) {
                throw e;
            }

            // Read the number of Ateroids close to the earth today
            int elementCount = jsonObject.getInt(AppConstants.ELEMENT_COUNT, 0);
            System.out.println(String.format("\nNumber of NEOs close to the Earth on %s is %d\n",
                    formattedToday, elementCount));

            if (elementCount == 0) {
                return;
            }

            JsonArray asteroidsJsonArray = jsonObject.getJsonObject(
                    AppConstants.NEAR_EARTH_OBJECTS).getJsonArray(formattedToday);

            if (asteroidsJsonArray.size() < 1) {
                throw new AsteroidParsingException(String.format(
                        "Unknown error, read 0 Asteroid objects, while expected %d\n",
                        elementCount));
            }

            Asteroid biggestNEO, closestNEO;

            List<Asteroid> asteroidList = new ArrayList<>();

            asteroidsJsonArray.forEach(jsonValue -> {
                Asteroid currAsteroid = createAsteroid(jsonValue.asJsonObject());
                if (currAsteroid == null) {
                    return;
                }

                asteroidList.add(currAsteroid);
            });

            asteroidList.sort(Comparator.comparing(Asteroid::getEstimatedDiameter).reversed());
            biggestNEO = asteroidList.get(0);

            asteroidList.sort(Comparator.comparing(Asteroid::getDistanceToEarth));
            closestNEO = asteroidList.get(0);


            System.out.println(String.format(
                    "\n*** The biggest Asteroid details *** %s", biggestNEO));
            System.out.println(String.format(
                    "\n*** The closest Asteroid details *** %s", closestNEO));

        } catch (JsonException | AsteroidParsingException e) {
            System.out.println(String.format("Error during reading JSON responce. Reason: %s", e.getMessage()));
            e.printStackTrace(System.err);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.out.println(String.format("The execution failed due to the following reason: %s", e.getMessage()));
            e.printStackTrace(System.err);
        }
    }
}