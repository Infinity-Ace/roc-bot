package jn.rocbot.utils;

import jn.rocbot.info.stores.ShipStore;
import jn.rocbot.ships.Ship;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.HashMap;
import java.util.StringJoiner;

public class Search {
    public static Ship findShip(String searchString) throws ShipStore.ShipNotFoundException {
        String[] shipNames = ShipStore.allNames().split("\n");
        HashMap<Ship, Integer> ratios = new HashMap<>();
        int lowestRatio = 35;
        for(String shipName : shipNames){
            Integer currentSearchResult = FuzzySearch.tokenSetPartialRatio(shipName, searchString);
            if(currentSearchResult < lowestRatio) lowestRatio = currentSearchResult;
            ratios.put(
                    ShipStore.getShip(shipName),
                    currentSearchResult
            );
        }

        if(lowestRatio < 35)
            throw new ShipStore.ShipNotFoundException(String.format("Found no ship named %s", searchString));

        Ship highestHit = ShipStore.randomShip();

        for(Ship key : ratios.keySet()){
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } return highestHit;
    }

    public String testShipSearch(String searchString){
        StringJoiner returned = new StringJoiner("\n");
        String[] shipNames = ShipStore.allNames().split("\n");

        HashMap<Ship, Integer> ratios = new HashMap<>();
        int lowestRatio = 35;
        for(String shipName : shipNames){
            Integer currentSearchResult = FuzzySearch.tokenSetPartialRatio(shipName, searchString);
            if(currentSearchResult < lowestRatio) lowestRatio = currentSearchResult;
            try {
                ratios.put(
                        ShipStore.getShip(shipName),
                        currentSearchResult
                );
            } catch (ShipStore.ShipNotFoundException e) {
                returned.add(String.format("shipName %s is not a valid name!", shipName));
            }
        }

        returned.add(String.format("Lowest ratio is %d!", lowestRatio));

        Ship highestHit = ShipStore.randomShip();
        for(Ship key : ratios.keySet()){
            returned.add(String.format("%s had a ratio of %d", key.name, ratios.get(key)));
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } returned.add("Most matching is: " + highestHit.name + " with a ratio of " + ratios.get(highestHit));

        return returned.toString();
    }
}
