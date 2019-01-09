package com.nasa.app.helper;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.JsonException;
import javax.json.stream.JsonGenerator;

/**
 * Singleton to convert JSON to well formatted string.
 */
public class JsonFormatterUtil {
    private static final JsonFormatterUtil instance = new JsonFormatterUtil();
    private JsonWriterFactory factory;

    private JsonFormatterUtil() {
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        this.factory = Json.createWriterFactory(config);
    }

    public static JsonFormatterUtil getInstance() {
        return instance;
    }

    public String format(JsonObject obj) {
        if (obj == null) {
            return "";
        }

        StringWriter stringWriter = new StringWriter();

        try (JsonWriter jsonWriter = factory.createWriter(stringWriter)) {
            jsonWriter.writeObject(obj);
        } catch (JsonException | IllegalStateException e) {
            System.out.println(String.format(
                    "\nSomething went wrong during formatting %s as a string.", obj.toString()));
            return "";
        }
        return stringWriter.toString();
    }
}
