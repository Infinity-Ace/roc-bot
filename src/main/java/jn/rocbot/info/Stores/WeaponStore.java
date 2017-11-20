package jn.rocbot.info.Stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.Main;
import jn.rocbot.ships.Weapon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WeaponStore {
    public static ArrayList<Weapon> WEAPONS;

    public static void init(){
        WEAPONS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        try {
            JsonObject shispsjson= parser.parse(new JsonReader(new FileReader("res/ships.json"))).getAsJsonObject();
            JsonArray ships = (JsonArray) shispsjson.get("ships");

            for (JsonElement jsonelementship : ships){

                JsonObject weapon = jsonelementship.getAsJsonObject().get("weapon").getAsJsonObject();

                HashMap<String, String> propertiesList = new HashMap<>();
                HashMap<String, String> propertiesFormatList = new HashMap<>();

                for (String key : weapon.keySet()) {
                    if(Objects.equals(key, "name") || Objects.equals(key, "damage output")) {

                    } else {
                        if (!key.contains("-format")) {
                            propertiesList.put(key, weapon.get(key).getAsString());
                        } else {
                            propertiesFormatList.put(key, weapon.get(key).getAsString());
                        }
                    }
                }

                if(weapon.keySet().contains("damage output")) {
                    WEAPONS.add(new Weapon(
                            weapon.get("name").getAsString(),
                            weapon.get("damage output").getAsFloat(),
                            propertiesList, propertiesFormatList
                    ));
                } else {
                    WEAPONS.add(new Weapon(
                            weapon.get("name").getAsString(),
                            0f,
                            propertiesList, propertiesFormatList
                    ));
                }
                Main.log(Main.LOGTYPE.INFO, String.valueOf(weapon.keySet()));
                Main.log(Main.LOGTYPE.INFO, String.valueOf(propertiesList) + "\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWeapon(String weaponName){
        boolean found = false;
        for(Weapon weapon: WEAPONS){
            if(weapon.name.toLowerCase().equals(weaponName.toLowerCase())) { found = true; break; }
        }
        return found;
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
