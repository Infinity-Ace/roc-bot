package jn.rocbot.utils;

import jn.rocbot.info.Stores.ShipStore;
import jn.rocbot.ships.Ship;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.HashMap;

public class Search {
    public static Ship findShip(String searchString) throws ShipStore.ShipNotFoundException {
        String[] shipNames = searchString.split("\n");
        HashMap<Ship, Integer> ratios = new HashMap<>();
        int lowestRatio = 0;
        for(String shipName : shipNames){
            Integer currentSearchResult = FuzzySearch.tokenSetPartialRatio(shipName, searchString);
            // if(currentSearchResult < lowestRatio)
            ratios.put(
                    ShipStore.getShip(shipName),
                    currentSearchResult
            );
        } Ship highestHit = ShipStore.randomShip();

        for(Ship key : ratios.keySet()){
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } return highestHit;
    }
}
