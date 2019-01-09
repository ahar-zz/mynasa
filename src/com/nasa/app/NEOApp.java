package com.nasa.app;

import com.nasa.app.constants.AppConstants;
import com.nasa.app.factory.AsteroidFactory;
import com.nasa.app.helper.UrlFormatterUtil;
import com.nasa.app.models.Asteroid;
import com.nasa.app.exceptions.AsteroidParsingException;
import com.nasa.app.services.AsteroidService;
import com.nasa.app.services.NasaService;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import javax.json.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class NEOApp {
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final static NasaService nasaService = new NasaService();
    private final static AsteroidFactory asteroidFactory = new AsteroidFactory();
    private final static AsteroidService asteroidService = new AsteroidService();
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

            byte[] content = nasaService.getPageContent(
                    UrlFormatterUtil.getInstance().createURLString(formattedToday)
            );

            // Print the raw responce.
            printURLResponce(content);

            asteroidService.load(content, formattedToday);

            // Read the number of Ateroids close to the earth today
            int elementCount = asteroidService.getElementCount();
            System.out.println(String.format("\nNumber of NEOs close to the Earth on %s is %d\n",
                    formattedToday, elementCount));

            if (elementCount == 0) {
                return;
            }

            Asteroid biggestNEO = asteroidService.getBiggest();
            Asteroid closestNEO = asteroidService.getClosest();

            System.out.println(String.format(
                    "\n*** The biggest Asteroid details *** %s", biggestNEO));
            System.out.println(String.format(
                    "\n*** The closest Asteroid details *** %s", closestNEO));

        } catch (JsonException e) { //| AsteroidParsingException
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