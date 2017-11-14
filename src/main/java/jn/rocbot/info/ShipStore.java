package jn.rocbot.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.Main;
import jn.rocbot.ships.RARITY;
import jn.rocbot.ships.Ship;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class ShipStore {
    public static ArrayList<Ship> SHIPS;

    public static void init(){
        SHIPS = new ArrayList<>();

        JsonParser parser = new JsonParser();

        try {
            JsonObject shipsjson = parser.parse(new JsonReader(new FileReader("res/ships.json"))).getAsJsonObject();
            JsonArray ships = (JsonArray) shipsjson.get("ships");

            for (JsonElement jsonelementship : ships){
                JsonObject jsonship = jsonelementship.getAsJsonObject();

                try {
                    Ship ship = new Ship(jsonship.get("name").getAsString(), jsonship.getAsJsonObject("weapon").get("name").getAsString(),
                            AuraStore.fromName(jsonship.get("aura").getAsString()),
                            jsonship.get("zen").getAsString(),
                            RARITY.valueOf(RARITY.fromInt(jsonship.get("r").getAsInt())));

                    SHIPS.add(ship);
                } catch (AuraStore.AuraNotFounException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Ship getShip(String shipname) throws ShipNotFoundException{
        for(Ship ship : SHIPS){
            if(Objects.equals(ship.name.toLowerCase(), shipname.toLowerCase())) return ship;
        }

        throw new ShipNotFoundException("Found no ship with name: " + shipname);
    }

    public static class ShipNotFoundException extends Exception{
        public ShipNotFoundException(String s) { super(s); }
    }
}
