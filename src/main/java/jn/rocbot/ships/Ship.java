package jn.rocbot.ships;

import jn.rocbot.info.ShipDescription;
import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;
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
        return info.toString();
    }

    public MessageEmbed desc(){
        return new ShipDescription(this).get();
    }

    public static abstract class ShipProperty {
        public final String name;
        public ShipProperty(String name){
            this.name = name;
        }
    }
}
