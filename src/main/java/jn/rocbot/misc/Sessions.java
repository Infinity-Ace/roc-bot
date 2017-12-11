package jn.rocbot.misc;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import java.io.*;

public class Sessions {
    public static int SESSIONS;

    public static void start(){
        init();

        newSession();
    }

    private static void init(){
        JsonParser parser = new JsonParser();

        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File(
                                            "meta/sessions.json"
                                    )
                            ), "UTF8"
                    )
            );

            SESSIONS = parser.parse(reader).getAsJsonObject().get("sessions").getAsInt();

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void newSession(){
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(new File("meta/sessions.json")));

            writer.beginObject();
            writer.name("sessions");
            writer.value(SESSIONS + 1);
            writer.endObject();

            writer.flush();
            writer.close();

            SESSIONS += 1;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
