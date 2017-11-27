package jn.rocbot.info;

import jn.rocbot.Main;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;

public class IDs {
    public static HashMap<String, Long> CHANNELS;
    public static HashMap<String, Long> GUILDS;

    public static void init(){
        CHANNELS = new HashMap<>();
        CHANNELS.put("bot-channel", 378546862627749908L);

        GUILDS = new HashMap<>();
        GUILDS.put("phoenix", 325430508379176961L);
    }
}