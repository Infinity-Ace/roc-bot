package jn.rocbot.info.stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.ships.Aura;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AuraStore {
    public static ArrayList<Aura> AURAS = new ArrayList<>();

    public static void init(){
        JsonParser parser = new JsonParser();

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File("res/auras.json")),
                                            "UTF8")
            );

            JsonObject aurasjson = parser.parse(new JsonReader(reader)).getAsJsonObject();
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
        } catch (UnsupportedEncodingException e) {
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

    public static Aura fromName(String name) throws AuraNotFoundException {
        for(Aura aura : AURAS){
            if(name.toLowerCase().equals(aura.name.toLowerCase())) return aura;
        } throw new AuraNotFoundException("Found no aura named: " + name);
    }

    public static class AuraNotFoundException extends Exception{
        public AuraNotFoundException(String message){
            super(message);
        }
    }

    public static boolean isAura(String string){
        for (Aura aura : AURAS)
            if(aura.name.toLowerCase().equals(string.toLowerCase())) return true;

        return false;
    }
}

