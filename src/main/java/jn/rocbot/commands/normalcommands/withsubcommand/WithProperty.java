package jn.rocbot.commands.normalcommands.withsubcommand;

import jn.rocbot.info.stores.AuraStore;
import jn.rocbot.ships.Ship;

public class WithProperty {
    public PROPERTY_TYPE type;
    public String value;

    public WithProperty(PROPERTY_TYPE type, String value) {
        this.type = type;
        this.value = value;
    }

    public enum PROPERTY_TYPE {
        HasShipProperty
    }

    public boolean isFullFilledBy(Ship ship){
        switch (this.type) {
            case HasShipProperty:
                switch (value.split(":")[0]){
                    case "aura":
                        return ship.aura.name.toLowerCase().equals(value.split(":")[1]);
                    case "zen":
                        return ship.zen.name.toLowerCase().equals(value.split(":")[1]);
                    case "weapon":
                        return ship.weapon.name.toLowerCase().equals(value.split(":")[1]);
                    case "rarity":
                        return ship.rarity.name.toLowerCase().equals(value.split(":")[1]);
                } break;
        }

        return false;
    }
}
