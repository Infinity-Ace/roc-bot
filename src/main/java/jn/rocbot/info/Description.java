package jn.rocbot.info;

import jn.rocbot.info.Stores.ShipPicStore;
import jn.rocbot.ships.Ship;
import jn.rocbot.utils.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class Description implements Formatter{
    private EmbedBuilder desc;

    public Description(Ship ship){
        desc = new EmbedBuilder().setTitle(bold(ship.name + " " + ship.rarity.toEmoji()));

        if(ShipPicStore.hasPic(ship)) desc.setThumbnail(ShipPicStore.getPicURL(ship));

        desc.addField(ship.weapon.name, ship.weapon.simpleDesc(false), false);
        desc.addField(ship.aura.name, ship.aura.simpleDesc(false), false);
        desc.addField(ship.zen.name, ship.zen.simpleDesc(false), false);

        desc.setColor(ShipPicStore.getShipsAvgColor(ship));
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
