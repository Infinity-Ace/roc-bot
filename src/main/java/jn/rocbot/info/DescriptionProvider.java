package jn.rocbot.info;

import net.dv8tion.jda.core.entities.MessageEmbed;

public interface DescriptionProvider {
    MessageEmbed getDesc();
}
