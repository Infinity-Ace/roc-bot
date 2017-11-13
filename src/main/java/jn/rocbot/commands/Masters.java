package jn.rocbot.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Masters {
    public String name;
    public String greeting;
    public long longID;

    private Masters(String name, String greeting, long longID){
        this.name = name;
        this.greeting = greeting;
        this.longID = longID;
    }

    public static ArrayList<Masters> MASTERS;

    public static void init(){
        MASTERS = new ArrayList<>();

        JsonParser parser = new JsonParser(); try {
            JsonObject mastersjson = parser.parse(new JsonReader(
                    new FileReader("res/roles.json")
            )).getAsJsonObject();

            JsonArray masters = (JsonArray) mastersjson.get("bot-masters");

            for (JsonElement jsonelementmaster : masters){
                JsonObject jsonmaster = jsonelementmaster.getAsJsonObject();

                MASTERS.add(new Masters(
                        jsonmaster.get("name").getAsString(),
                        jsonmaster.get("greeting").getAsString()
                                .replace("@", jsonmaster.get("name").getAsString()),
                        jsonmaster.get("longId").getAsLong()
                ));
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public static Masters fromLongID(long id){
        for(Masters master : MASTERS){
            if(master.longID == id) return master;
        }

        return null;
    }
}
