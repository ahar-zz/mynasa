package com.nasa.app.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NasaService {

    /*
     *
     * Retrieves page content and transfer it to bytes for future work
     *
     * @param url - url object
     */
    public byte[] getPageContent(String url) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL urlObj = new URL(url);

            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = urlObj.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }
}
