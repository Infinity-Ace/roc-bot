package jn.rocbot.info.stores;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.ships.RARITY;
import jn.rocbot.ships.Ship;

import java.io.*;
import java.util.*;

public class ShipStore {
    public static ArrayList<Ship> SHIPS;

    public static Ship randomShip(){
        Random random = new Random();
        return SHIPS.get(random.nextInt(SHIPS.size() -1));
    }

    public static void init(){
        SHIPS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File("res/ships.json")),
                            "UTF8"));

            JsonObject shipsjson = parser.parse(new JsonReader(reader)).getAsJsonObject();
            JsonArray ships = (JsonArray) shipsjson.get("ships");

            for (JsonElement jsonelementship : ships){
                JsonObject jsonship = jsonelementship.getAsJsonObject();

                try {
                    Ship ship = new Ship(jsonship.get("name").getAsString(),
                            WeaponStore.fromName(jsonship.getAsJsonObject("weapon").get("name").getAsString()),
                            AuraStore.fromName(jsonship.get("aura").getAsString()),
                            ZenStore.fromName(jsonship.get("zen").getAsString()),
                            RARITY.valueOf(RARITY.fromInt(jsonship.get("r").getAsInt())));

                    SHIPS.add(ship);
                } catch (AuraStore.AuraNotFoundException e) {
                    e.printStackTrace();
                } catch (ZenStore.ZenNotFoundException e) {
                    e.printStackTrace();
                } catch (WeaponStore.WeaponNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
