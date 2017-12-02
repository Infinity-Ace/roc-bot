package jn.rocbot.info;

import jn.rocbot.info.stores.ShipPicStore;
import jn.rocbot.ships.DamageType;
import jn.rocbot.ships.Ship;
import jn.rocbot.utils.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

import static jn.rocbot.ships.DamageType.AP;
import static jn.rocbot.ships.DamageType.HI;
import static jn.rocbot.ships.DamageType.SB;

public class ShipDescription implements Formatter{
    private EmbedBuilder desc;

    public ShipDescription(Ship ship){
        desc = new EmbedBuilder().setTitle(bold(ship.name + " " + ship.rarity.toEmoji()));

        Color color = null;

        if (ship.weapon.damageType.toString().toLowerCase().equals(SB.toString().toLowerCase())) {
            color = new Color(0x0080FE);

        } else if (ship.weapon.damageType.toString().toLowerCase().equals(AP.toString().toLowerCase())) {
            color = new Color(0xCAAE00);

        } else if (ship.weapon.damageType.toString().toLowerCase().equals(HI.toString().toLowerCase())) {
            color = new Color(0xB43200);

        } desc.setColor(color);

        if(ShipPicStore.hasPic(ship)) desc.setThumbnail(ShipPicStore.getPicURL(ship));

        desc.addField(ship.weapon.name, ship.weapon.simpleDesc(false), false);
        desc.addField(ship.aura.name, ship.aura.simpleDesc(false), false);
        desc.addField(ship.zen.name, ship.zen.simpleDesc(false), false);
    }

    public void addField(MessageEmbed.Field field){
        desc.addField(field);
    }

    public void addField(String name, String content, boolean inLine){
        desc.addField(name, content, inLine);
    }

    public MessageEmbed get(){
        return desc.build();
    }
}
