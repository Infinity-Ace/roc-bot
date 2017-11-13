package jn.rocbot.Permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.core.entities.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

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
    }

    public static ArrayList<Moderator> MODERATORS;

    public static void init(){
        MODERATORS = new ArrayList<>();

        JsonParser parser = new JsonParser(); try {
            JsonObject moderatorsjson = parser.parse(new JsonReader(
                    new FileReader("meta/permissions.json")
            )).getAsJsonObject();

            JsonArray moderators = (JsonArray) moderatorsjson.get("moderators");

            for (JsonElement jsonelementmoderator : moderators){
                JsonObject jsonmoderator = jsonelementmoderator.getAsJsonObject();

                MODERATORS.add(new Moderator(
                        jsonmoderator.get("name").getAsString(),
                        jsonmoderator.get("longID").getAsLong()
                ));
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public static Moderator fromLongID(long id){
        for(Moderator moderator : MODERATORS){
            if(moderator.longID == id) return moderator;
        }
        return null;
    }

    public static boolean isModerator(User user){
        for(Moderator mod : MODERATORS){
            if(mod.longID == user.getIdLong()) return true;
        }

        return false;
    }
}
