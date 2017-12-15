package jn.rocbot.commands.normalcommands.withsubcommand;

import jn.rocbot.info.stores.AuraStore;
import jn.rocbot.info.stores.ZenStore;
import jn.rocbot.misc.NotFoundException;
import jn.rocbot.ships.DamageType;
import jn.rocbot.ships.Rarity;
import jn.rocbot.ships.Ship;
import jn.rocbot.utils.Search;

import static jn.rocbot.ships.Ship.ShipPropertyType.*;

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
    
    public static WithProperty createFrom(String searchWord) throws NotFoundException{
        int LOWEST_MATCH_RATIO = 100;
        switch (getShipPropertyType(searchWord)) {
            case Aura:
                try {
                    return
                            new WithProperty(
                                    WithProperty.PROPERTY_TYPE.HasShipProperty,
                                    "aura:" + Search.findAura(
                                            searchWord, LOWEST_MATCH_RATIO
                                    ).name.toLowerCase()
                    );
                } catch (AuraStore.AuraNotFoundException ignored) {
                }
                break;
            case Zen:
                try {
                    return
                            new WithProperty(WithProperty.PROPERTY_TYPE.HasShipProperty,
                                    "zen:" + Search.findZen(
                                            searchWord.toLowerCase(), LOWEST_MATCH_RATIO
                                    ).name.toLowerCase()
                    );
                } catch (ZenStore.ZenNotFoundException ignored) {
                }
                break;
            case Weapon:
                return
                        new WithProperty(WithProperty.PROPERTY_TYPE.HasShipProperty,
                                "weapon:" + searchWord.toLowerCase()
                        );
            case Rarity:
                return
                        new WithProperty(
                                WithProperty.PROPERTY_TYPE.HasShipProperty,
                                "rarity:"
                                        + jn.rocbot.ships.Rarity
                                            .fromString(searchWord.toLowerCase()).name.toLowerCase()
                        );
            case DamageType:
                try {
                    String damageType;
                    switch (searchWord.toLowerCase()) {
                        case "sb":
                            damageType = "sb";
                            break;
                        case "ap":
                            damageType = "ap";
                            break;
                        case "hi":
                            damageType = "hi";
                            break;
                        default:
                            damageType =
                                    jn.rocbot.ships.DamageType
                                        .fromString(searchWord.replace("-", " ")).toString();
                    }

                    return new WithProperty(
                            WithProperty.PROPERTY_TYPE.HasShipProperty,
                            "damagetype:" + damageType
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case None:
                throw new NotFoundException("No ship-property name " + searchWord);
            default:
                throw new NotFoundException("No ship-property name " + searchWord);
        } return null;
    }

    public boolean isFullFilledBy(Ship ship){
        switch (this.type) {
            case HasShipProperty:
                switch (value.split(":")[0]){
                    case "aura":
                        try {
                            return ship.aura.name.toLowerCase().equals(
                                    Search.findAura(
                                            value.split(":")[1].toLowerCase(), WithSubCommand.LOWEST_MATCH_RATIO
                                    ).name.toLowerCase()
                            );

                        } catch (AuraStore.AuraNotFoundException e) {
                            e.printStackTrace();
                        }
                    case "zen":
                        try {
                            return ship.zen.name.toLowerCase().equals(
                                    Search.findZen(
                                            value.split(":")[1].toLowerCase(), WithSubCommand.LOWEST_MATCH_RATIO
                                    ).name.toLowerCase()
                            );

                        } catch (ZenStore.ZenNotFoundException e) {
                            e.printStackTrace();
                        }
                    case "weapon":
                        return ship.weapon.name.toLowerCase().equals(value.split(":")[1].toLowerCase());
                    case "rarity":
                        return ship.rarity.name.toLowerCase().equals(value.split(":")[1].toLowerCase());
                    case "damagetype":
                        try {
                            return ship.weapon.damageType.toString().toLowerCase().equals(
                                    jn.rocbot.ships.DamageType.fromString(
                                            value.split(":")[1].toLowerCase()
                                    ).toString().toLowerCase()
                            );

                        } catch (DamageType.DamageTypeNotFoundException e) {
                            e.printStackTrace();
                        }
                } break;
        }

        return false;
    }
}
