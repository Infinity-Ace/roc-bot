package jn.rocbot.info;

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
    public static ArrayList<Aura> AURAS = new ArrayList<Aura>();

    public static void init(){
        JsonParser parser = new JsonParser();

        try {
            JsonObject aurasjson = parser.parse(new JsonReader(new FileReader("res/auras.json"))).getAsJsonObject();
            JsonArray auras = (JsonArray) aurasjson.get("auras");

            for (JsonElement jsonelementaura : auras){
                JsonObject jsonaura = jsonelementaura.getAsJsonObject();

                HashMap<String, String> propertiesList = new HashMap<>();

                JsonObject properties = jsonaura.getAsJsonObject("properties");

                properties.keySet().forEach((String key) ->{
                    propertiesList.put(
                            key,
                            properties.get(key).getAsString()
                    );
                });

                HashMap<String, String> ultimatePropertiesList = new HashMap<>();

                JsonObject ultimateProperties = jsonaura.getAsJsonObject("ult");

                ultimateProperties.keySet().forEach((String key) -> {
                    ultimatePropertiesList.put(
                            key,
                            ultimateProperties.get(key).getAsString()
                    );
                });

                //String name, String description, String ultimateName, HashMap<String, String> properties,
                //HashMap<String, String> ultimateProperties

                AURAS.add(new Aura(
                        jsonaura.get("name").getAsString(),
                        jsonaura.get("desc").getAsString(),
                        ultimatePropertiesList.get("name"),
                        propertiesList,
                        ultimatePropertiesList
                ));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Aura fromName(String name) throws AuraNotFounException{
        for(Aura aura : AURAS){
            Main.log(Main.LOGTYPE.DEBUG, "AURAS contain " + AURAS.size() + " auras");
            if(aura.name.toLowerCase() == name.toLowerCase()) return aura;
        }

        throw new AuraNotFounException("Found no aura named: " + name);
    }

    public static class AuraNotFounException extends Exception{
        public AuraNotFounException(String message){
            super(message);
        }
    }
}
