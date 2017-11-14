package jn.rocbot.commands.devcommands;

import jn.rocbot.Emojis;
import jn.rocbot.Main;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SayCommand implements Command{
    private CommandConfig config = new CommandConfig(CommandType.DEV, false);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String message = "";

        int i = 0;
        for(String arg : args){
            arg = arg.replace("<EL>", Emojis.EL);
            i++; if(!(i == args.length))
                message += arg + " ";
            else
                message += arg;
        } sendMessage(message, event);
    }

    @Override
    public String help() {
        return "";
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
