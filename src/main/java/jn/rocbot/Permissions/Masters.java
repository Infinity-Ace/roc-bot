package jn.rocbot.Permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Masters {
    public static class Master{
        public String name;
        public String greeting;
        public long longID;

        public Master(String name, String greeting, long longID) {
            this.name = name;
            this.greeting = greeting;
            this.longID = longID;
        }
    }

    public static ArrayList<Master> MASTERS;

    public static void init(){
        MASTERS = new ArrayList<>();

        JsonParser parser = new JsonParser(); try {
            JsonObject mastersjson = parser.parse(new JsonReader(
                    new FileReader("meta/permissions.json")
            )).getAsJsonObject();

            JsonArray masters = (JsonArray) mastersjson.get("bot-masters");

            for (JsonElement jsonelementmaster : masters){
                JsonObject jsonmaster = jsonelementmaster.getAsJsonObject();

                MASTERS.add(new Master(
                        jsonmaster.get("name").getAsString(),
                        jsonmaster.get("greeting").getAsString()
                                .replace("@", jsonmaster.get("name").getAsString()),
                        jsonmaster.get("longID").getAsLong()
                ));
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public static Master fromLongID(long id){
        for(Master master : MASTERS){
            if(master.longID == id) return master;
        }

        return null;
    }
}
