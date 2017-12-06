package jn.rocbot.info.stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.ships.Aura;
import jn.rocbot.ships.Ship;
import jn.rocbot.ships.Zen;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringJoiner;

public class ZenStore {
    public static ArrayList<Zen> ZENS;

    public static void init(){
        ZENS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File("res/zens.json")),
                            "UTF8")
            );

            JsonObject zensjson = parser.parse(new JsonReader(reader)).getAsJsonObject();
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
                if(!jsonzen.has("abbreviations")) {
                    ZENS.add(new Zen(
                            name, desc, ultimateName, ultimateDesc, propertiesList, propertiesFormatList
                    ));
                } else {
                    ArrayList<String> abbreviations = new ArrayList<>();
                    JsonArray jsonAbbreviations = jsonzen.getAsJsonArray("abbreviations");
                    for (int i = 0; i < jsonAbbreviations.size(); i++){
                        abbreviations.add(jsonAbbreviations.get(i).getAsString());
                    } ZENS.add(new Zen(
                            name, desc, ultimateName, ultimateDesc, propertiesList, propertiesFormatList
                    ).setAbbreviations(abbreviations.toArray(new String[jsonAbbreviations.size()])));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static boolean isZen(String zenName){
        for(Zen zen : ZENS){
            for(String abbreviation : zen.abbreviations) {
                if (zenName.toLowerCase().equals(abbreviation.toLowerCase())) return true;
            } if(zen.name.toLowerCase().equals(zenName.toLowerCase())) return true;
        } return false;
    }

    public static Zen getZen(String name) throws ZenNotFoundException {
        for(Zen zen : ZENS){
            if(name.toLowerCase().equals(zen.name.toLowerCase())) return zen;
            for(String abbreviation: zen.abbreviations) {
              if(abbreviation.toLowerCase().equals(name.toLowerCase())) return zen;
            }
        } throw new ZenNotFoundException(String.format("No zen with name %s!", name));
    }

    public static String allNames() {
        StringJoiner joiner = new StringJoiner("\n");
        for(Zen zen: ZENS){
            joiner.add(zen.name.toLowerCase());
        } return joiner.toString();
    }

    public static Zen randomZen() {
        return ZENS.get(new Random().nextInt(ZENS.size() - 1));
    }

    public static class ZenNotFoundException extends Exception {
        public ZenNotFoundException(String message) {
            super(message);
        }
    }
}
