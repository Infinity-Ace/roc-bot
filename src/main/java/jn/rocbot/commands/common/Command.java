package jn.rocbot.commands.common;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
    public boolean called(String[] args, MessageReceivedEvent event);
    public void action(String[] args, MessageReceivedEvent event);
    public String help();
    public boolean executed(boolean success, MessageReceivedEvent event);
    public CommandType getType();
    public default void sendMessage(String msg, MessageReceivedEvent event){
        event.getTextChannel().sendMessage(msg).complete();
    }
}
