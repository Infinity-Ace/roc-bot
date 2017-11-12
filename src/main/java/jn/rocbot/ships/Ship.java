package jn.rocbot.ships;

import jn.rocbot.commands.Formatter;
import jn.rocbot.info.ShipStore;

public class Ship implements Formatter{
    public final String name;
    public final String weapon;
    public final Aura aura;
    public final String zen;
    public final RARITY rarity;

    public Ship(String name, String weapon, Aura aura, String zen, RARITY rarity) {
        this.name = name;
        this.weapon = weapon;
        this.aura = aura;
        this.zen = zen;
        this.rarity = rarity;
    }

    public String simpleToString(){
        return rarity.toEmoji() + " " + bold(name + ":") + " Weapon: " + italic(weapon) + ", Aura: " + italic(aura.name) + ", Zen: " + italic(zen);
    }

    public static boolean isShip(String name){
        try {
            return ShipStore.getShip(name) != null;
        } catch (ShipStore.ShipNotFoundException e) {
            return false;
        }
    }
}
