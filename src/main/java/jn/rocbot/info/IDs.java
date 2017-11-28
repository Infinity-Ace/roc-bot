package jn.rocbot.info;

import java.util.HashMap;

import static jn.rocbot.info.IDs.ID_KEY.*;

public class IDs {
    public static HashMap<ID_KEY, Long> CHANNELS;
    public static HashMap<ID_KEY, Long> GUILDS;

    public static final long BOT = 377812695585390602L;

    public static void init(){
        CHANNELS = new HashMap<>();
        CHANNELS.put(CHANNEL_BOT_CHANNEL, 378546862627749908L);
        CHANNELS.put(CHANNEL_GENERAL, 325430508379176961L);

        GUILDS = new HashMap<>();
        GUILDS.put(GUILD_PHOENIX_II, 325430508379176961L);
    }

    public enum ID_KEY {
        CHANNEL_BOT_CHANNEL,
        CHANNEL_GENERAL,
        GUILD_PHOENIX_II;
    }
}