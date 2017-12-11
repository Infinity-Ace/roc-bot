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

public class Moderators {
    public static class Moderator{
        public String name;
        public String greeting;
        public long longID;

        public Moderator(String name, long longID) {
            this.name = name;
            this.greeting = "Hello Moderator " + name;
            this.longID = longID;
        }

        public Moderator(String name, String greeting, long longID) {
            this.name = name;
            this.greeting = greeting;
            this.longID = longID;
        }
    }

    public static ArrayList<Moderator> MODERATORS;

    public static void init() throws FileNotFoundException {
        MODERATORS = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject moderatorsjson = parser.parse(new JsonReader(
                new FileReader("meta/permissions.json")
        )).getAsJsonObject();

        JsonArray moderators = (JsonArray) moderatorsjson.get("moderators");

        for (JsonElement jsonelementmoderator : moderators){
            JsonObject jsonmoderator = jsonelementmoderator.getAsJsonObject();

            if(!jsonmoderator.keySet().contains("greeting")) {
                MODERATORS.add(new Moderator(
                        jsonmoderator.get("name").getAsString(),
                        jsonmoderator.get("longID").getAsLong()
                ));
            } else {
                MODERATORS.add(new Moderator(
                        jsonmoderator.get("name").getAsString(),
                        jsonmoderator.get("greeting").getAsString(),
                        jsonmoderator.get("longID").getAsLong()
                ));
            }
        }
    }

    public static Moderator fromLongID(long id){
        for(Moderator moderator : MODERATORS){
            if(moderator.longID == id) return moderator;
        } return null;
    }

    public static boolean isModerator(User user){
        for(Moderator mod : MODERATORS){
            if(mod.longID == user.getIdLong()) return true;
        } return false;
    }

    public static String allModerators(){
        StringJoiner allMods = new StringJoiner(", ");
        for(Moderator mod : MODERATORS) allMods.add(mod.name);

        return allMods.toString();
    }
}
