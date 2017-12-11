package jn.rocbot.permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.core.entities.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringJoiner;

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

    public static void init() throws FileNotFoundException {
        MASTERS = new ArrayList<>();

        JsonParser parser = new JsonParser();
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
    }

    public static Master fromLongID(long id){
        for(Master master : MASTERS){
            if(master.longID == id) return master;
        } return null;
    }

    public static boolean isMaster(User user){
        for(Master master : MASTERS){
            if(master.longID == user.getIdLong()) return true;
        } return false;
    }

    public static String allMasters(){
        StringJoiner allMasters = new StringJoiner(", ");
        for(Master master : MASTERS) allMasters.add(master.name);
        return allMasters.toString();
    }
}
