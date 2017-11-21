package jn.rocbot.info;

import jn.rocbot.Emojis;
import jn.rocbot.ships.RARITY;
import jn.rocbot.ships.Ship;
import jn.rocbot.utils.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public class Description implements Formatter{
    private EmbedBuilder emb;

    public Description(Ship ship){
        emb = new EmbedBuilder().setTitle(bold(ship.name + " " + ship.rarity.toEmoji()));
        emb.setThumbnail()
        emb.addField(ship.weapon.name, ship.weapon.simpleDesc(false), false);
        emb.addField(ship.aura.name, ship.aura.simpleDesc(false), false);
        emb.addField(ship.zen.name, ship.zen.simpleDesc(false), false);
    }

    public void addField(MessageEmbed.Field field){
        emb.addField(field);
    }

    public void addField(String name, String content, boolean inLine){
        emb.addField(name, content, inLine);
    }

    public MessageEmbed get(){
        return emb.build();
    }
}
