package jn.rocbot.commands.normalcommands;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.Permissions.Moderators;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static jn.rocbot.commands.Commands.COMMANDS;

public class HelpCommand implements Command {
    private final static String HELP = "Usage: !help *command*";

    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            String allCommands = "Use *!help command* to get help on specific command";
            allCommands += "\nAvaible commands are: ";
            if(!Masters.isMaster(event.getAuthor())) {
                for (String key : COMMANDS.keySet()) {
                    if(COMMANDS.get(key).getType() == CommandType.NORMAL)
                        allCommands += "\n\t!" + key;
                } event.getTextChannel().sendMessage(allCommands).complete();
            }else{
                for (String key : COMMANDS.keySet()) {
                    switch (COMMANDS.get(key).getType()) {
                        case NORMAL:
                            allCommands += "\n\t!" + key;
                            break;
                        case MOD:
                            allCommands += "\n\t~!" + key;
                            break;
                        case DEV:
                            allCommands += "\n\t§" + key;
                            break;
                    }
                } allCommands += "\n\tUse *!help somecommand* to get help on specific command";

                event.getTextChannel().sendMessage(allCommands).complete();
            }
            if(Moderators.isModerator(event.getAuthor())){

            }
        } else if (args.length > 0){
            if(COMMANDS.containsKey(args[0].toLowerCase())){
                event.getTextChannel().sendMessage(COMMANDS.get(args[0].toLowerCase()).help()).complete();
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
    public CommandConfig getConfig() {
        return config;
    }

    @Override
    public CommandType getType() {
        return config.type;
    }
}