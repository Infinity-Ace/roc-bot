package jn.rocbot.utils;

import jn.rocbot.commands.normalcommands.withsubcommand.WithFilter;
import jn.rocbot.commands.normalcommands.withsubcommand.WithProperty;
import jn.rocbot.info.stores.AuraStore;
import jn.rocbot.info.stores.ShipStore;
import jn.rocbot.info.stores.ZenStore;
import jn.rocbot.ships.Aura;
import jn.rocbot.ships.Ship;
import jn.rocbot.ships.Zen;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class Search {
    public static Ship findShip(String searchString) throws ShipStore.ShipNotFoundException {
        return findShip(searchString, 55);
    }

    public static Ship findShip(String searchString, int lowestAllowedRatio) throws ShipStore.ShipNotFoundException {
        String[] shipNames = ShipStore.allNames().split("\n");

        HashMap<Ship, Integer> ratios = new HashMap<>();
        int currentLowestRatio = 35;
        for(String shipName : shipNames){
            Integer currentSearchResult = FuzzySearch.partialRatio(shipName, searchString.toLowerCase());
            if(currentSearchResult == 100) return ShipStore.getShip(shipName);
            if(currentSearchResult < currentLowestRatio)
                currentLowestRatio = currentSearchResult;
            try {
                ratios.put(ShipStore.getShip(shipName), currentSearchResult);
            } catch (ShipStore.ShipNotFoundException e) { } // Ignore because it won't happen anyways
        }

        Ship highestHit = ShipStore.randomShip();
        for(Ship key : ratios.keySet()){
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } if(ratios.get(highestHit) > lowestAllowedRatio)
            return highestHit;

        throw new ShipStore.ShipNotFoundException("Found no ship with name matching " + searchString);
    }

    public static Aura findAura(String searchString) throws AuraStore.AuraNotFoundException {
        return findAura(searchString, 55);
    }

    public static Aura findAura(String searchString, int lowestAllowedRatio) throws AuraStore.AuraNotFoundException{
        String[] auraNames = AuraStore.allNames().split("\n");

        HashMap<Aura, Integer> ratios = new HashMap<>();
        int currentLowestRatio = 35;
        for (String auraName : auraNames){
            Integer currentSearchResult = FuzzySearch.partialRatio(auraName, searchString.toLowerCase());
            if(currentSearchResult == 100) return AuraStore.getAura(searchString);
            if(currentSearchResult < currentLowestRatio)
                currentLowestRatio = currentSearchResult;
            try {
                ratios.put(AuraStore.getAura(auraName), currentSearchResult);
            } catch (AuraStore.AuraNotFoundException e){ } // Ignore because it won't happen anyways
        }

        Aura highestHit = AuraStore.randomAura();
        for(Aura key : ratios.keySet()) {
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } if(ratios.get(highestHit) > lowestAllowedRatio)
            return highestHit;
        else
            throw new AuraStore.AuraNotFoundException(("Found no aura with name matching " + searchString));
    }

    public static Zen findZen(String searchString) throws ZenStore.ZenNotFoundException {
        return findZen(searchString, 55);
    }

    public static Zen findZen(String searchString, int lowestAllowedRatio) throws ZenStore.ZenNotFoundException{
        String[] zenNames = ZenStore.allNames().split("\n");

        HashMap<Zen, Integer> ratios = new HashMap<>();
        int currentLowestRatio = 35;
        for (String zenName : zenNames){
            Integer currentSearchResult = FuzzySearch.partialRatio(zenName, searchString.toLowerCase());
            if(currentSearchResult == 100) return ZenStore.getZen(searchString);
            if(currentSearchResult < currentLowestRatio)
                currentLowestRatio = currentSearchResult;
            try {
                ratios.put(ZenStore.getZen(zenName), currentSearchResult);
            } catch (ZenStore.ZenNotFoundException e){ } // Ignore because it won't happen anyways
        }

        Zen highestHit = ZenStore.randomZen();
        for(Zen key : ratios.keySet()) {
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } if(ratios.get(highestHit) > lowestAllowedRatio)
            return highestHit;
        else
            throw new ZenStore.ZenNotFoundException("Found no zen with name matching " + searchString);
    }

    public String testAuraSearch(String searchString){
        StringJoiner returned = new StringJoiner("\n");
        String[] auraNames = AuraStore.allNames().split("\n");

        HashMap<Aura, Integer> ratios = new HashMap<>();
        int currentLowestRatio = 35;
        for (String auraName : auraNames){
            Integer currentSearchResult = FuzzySearch.partialRatio(auraName, searchString.toLowerCase());
            if(currentSearchResult < currentLowestRatio)
                currentLowestRatio = currentSearchResult;
            try {
                ratios.put(AuraStore.getAura(auraName), currentSearchResult);
            } catch (AuraStore.AuraNotFoundException e){ } // Ignore because it won't happen anyways
        }

        Aura highestHit = AuraStore.randomAura();
        for(Aura key : ratios.keySet()) {
            if(ratios.get(key) > ratios.get(highestHit)) highestHit = key;
        } returned.add(highestHit.name + " with ratio " + ratios.get(highestHit));

        return returned.toString();
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
