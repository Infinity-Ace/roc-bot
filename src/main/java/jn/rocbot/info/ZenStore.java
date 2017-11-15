package jn.rocbot.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.Main;
import jn.rocbot.ships.Zen;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ZenStore {
    public static ArrayList<Zen> ZENS;

    public static void init(){
        ZENS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        try {
            JsonObject zensjson = parser.parse(new JsonReader(new FileReader("res/zens.json"))).getAsJsonObject();
            JsonArray zens = (JsonArray) zensjson.get("zens");

            for (JsonElement jsonelementzen : zens){
                JsonObject jsonzen = jsonelementzen.getAsJsonObject();

                String name = jsonzen.get("name").getAsString();
                String desc = jsonzen.get("desc").getAsString();

                JsonObject properties = jsonzen.getAsJsonObject("properties");
                HashMap<String, String> propertiesList = new HashMap<>();
                HashMap<String, String> propertiesFormatList = new HashMap<>();
                properties.keySet().forEach((String key) ->{
                    if(!key.contains("-format")){
                        propertiesList.put(key, properties.get(key).getAsString());
                    } else {
                        propertiesFormatList.put(key, properties.get(key).getAsString());
                    }
                });

                JsonObject ult = jsonzen.getAsJsonObject("ult");
                String ultimateName = ult.get("name").getAsString();
                String ultimateDesc = ult.get("desc").getAsString();

                ZENS.add(new Zen(
                        name, desc, ultimateName, ultimateDesc, propertiesList, propertiesFormatList
                ));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isZen(String zenName){
        boolean found = false;
        for(Zen zen : ZENS){
            if(zen.name.toLowerCase().equals(zenName.toLowerCase())) { found = true; break; }
        }
        return found;
    }

    public static Zen fromName(String name) throws ZenNotFoundException {
        for(Zen zen : ZENS){
            if(name.toLowerCase().equals(zen.name.toLowerCase())) return zen;
        } throw new ZenNotFoundException();
    }

    public static class ZenNotFoundException extends Exception {}
}
