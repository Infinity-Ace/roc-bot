package jn.rocbot.commands;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelloCommand implements Command {
    private final static String HELP = "Usage: !Hello";
    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        boolean isMaster = false;
        for (Masters.Master master : Masters.MASTERS) {
            isMaster = master.longID == event.getAuthor().getIdLong();

            if(isMaster) break;
        }

        if (isMaster) {
            event.getTextChannel().sendMessage(Masters.fromLongID(event.getAuthor().getIdLong()).greeting).complete();
        } else {
            event.getTextChannel().sendMessage("Hello " + event.getAuthor().getName() + "!").complete();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public boolean executed(boolean success, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public CommandConfig getConfig() {
        return config;
    }

    @Override
    public CommandType getType() {
        return config.type;
    }
}
