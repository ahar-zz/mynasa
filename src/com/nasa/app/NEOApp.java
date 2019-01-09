package com.nasa.app;

import com.nasa.app.helper.UrlFormatterUtil;
import com.nasa.app.models.Asteroid;
import com.nasa.app.services.AsteroidService;
import com.nasa.app.services.NasaService;

import javax.json.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class NEOApp {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final NasaService nasaService;
    private final AsteroidService asteroidService;

    /*
     * Constructor for NEOApp
     */
    public NEOApp(NasaService nasaService, AsteroidService asteroidService) {
        this.nasaService = nasaService;
        this.asteroidService = asteroidService;
    }

    /**
     * Main method for retrieving data and printing it out.
     *
     * @param args
     */
    public static void main(String[] args) {
        NasaService nasaService = new NasaService();
        AsteroidService asteroidService = new AsteroidService();

        NEOApp neoApp = new NEOApp(nasaService, asteroidService);
        neoApp.run();
    }

    /*
     * Method to run whole application.
     */
    private void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("\n\nFinished work.")));

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

    /**
     * Method to print out url response.
     *
     * @param content - byte array to print.
     */
    private static void printURLResponce(byte[] content) {
        System.out.println("/***** URL Content *****\n");
        System.out.println(new String(content));
    }
}