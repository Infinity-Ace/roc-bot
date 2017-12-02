package jn.rocbot.info.stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.ships.Weapon;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WeaponStore {
    public static ArrayList<Weapon> WEAPONS;

    public static void init(){
        WEAPONS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File("res/ships.json")),
                            "UTF8")
            );

            JsonObject shispsjson= parser.parse(new JsonReader(reader)).getAsJsonObject();
            JsonArray ships = (JsonArray) shispsjson.get("ships");

            for (JsonElement jsonelementship : ships){
                JsonObject weapon = jsonelementship.getAsJsonObject().get("weapon").getAsJsonObject();

                String damageType = weapon.get("damage type").getAsString();

                HashMap<String, String> propertiesList = new HashMap<>();
                HashMap<String, String> propertiesFormatList = new HashMap<>();

                for (String key : weapon.keySet()) {
                    if(Objects.equals(key, "name") || Objects.equals(key, "damage output") || Objects.equals(key, "damage type")) {

                    } else {
                        if (!key.contains("-format")) {
                            propertiesList.put(key, weapon.get(key).getAsString());
                        } else {
                            propertiesFormatList.put(key, weapon.get(key).getAsString());
                        }
                    }
                }

                try {
                    if(weapon.keySet().contains("damage output")) {
                        WEAPONS.add(new Weapon(
                                weapon.get("name").getAsString(),
                                weapon.get("damage output").getAsFloat(),
                                damageType, propertiesList, propertiesFormatList
                        ));
                    } else {
                        WEAPONS.add(new Weapon(
                                weapon.get("name").getAsString(),
                                0f,
                                damageType, propertiesList, propertiesFormatList
                        ));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWeapon(String weaponName){
        for(Weapon weapon: WEAPONS){
            if(weapon.name.toLowerCase().equals(weaponName.toLowerCase())) return true;
        } return false;
    }

    public static Weapon fromName(String name) throws WeaponNotFoundException{
        for(Weapon weapon: WEAPONS){
            if(name.toLowerCase().equals(weapon.name.toLowerCase())) return weapon;
        } throw new WeaponNotFoundException("Found no ship with name: " + name);
    }

    public static class WeaponNotFoundException extends Exception {
        public WeaponNotFoundException(String s) { super(s); }
    }
}
