package jn.rocbot;

import java.util.HashMap;

import static jn.rocbot.IDs.ID_KEY.*;

public class IDs {
    public static HashMap<ID_KEY, Long> CHANNELS;
    public static HashMap<ID_KEY, Long> GUILDS;

    public static final long BOT_USER_IDLONG = 377812695585390602L;

    public static void init() {
        CHANNELS = new HashMap<>();

        // Phoenix II server
        CHANNELS.put(CHANNEL_GP2_BOT_CHANNEL, 378546862627749908L);
        CHANNELS.put(CHANNEL_GP2_GENERAL,     325430508379176961L);
        CHANNELS.put(CHANNEL_GP2_WELCOME,     390389477983387649L);
        CHANNELS.put(CHANNEL_GP2_MOD_LOGS,    392600148901101578L);
        //

        // Roc-bot control center
        CHANNELS.put(CHANNEL_GCC_LOGS_INFO,       389525278445273101L);
        CHANNELS.put(CHANNEL_GCC_LOGS_DEBUG,      389514936016961559L);
        CHANNELS.put(CHANNEL_GCC_LOGS_VERBOSE,    389515023141175316L);
        CHANNELS.put(CHANNEL_GCC_LOGS_ERRORS,     389527619692920832L);
        CHANNELS.put(CHANNEL_GCC_LOGS_CONNECTION, 389515057215701022L);

        CHANNELS.put(CHANNEL_GCC_LOGS_BOT_CHANNEL_RCWD, 389799498131701782L);

        CHANNELS.put(CHANNEL_GCC_TESTS,   389515306067951620L);
        CHANNELS.put(CHANNEL_GCC_GENERAL, 378949749883273220L);
        //

        // Mug's place
        CHANNELS.put(CHANNEL_GMH_BOT_INTERACT, 418511762720161802L);
        //

        // Self explanatory
        GUILDS = new HashMap<>();
        GUILDS.put(GUILD_PHOENIX_II,  325430508379176961L);
        GUILDS.put(GUILD_BOT_CC,      378949749883273217L);
        GUILDS.put(GUILD_MUG_HANGOUT, 408330926733656064L);
    }

    public enum ID_KEY {
        // Phoenix II server
        CHANNEL_GP2_BOT_CHANNEL,
        CHANNEL_GP2_GENERAL,
        CHANNEL_GP2_WELCOME,
        CHANNEL_GP2_MOD_LOGS,

        // Roc-bot control center
        CHANNEL_GCC_LOGS_INFO,
        CHANNEL_GCC_LOGS_DEBUG,
        CHANNEL_GCC_LOGS_VERBOSE,
        CHANNEL_GCC_LOGS_ERRORS,
        CHANNEL_GCC_LOGS_CONNECTION,
        CHANNEL_GCC_GENERAL,
        CHANNEL_GCC_TESTS,
        CHANNEL_GCC_LOGS_BOT_CHANNEL_RCWD,

        // Mug's place
        CHANNEL_GMH_BOT_INTERACT,

        // Guilds
        GUILD_PHOENIX_II,
        GUILD_BOT_CC,
        GUILD_MUG_HANGOUT
    }
}