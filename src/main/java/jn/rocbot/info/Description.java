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
        emb.addField(ship.weapon, "", false);
        emb.addField(ship.aura.name, ship.aura.simpleDesc(), false);
        emb.addField(ship.zen.name, ship.zen.simpleDesc(), false);
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
