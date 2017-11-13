package jn.rocbot.commands;

import jn.rocbot.Bot;
import jn.rocbot.Permissions.Masters;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand implements Command {
    private final static String HELP = "Usage: !help <whatever_comand_you_want_help_on>";
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            String allCommands = "Avaible commands are: ";
            if(!Masters.isMaster(event.getAuthor())) {
                for (String key : Bot.COMMANDS.keySet()) {
                    if(Bot.COMMANDS.get(key).getType() == CommandType.NORMAL)
                        allCommands += "\n\t!" + key;
                }

                event.getTextChannel().sendMessage(allCommands).complete();
            }
        } else if (args.length > 0){
            if(Bot.COMMANDS.containsKey(args[0].toLowerCase())){
                event.getTextChannel().sendMessage(Bot.COMMANDS.get(args[0].toLowerCase()).help()).complete();
            } else {
                event.getTextChannel().sendMessage("No command named *" + args[0] + "* use !help for a list of avaible commands").complete();
            }
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
    public CommandType getType() {
        return CommandType.NORMAL;
    }
}
