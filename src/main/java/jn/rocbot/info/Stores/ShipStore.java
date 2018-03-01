package jn.rocbot.info.stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.ships.Rarity;
import jn.rocbot.ships.Ship;

import java.io.*;
import java.util.*;

public class ShipStore {
    public static ArrayList<Ship> SHIPS;

    public static Ship randomShip(){
        Random random = new Random();
        return SHIPS.get(random.nextInt(SHIPS.size() -1));
    }

    public static void init() throws FileNotFoundException, UnsupportedEncodingException, AuraStore.AuraNotFoundException, WeaponStore.WeaponNotFoundException, ZenStore.ZenNotFoundException {
        SHIPS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(
                                new File("res/ships.json")),
                        "UTF8"));

        JsonObject shipsjson = parser.parse(new JsonReader(reader)).getAsJsonObject();
        JsonArray ships = (JsonArray) shipsjson.get("ships");

        for (JsonElement jsonElement : ships){
            JsonObject jsonShip = jsonElement.getAsJsonObject();

            Ship ship = new Ship(
                    jsonShip.get("name").getAsString(),
                    WeaponStore.fromName(jsonShip.getAsJsonObject("weapon").get("name").getAsString()),
                    AuraStore.getAura(jsonShip.get("aura").getAsString()),
                    ZenStore.getZen(jsonShip.get("zen").getAsString()),
                    Rarity.fromInt(jsonShip.get("r").getAsInt())
            );

            SHIPS.add(ship);
        }

    }

    public static Ship getShip(String shipname) throws ShipNotFoundException{
        for(Ship ship : SHIPS){
            if(Objects.equals(ship.name.toLowerCase(), shipname.toLowerCase())) return ship;
        }

        throw new ShipNotFoundException("Found no ship with name: " + shipname);
    }

    public static boolean isShip(String name) {
        for (Ship ship : SHIPS) {
            if (ship.name.toLowerCase().equals(name.toLowerCase()))
                return true;
        } return false;
    }

    public static boolean isShip(String[] name) {
        StringJoiner shipName = new StringJoiner(" ");
        for(int i = 1; i < name.length; i++){
            shipName.add(name[i]);
            for(Ship ship : SHIPS) {
                if(ship.name.toLowerCase().equals(shipName.toString().toLowerCase())) return true;
            }
        } return false;
    }

    public static class ShipNotFoundException extends Exception{
        public ShipNotFoundException(String s) { super(s); }
    }

    public static String allNames(){
        StringJoiner joiner = new StringJoiner("\n");
        for(Ship ship : SHIPS){
            joiner.add(ship.name.toLowerCase());
        } return joiner.toString();
    }
}
