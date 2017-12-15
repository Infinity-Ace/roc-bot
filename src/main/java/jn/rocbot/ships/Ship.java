package jn.rocbot.ships;

import jn.rocbot.info.ShipDescription;
import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.info.stores.AuraStore;
import jn.rocbot.info.stores.WeaponStore;
import jn.rocbot.info.stores.ZenStore;
import jn.rocbot.utils.Formatter;
import jn.rocbot.utils.Search;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class Ship implements Formatter{
    public final String name;
    public final Weapon weapon;
    public final Aura aura;
    public final Zen zen;
    public final Rarity rarity;

    public Ship(String name, Weapon weapon, Aura aura, Zen zen, Rarity rarity) {
        this.name = name;
        this.weapon = weapon;
        this.aura = aura;
        this.zen = zen;
        this.rarity = rarity;
    }

    public String info(){
        SimpleDescBuilder info = new SimpleDescBuilder(bold(name));
        info.addLine("Weapon: " + weapon.simpleDesc(true));
        info.addLine("\nZen: " + zen.simpleDesc(true));
        info.addLine("\nAura: " + aura.simpleDesc(true));
        return info.get();
    }

    public MessageEmbed desc(){
        return new ShipDescription(this).get();
    }

    public enum ShipPropertyType {
        Aura, Zen, Weapon, Rarity, DamageType, None;

        public static ShipPropertyType getShipPropertyType(String searchWord){
            return getShipPropertyType(searchWord, 60);
        }

        public static ShipPropertyType getShipPropertyType(String searchWord, int LOWEST_MATCH_RATIO){

            //First checking if the searchword is grammatically correct
            if(AuraStore.isAura(searchWord))
                return Ship.ShipPropertyType.Aura;

            if(ZenStore.isZen(searchWord))
                return Ship.ShipPropertyType.Zen;

            if(WeaponStore.isWeapon(searchWord))
                return Ship.ShipPropertyType.Weapon;

            if(jn.rocbot.ships.Rarity
                     .isRarity(searchWord)
                    ) return Ship.ShipPropertyType.Rarity;

            if(jn.rocbot.ships.DamageType
                     .isDamageType(searchWord.replace("-", " "))
                    ) return Ship.ShipPropertyType.DamageType;

            // Checking if it is almost correct
            try {
                Search.findAura(searchWord, LOWEST_MATCH_RATIO);
                return Ship.ShipPropertyType.Aura;
            } catch (AuraStore.AuraNotFoundException ignored) { }

            try {
                Search.findZen(searchWord, LOWEST_MATCH_RATIO);
                return Ship.ShipPropertyType.Zen;
            } catch (ZenStore.ZenNotFoundException ignored) { }

            return Ship.ShipPropertyType.None; //If none of the above has returned
        }

    }

    public static abstract class ShipProperty {
        public final String name;
        public ShipProperty(String name){
            this.name = name;
        }
    }
}
