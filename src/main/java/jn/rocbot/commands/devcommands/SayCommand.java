package jn.rocbot.commands.devcommands;

import jn.rocbot.Main;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static jn.rocbot.Main.LOGTYPE.INFO;

public class SayCommand implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Main.log(INFO, "Executed say-command");
        sendMessage(args[0].replace("|", " "), event);
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
