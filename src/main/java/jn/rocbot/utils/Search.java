package jn.rocbot.utils;

import jn.rocbot.commands.normalcommands.withsubcommand.WithFilter;
import jn.rocbot.commands.normalcommands.withsubcommand.WithProperty;
import jn.rocbot.info.stores.ShipStore;
import jn.rocbot.ships.Ship;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Search {
    public static Ship findShip(String searchString) throws ShipStore.ShipNotFoundException {
        return findShip(searchString, 30);
    }

    public static Ship findShip(String searchString, int ratio) throws ShipStore.ShipNotFoundException {
        String[] shipNames = ShipStore.allNames().split("\n");

        HashMap<Ship, Integer> ratios = new HashMap<>();
        int lowestRatio = 35;
        for(String shipName : shipNames){
            Integer currentSearchResult = FuzzySearch.partialRatio(shipName, searchString.toLowerCase());

            if(currentSearchResult < lowestRatio)
                lowestRatio = currentSearchResult;
            try {
                ratios.put(ShipStore.getShip(shipName), currentSearchResult);
            } catch (ShipStore.ShipNotFoundException e) { } //Do nothing
        }

        Ship highestHit = ShipStore.randomShip();
        for(Ship key : ratios.keySet()){
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        }
        if(ratios.get(highestHit) > ratio)
            return highestHit;
        else
            throw new ShipStore.ShipNotFoundException("Found no ship with name matching " + searchString);
    }

    public String testShipSearch(String searchString){
        StringJoiner returned = new StringJoiner("\n");
        String[] shipNames = ShipStore.allNames().split("\n");

        HashMap<Ship, Integer> ratios = new HashMap<>();
        int lowestRatio = 35;
        for(String shipName : shipNames){
            Integer currentSearchResult = FuzzySearch.ratio(shipName, searchString.toLowerCase());

            if(currentSearchResult < lowestRatio)
                lowestRatio = currentSearchResult;
            try {
                ratios.put(ShipStore.getShip(shipName), currentSearchResult);
            } catch (ShipStore.ShipNotFoundException e) {
                returned.add(String.format("shipName %s is not a valid name!", shipName));
            }
        }

        returned.add(String.format("Lowest ratio is %d!", lowestRatio));

        Ship highestHit = ShipStore.randomShip();
        for(Ship key : ratios.keySet()){
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } returned.add("Most matching is: " + highestHit.name + " with a ratio of " + ratios.get(highestHit));

        return returned.toString();
    }

    public static Ship[] shipsWith(WithFilter filter){
        ArrayList<Ship> ships = new ArrayList<>();

        for(Ship ship : ShipStore.SHIPS){
            for(WithProperty property : filter.properties) {
                if(property.isFullFilledBy(ship)) {
                    ships.add(ship);
                    break;
                }
            }
        } return ships.toArray(new Ship[ships.size()]);
    }
}
