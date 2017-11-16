package jn.rocbot.info.Stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.Main;
import jn.rocbot.ships.Aura;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class AuraStore {
    public static ArrayList<Aura> AURAS = new ArrayList<>();

    public static void init(){
        JsonParser parser = new JsonParser();

        try {
            JsonObject aurasjson = parser.parse(new JsonReader(new FileReader("res/auras.json"))).getAsJsonObject();
            JsonArray auras = (JsonArray) aurasjson.get("auras");

            for (JsonElement jsonelementaura : auras){
                JsonObject jsonaura = jsonelementaura.getAsJsonObject();

                HashMap<String, String> propertiesList = new HashMap<>();

                JsonObject properties = jsonaura.getAsJsonObject("properties");

                HashMap<String, String> formatting = new HashMap<>();

                properties.keySet().forEach((String key) ->{
                    if(!key.contains("-format")) {
                        propertiesList.put(key, properties.get(key).getAsString());
                    } else {
                        formatting.put(key, properties.get(key).getAsString());
                    }
                });

                HashMap<String, String> ultimatePropertiesList = new HashMap<>();

                JsonObject ultimateProperties = jsonaura.getAsJsonObject("ult");

                ultimateProperties.keySet().forEach((String key) -> {
                    if(!key.contains("-format")) {
                        ultimatePropertiesList.put(key, ultimateProperties.get(key).getAsString());
                    } else {
                        formatting.put(key, ultimateProperties.get(key).getAsString());
                        Main.log(Main.LOGTYPE.DEBUG, "Put ulformat in: " + jsonaura.get("name").getAsString());
                    }
                });

                //String name, String desc, String ultimateName, HashMap<String, String> properties,
                //HashMap<String, String> ultimateProperties

                AURAS.add(new Aura(
                        jsonaura.get("name").getAsString(),
                        jsonaura.get("desc").getAsString(),
                        ultimatePropertiesList.get("name"),
                        propertiesList,
                        ultimatePropertiesList,
                        formatting
                ));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String auraStoreState(){
        String state = "The aurastore currently contaions the auras:";

        for (Aura aura : AURAS){
            state += "\n\t" + aura.name;
        }

        return state;
    }

    public static Aura fromName(String name) throws AuraNotFounException{
        for(Aura aura : AURAS){
            if(name.toLowerCase().equals(aura.name.toLowerCase())) return aura;
        } throw new AuraNotFounException("Found no aura named: " + name);
    }

    public static class AuraNotFounException extends Exception{
        public AuraNotFounException(String message){
            super(message);
        }
    }
}

