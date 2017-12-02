package jn.rocbot.commands.normalcommands.withsubcommand;

import jn.rocbot.info.stores.AuraStore;
import jn.rocbot.info.stores.ZenStore;
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
                        try {
                            return ship.aura.name.toLowerCase().equals(
                                    AuraStore.fromName(
                                            value.split(":")[1].toLowerCase()
                                    ).name.toLowerCase()
                            );

                        } catch (AuraStore.AuraNotFoundException e) {
                            e.printStackTrace();
                        }
                    case "zen":
                        try {
                            return ship.zen.name.toLowerCase().equals(
                                    ZenStore.fromName(
                                            value.split(":")[1].toLowerCase()
                                    ).name.toLowerCase()
                            );

                        } catch (ZenStore.ZenNotFoundException e) {
                            e.printStackTrace();
                        }
                    case "weapon":
                        return ship.weapon.name.toLowerCase().equals(value.split(":")[1].toLowerCase());
                    case "rarity":
                        return ship.rarity.name.toLowerCase().equals(value.split(":")[1].toLowerCase());
                } break;
        }

        return false;
    }
}
