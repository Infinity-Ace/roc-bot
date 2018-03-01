package jn.rocbot.ships;

import jn.rocbot.info.stores.AuraStore;
import jn.rocbot.info.stores.WeaponStore;
import jn.rocbot.info.stores.ZenStore;
import jn.rocbot.utils.Search;

public enum ShipPropertyType {
    Aura, Zen, Weapon, Rarity, DamageType, None;

    public static ShipPropertyType getShipPropertyType(String searchWord){
        return getShipPropertyType(searchWord, 60);
    }

    public static ShipPropertyType getShipPropertyType(String searchWord, int LOWEST_MATCH_RATIO){

        //First checking if the searchword is grammatically correct
        if(AuraStore.isAura(searchWord))
            return ShipPropertyType.Aura;

        if(ZenStore.isZen(searchWord))
            return ShipPropertyType.Zen;

        if(WeaponStore.isWeapon(searchWord))
            return ShipPropertyType.Weapon;

        if(jn.rocbot.ships.Rarity
                 .isRarity(searchWord)
                ) return ShipPropertyType.Rarity;

        if(jn.rocbot.ships.DamageType
                 .isDamageType(searchWord.replace("-", " "))
                ) return ShipPropertyType.DamageType;

        try { // Checking if it is almost correct
            Search.findAura(searchWord, LOWEST_MATCH_RATIO);
            return ShipPropertyType.Aura;
        } catch (AuraStore.AuraNotFoundException ignored) { }

        try {
            Search.findZen(searchWord, LOWEST_MATCH_RATIO);
            return ShipPropertyType.Zen;
        } catch (ZenStore.ZenNotFoundException ignored) { }

        return ShipPropertyType.None; //If none of the above has returned
    }

}
