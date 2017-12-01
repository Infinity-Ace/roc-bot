package jn.rocbot.commands.common;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    String help();
    boolean executed(boolean success, MessageReceivedEvent event);

    CommandConfig getConfig();

    default CommandType getType(){
        return getConfig().type;
    }

    default void sendMessage(String msg, MessageReceivedEvent event){
        event.getTextChannel().sendMessage(msg).complete();
    }

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
