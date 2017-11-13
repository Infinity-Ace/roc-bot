package jn.rocbot.commands.common;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    String help();
    boolean executed(boolean success, MessageReceivedEvent event);
    CommandType getType();
    default void sendMessage(String msg, MessageReceivedEvent event){
        event.getJDA().getTextChannelById(378546862627749908L).sendMessage(msg).complete();
    }
}
