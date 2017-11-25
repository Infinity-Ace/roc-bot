package jn.rocbot.commands.modcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StatsCommand implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "Blame jens";
    }

    @Override
    public boolean executed(boolean success, MessageReceivedEvent event) {
        return true;
    }

    private CommandConfig config = new CommandConfig(CommandType.MOD, true);

    @Override
    public CommandConfig getConfig() {
        return config;
    }
}
