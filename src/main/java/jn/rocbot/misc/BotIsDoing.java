package jn.rocbot.misc;

import net.dv8tion.jda.core.entities.Game;

public class BotIsDoing {
    public static Game watchingYou = new WatchingYou();

    private static class WatchingYou extends Game{
        private WatchingYou() {
            super("Watching you");
        }
    }
}
