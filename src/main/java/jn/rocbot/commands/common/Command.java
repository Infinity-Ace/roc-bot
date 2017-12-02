package jn.rocbot.commands.common;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    //<editor-fold desc="Defaults">
    default boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    default boolean executed(boolean success, MessageReceivedEvent event) {
        return true;
    }

    default CommandType getType(){
        return getConfig().type;
    }

    default void sendMessage(String msg, MessageReceivedEvent event){
        event.getTextChannel().sendMessage(msg).complete();
    }
    //</editor-fold>

    void action(String[] args, MessageReceivedEvent event);

    String help();

    CommandConfig getConfig();

    enum PREFIX {
        NORMAL("!"),
        MODERATOR("~!"),
        MASTER("§");

        private final String prefix;

        PREFIX(String prefix){
            this.prefix = prefix;
        }
    }
}
