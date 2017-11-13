package jn.rocbot.commands.devcommands;

import jn.rocbot.Emojis;
import jn.rocbot.Main;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SayCommand implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String message = "";

        int i = 1;
        for(String arg : args){
            arg.replace("<EL>", Emojis.EL);
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
    public CommandType getType() {
        return CommandType.DEV;
    }
}
